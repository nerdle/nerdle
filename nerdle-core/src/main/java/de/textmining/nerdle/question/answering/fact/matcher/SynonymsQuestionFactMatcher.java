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

package de.textmining.nerdle.question.answering.fact.matcher;

import java.util.*;
import java.util.Map.Entry;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import de.textmining.nerdle.semantics.VnpbMappings;
import de.textmining.nerdle.utils.ItemCounter;
import de.textmining.nerdle.utils.MapSorter;

import org.apache.log4j.Logger;

import de.textmining.nerdle.question.answering.model.NerdleFact;
import de.textmining.nerdle.database.FactProvider;
import etm.core.configuration.EtmManager;
import etm.core.monitor.EtmMonitor;
import etm.core.monitor.EtmPoint;

/**
 * Main implementation of QuestionFactMatcher interface. The answers are sorted
 * by score, highest first.
 * 
 */
public class SynonymsQuestionFactMatcher implements FactMatcher {

    private static final int SYNONYMS_LIMIT = 10;

    private static final EtmMonitor etmMonitor = EtmManager.getEtmMonitor();

    protected final static Logger log = Logger.getLogger(SynonymsQuestionFactMatcher.class);

    @Override
    public SortedSet<Entry<String, Float>> getAnswers(FactProvider factProvider, List<NerdleFact> questionDesciption) {

        EtmPoint point = etmMonitor.createPoint("SynonymsQuestionFactMatcher:getAnswers");

        SortedSet<Entry<String, Float>> sortedAnswers = new TreeSet<>();

        if (questionDesciption.size() == 1) {

            NerdleFact firstQuestionFact = questionDesciption.get(0);

            List<NerdleFact> questionFactAndSynonyms = new ArrayList<>();
            questionFactAndSynonyms.add(firstQuestionFact);
            questionFactAndSynonyms.addAll(VnpbMappings.getInstance().getSynonyms(firstQuestionFact, SYNONYMS_LIMIT));

            ItemCounter answerScore = new ItemCounter();

            for (NerdleFact questionFact : questionFactAndSynonyms) {
                List<NerdleFact> matchingFacts = factProvider.getFactsByMatch(questionFact, questionFact.getQuestionArg());

                Multimap<String, NerdleFact> answerEvidence = HashMultimap.create();

                for (NerdleFact matchingFact : matchingFacts) {
                    String answer = matchingFact.getArgument(questionFact.getQuestionArg().getArgLabel());
                    answerEvidence.put(answer, matchingFact);

                    answerScore.put(answer, 1f);
                }
            }

            sortedAnswers = MapSorter.sortByValueDesc(answerScore.getAll());

            point.collect();
            return sortedAnswers;

        }

        point.collect();
        return sortedAnswers;
    }
}
