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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.log4j.Logger;

import de_tu_berlin.dima.nerdle.ClearNLPHelper;
import de_tu_berlin.dima.nerdle.matcher.NerdleFactMatcher;
import de_tu_berlin.dima.nerdle.model.NerdleArg;
import de_tu_berlin.dima.nerdle.model.NerdleFact;
import de_tu_berlin.dima.nerdle.model.NerdlePredicate;
import de_tu_berlin.dima.nerdle.provider.FactProvider;

public class QuestionAnswer {

    protected final static Logger log = Logger.getLogger(QuestionAnswer.class);

    private static double TRESHOLD_WH0 = 0.0;
    private static double TRESHOLD_WHICH = 0.33;

    private NerdleFactMatcher nerdleFactMatcher;

    public QuestionAnswer(NerdleFactMatcher nerdleFactMatcher) {
        this.nerdleFactMatcher = nerdleFactMatcher;
    }

    public QuestionAnswerResponse answerToQuestion(FactProvider factProvider, String question) throws InterruptedException, ConfigurationException {

        log.debug("Handling question: " + question);

        QuestionAnswerResponse questionAnswerResponse = null;

        if (question.toLowerCase().contains("which")) {
            questionAnswerResponse = answerToWhich(factProvider, question);
        } else {
            questionAnswerResponse = answerToGeneric(factProvider, question);
        }

        return questionAnswerResponse;
    }

    public QuestionAnswerResponse answerToGeneric(FactProvider factProvider, String question) {

        List<NerdleFact> questionFacts = ClearNLPHelper.INSTANCE.extractFactsFromSentence(question, "");

        HashMap<NerdleFact, Double> answerToScore = new HashMap<NerdleFact, Double>();
        HashMap<NerdleFact, NerdleFact> answerToQuestion = new HashMap<NerdleFact, NerdleFact>();
        HashMap<NerdleFact, NerdleArg> answerToSearchArg = new HashMap<NerdleFact, NerdleArg>();

        for (NerdleFact questionFact : questionFacts) {

            NerdleArg searchArg = null;

            for (NerdleArg nerdleArg : questionFact.getArguments()) {
                if (nerdleArg.getArgLabel().startsWith("R-")) {
                    searchArg = nerdleArg;
                }
            }

            log.debug(questionFact);
            log.debug(searchArg);

            if (searchArg != null) {

                searchArg.setArgLabel(searchArg.getArgLabel().replace("R-", ""));

                long beginMatcher = System.currentTimeMillis();

                List<NerdleFact> answerExtractions = nerdleFactMatcher.match(factProvider, questionFact, searchArg);

                log.info("SCAN Matcher : " + (System.currentTimeMillis() - beginMatcher));

                log.debug("Facts founded: " + answerExtractions.size());

                for (NerdleFact answerExtraction : answerExtractions) {

                    double score = ScoreCalculator.calculate(questionFact, answerExtraction, searchArg);

                    if (score > TRESHOLD_WH0) {
                        answerToScore.put(answerExtraction, score);
                        answerToQuestion.put(answerExtraction, questionFact);
                        answerToSearchArg.put(answerExtraction, searchArg);
                    }

                }

            }

        }

        QuestionAnswerResponse questionAnswerResponse = new QuestionAnswerResponse(answerToScore, answerToQuestion, QuestionType.GENERIC, answerToSearchArg,
                questionFacts);

        return questionAnswerResponse;

    }

    public QuestionAnswerResponse answerToWhich(FactProvider factProvider, String question) throws InterruptedException, ConfigurationException {

        String cleanedQuestion = question.substring(5).trim();

        List<NerdleFact> questionFacts = ClearNLPHelper.INSTANCE.extractFactsFromSentence(cleanedQuestion, "");

        NerdleFact bestQuestionFact = getMaxConfidenceExtraction(questionFacts);

        HashMap<NerdleFact, Double> answerToScore = new HashMap<NerdleFact, Double>();
        HashMap<NerdleFact, Double> answerToScoreTemp = new HashMap<NerdleFact, Double>();
        HashMap<NerdleFact, NerdleFact> answerToQuestion = new HashMap<NerdleFact, NerdleFact>();
        HashMap<NerdleFact, NerdleArg> answerToSearchArg = new HashMap<NerdleFact, NerdleArg>();
        List<NerdleFact> retQuestionFacts = new ArrayList<NerdleFact>();

        if (bestQuestionFact != null) {

            NerdleFact firstWhoFact = new NerdleFact("", "");
            NerdleFact secondWhoFact = new NerdleFact("", "");

            NerdleArg firstSearchArg = new NerdleArg("Who", "", "A1", "nsubj");
            NerdleArg secondSearchArg = null;

            // first who question: 'Who is a ... ?'

            firstWhoFact.setPredicate(new NerdlePredicate("is", "be", "be.01"));

            for (NerdleArg nerdleArg : bestQuestionFact.getArguments()) {
                if (nerdleArg.getArgLabel().equals("A0")) {
                    firstWhoFact.addArgument(new NerdleArg(nerdleArg.getText(), "", "A2", "attr"));
                }
            }

            for (NerdleArg nerdleArg : bestQuestionFact.getArguments()) {
                if (nerdleArg.getArgLabel().equals("A1")) {
                    firstWhoFact.addArgument(new NerdleArg(nerdleArg.getText(), "", "A2", "attr"));
                }
            }

            // to show a 'Who' in the question graph
            firstWhoFact.addArgument(firstSearchArg);

            // second who question

            for (NerdleArg nerdleArg : bestQuestionFact.getArguments()) {
                if (nerdleArg.getArgLabel().equals("A0")) {
                    secondSearchArg = nerdleArg;
                }
            }

            for (NerdleArg nerdleArg : bestQuestionFact.getArguments()) {
                if (nerdleArg.getArgLabel().equals("A1")) {
                    secondSearchArg = nerdleArg;
                }
            }

            secondWhoFact.setPredicate(bestQuestionFact.getPredicate());

            secondWhoFact.setArguments(bestQuestionFact.getArguments());

            if (secondSearchArg != null) {

                // to show a 'Who' in the question graph
                secondSearchArg.setText("Who");

                retQuestionFacts.add(firstWhoFact);
                retQuestionFacts.add(secondWhoFact);

                //

                long beginMatcher1 = System.currentTimeMillis();

                List<NerdleFact> firstFacts = nerdleFactMatcher.match(factProvider, firstWhoFact, firstSearchArg);

                log.info("SCAN Matcher who 1: " + (System.currentTimeMillis() - beginMatcher1));

                long beginMatcher2 = System.currentTimeMillis();

                List<NerdleFact> secondFacts = nerdleFactMatcher.match(factProvider, secondWhoFact, secondSearchArg);

                log.info("SCAN Matcher who 2: " + (System.currentTimeMillis() - beginMatcher2));

                List<NerdleFact> firstAnswers = new ArrayList<NerdleFact>();
                List<NerdleFact> thirdAnswers = new ArrayList<NerdleFact>();

                for (NerdleFact answerExtraction : firstFacts) {
                    double score = ScoreCalculator.calculate(firstWhoFact, answerExtraction, firstSearchArg);

                    if (score >= TRESHOLD_WHICH) {
                        answerToScoreTemp.put(answerExtraction, score);
                        firstAnswers.add(answerExtraction);
                    }
                }

                for (NerdleFact answerExtraction : secondFacts) {
                    double score = ScoreCalculator.calculate(secondWhoFact, answerExtraction, secondSearchArg);

                    if (score >= TRESHOLD_WHICH) {
                        answerToScoreTemp.put(answerExtraction, score);
                        thirdAnswers.add(answerExtraction);
                    }
                }

                long beginWhichLoop = System.currentTimeMillis();

                for (NerdleFact firstAnswer : firstAnswers) {

                    for (NerdleFact thirdAnswer : thirdAnswers) {

                        if (Grouper.groupMath(firstAnswer, firstSearchArg, thirdAnswer, secondSearchArg)) {

                            answerToScore.put(firstAnswer, answerToScoreTemp.get(firstAnswer));

                            answerToScore.put(thirdAnswer, answerToScoreTemp.get(thirdAnswer));

                            answerToSearchArg.put(firstAnswer, firstSearchArg);

                            answerToSearchArg.put(thirdAnswer, secondSearchArg);

                            answerToQuestion.put(firstAnswer, firstWhoFact);

                            answerToQuestion.put(thirdAnswer, secondWhoFact);

                        }

                    }

                }

                log.info("SCAN Matcher which loop: " + (System.currentTimeMillis() - beginWhichLoop));

            }
        }

        QuestionAnswerResponse questionAnswerResponse = new QuestionAnswerResponse(answerToScore, answerToQuestion, QuestionType.WHICH, answerToSearchArg,
                retQuestionFacts);

        return questionAnswerResponse;
    }

    private NerdleFact getMaxConfidenceExtraction(List<NerdleFact> extractions) {
        if (extractions.size() > 0) {
            return extractions.get(0);
        }
        return null;
    }

}
