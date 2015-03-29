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

package de.textmining.nerdle.question.answering;

import java.util.List;
import java.util.Map;
import java.util.SortedSet;

import org.apache.commons.configuration.ConfigurationException;

import de.textmining.nerdle.database.FactProvider;
import de.textmining.nerdle.question.answering.fact.matcher.FactMatcher;
import de.textmining.nerdle.question.answering.model.Answer;
import de.textmining.nerdle.question.answering.model.NerdleFact;
import de.textmining.nerdle.question.answering.model.Question;
import de.textmining.nerdle.question.answering.question.parsing.QuestionParser;
import etm.core.configuration.EtmManager;
import etm.core.monitor.EtmMonitor;
import etm.core.monitor.EtmPoint;

/**
 * Main implementation of @QuestionAnswerer interface. It requires three classes
 * in order to run: A @QuestionParser that interprets a question, a @FactProvider
 * that gives access to a database of facts, and a @FactMatcher that matches the
 * question to a fact in the database.
 * 
 */
public class MatchFactQuestionAnswerer implements QuestionAnswerer {

    private static final EtmMonitor etmMonitor = EtmManager.getEtmMonitor();

    private FactMatcher questionFactMatcher;
    private FactProvider factProvider;
    private QuestionParser questionParser;

    public MatchFactQuestionAnswerer(QuestionParser questionParser, FactMatcher questionFactMatcher, FactProvider factProvider) {
        this.questionFactMatcher = questionFactMatcher;
        this.factProvider = factProvider;
        this.questionParser = questionParser;
    }
    
    @Override
    public Answer answer(Question question) {

        EtmPoint point = etmMonitor.createPoint("MatchFactQuestionAnswerer:answer");

        Answer finalAnswer = new Answer();
        try {

            // The question parser interprets the question as a list of
            // NerdleFacts
            List<NerdleFact> questionDesciption = questionParser.analyzeQuestion(question);

            // The questionFactMatcher then matches this question against a data
            // source as given by the factProvider
            SortedSet<Map.Entry<String, Float>> answers = questionFactMatcher.getAnswers(factProvider, questionDesciption);

            for (Map.Entry<String, Float> answer : answers) {
                finalAnswer.add(answer.getKey());
            }

        } catch (ConfigurationException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            point.collect();
        }

        // Final answer contains the string results sorted by score
        return finalAnswer;
    }

}