/**
 * Copyright 2014 DIMA Research Group, TU Berlin (http://www.dima.tu-berlin.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de_tu_berlin.dima.nerdle.qa;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

import de_tu_berlin.dima.nerdle.matcher.DBFactMatcher;
import de_tu_berlin.dima.nerdle.matcher.FactMatcher;
import de_tu_berlin.dima.nerdle.matcher.NerdleFactMatcher;
import de_tu_berlin.dima.nerdle.matcher.TinkerpopFactMatcher;
import de_tu_berlin.dima.nerdle.model.NerdleFact;
import de_tu_berlin.dima.nerdle.provider.FactProvider;
import de_tu_berlin.dima.nerdle.util.ResourceManager;

public class NerdleAnswerer {

    protected final static Logger log = Logger.getLogger(NerdleAnswerer.class);

    private static int MAX = 10;
    private static final String FACT_PROVIDER_TINKER_POP = "TINKERPOP";
    private static final String FACT_PROVIDER_DB = "DB";

    private QuestionAnswer questionParser;

    public NerdleAnswerer() {
        try {
            PropertiesConfiguration propertiesConfiguration = new PropertiesConfiguration(ResourceManager.getResourcePath(File.separator
                    + "nerdle_config.properties"));

            String factproviderStr = propertiesConfiguration.getString("factprovider");

            FactMatcher factMatcher = null;

            if (factproviderStr.equals(FACT_PROVIDER_TINKER_POP)) {
                factMatcher = new TinkerpopFactMatcher();
            } else if (factproviderStr.equals(FACT_PROVIDER_DB)) {
                factMatcher = new DBFactMatcher();
            } else {
                throw new IllegalArgumentException("Please use TINKERPOP or DB as fact provider.");
            }

            NerdleFactMatcher nerdleFactMatcher = new NerdleFactMatcher(factMatcher);
            questionParser = new QuestionAnswer(nerdleFactMatcher);

        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
    }

    public NerdleAnswerer(QuestionAnswer questionParser) {
        this.questionParser = questionParser;
    }

    public List<GroupedAnswer> getAnswers(FactProvider factProvider, String question, Integer page) throws InterruptedException, ConfigurationException {

        log.info("");

        long beginLoad = System.currentTimeMillis();

        log.info("SCAN LOAD : " + (System.currentTimeMillis() - beginLoad));

        long beginAnswer = System.currentTimeMillis();

        QuestionAnswerResponse questionAnswerResponse = questionParser.answerToQuestion(factProvider, question);

        log.info("SCAN ANSWER: " + (System.currentTimeMillis() - beginAnswer));

        HashMap<String, Entry<String, List<NerdleFact>>> groupMap = null;

        long beginGrouping = System.currentTimeMillis();

        groupMap = Grouper.group(questionAnswerResponse);

        log.info("SCAN GROUPING : " + (System.currentTimeMillis() - beginGrouping));

        long beginSorting = System.currentTimeMillis();

        HashMap<String, List<NerdleFact>> groupAndSortedElements = new HashMap<String, List<NerdleFact>>();
        HashMap<String, DoubleScore> groupToScore = new HashMap<String, DoubleScore>();
        GroupScoreComparator groupScoreComparator = new GroupScoreComparator(groupToScore);
        TreeMap<String, DoubleScore> sortedGroupMap = new TreeMap<String, DoubleScore>(groupScoreComparator);

        int totalScoreSum = 0;

        for (Entry<NerdleFact, Double> entry : questionAnswerResponse.getAnswerToScore().entrySet()) {
            totalScoreSum += entry.getValue();
        }

        for (Entry<String, Entry<String, List<NerdleFact>>> entry : groupMap.entrySet()) {

            HashMap<NerdleFact, Double> map = new HashMap<NerdleFact, Double>();
            ScoreComparator bvc = new ScoreComparator(map);
            TreeMap<NerdleFact, Double> sortedMap = new TreeMap<NerdleFact, Double>(bvc);

            ArrayList<Double> scoreList = new ArrayList<Double>();

            for (NerdleFact extraction : entry.getValue().getValue()) {

                Double score = questionAnswerResponse.getAnswerToScore().get(extraction);

                scoreList.add(score);

                map.put(extraction, score);

            }

            groupToScore.put(entry.getKey(), calculateGroupScore(scoreList, totalScoreSum));

            sortedMap.putAll(map);

            ArrayList<NerdleFact> facts = new ArrayList<NerdleFact>();
            facts.addAll(sortedMap.keySet());

            groupAndSortedElements.put(entry.getKey(), facts);

        }

        sortedGroupMap.putAll(groupToScore);

        log.info("SCAN SORTING: " + (System.currentTimeMillis() - beginSorting));

        // pagination

        int answerSize = sortedGroupMap.size();

        int offset = MAX * (page - 1);

        int fromIndex = Math.min(offset, answerSize);
        int toIndex = Math.min(offset + MAX, answerSize);

        long beginGenerating = System.currentTimeMillis();

        // Generate sorted and grouped answers
        HashMap<String, ArrayList<Answer>> answers = new HashMap<String, ArrayList<Answer>>();
        List<GroupedAnswer> groupedAnswers = new ArrayList<GroupedAnswer>();

        int index = 0;
        for (Entry<String, DoubleScore> sortedGroup : sortedGroupMap.entrySet()) {

            if (index >= fromIndex && index < toIndex) {

                ArrayList<Answer> aList = new ArrayList<Answer>();
                for (NerdleFact sortedElement : groupAndSortedElements.get(sortedGroup.getKey())) {

                    double roundedScore = Math.round(questionAnswerResponse.getAnswerToScore().get(sortedElement) * 100) / 100.0;

                    aList.add(new Answer(sortedElement.getSentence(), roundedScore, sortedElement.getSource()));

                }

                groupedAnswers.add(new GroupedAnswer(groupMap.get(sortedGroup.getKey()).getKey(), aList, groupToScore.get(sortedGroup.getKey()).getMaxScore(),
                        groupToScore.get(sortedGroup.getKey()).getSumScore()));

                answers.put(sortedGroup.getKey(), aList);

            }

            index++;
        }

        log.info("SCAN GENERATING: " + (System.currentTimeMillis() - beginGenerating));

        return groupedAnswers;
    }

    private DoubleScore calculateGroupScore(List<Double> scoreList, int totalScoreSum) {

        double max = Double.MIN_VALUE;
        double sum = 0;
        for (Double score : scoreList) {
            if (max < score) {
                max = score;
            }
            sum += score;
        }

        return new DoubleScore(max, sum);

    }
}
