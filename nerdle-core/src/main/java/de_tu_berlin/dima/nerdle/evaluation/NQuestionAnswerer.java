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

package de_tu_berlin.dima.nerdle.evaluation;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.configuration.ConfigurationException;

import de.tu_berlin.dima.qaeval.Answer;
import de.tu_berlin.dima.qaeval.Question;
import de.tu_berlin.dima.qaeval.QuestionAnswerer;
import de.tu_berlin.dima.qaeval.Topic;
import de_tu_berlin.dima.nerdle.provider.FactProvider;
import de_tu_berlin.dima.nerdle.qa.GroupedAnswer;
import de_tu_berlin.dima.nerdle.qa.NerdleAnswerer;

public class NQuestionAnswerer implements QuestionAnswerer {

    private NerdleAnswerer nerdleAnswerer;
    private HashMap<Topic, FactProvider> topicToFactProvider;

    public NQuestionAnswerer(NerdleAnswerer nerdleAnswerer, HashMap<Topic, FactProvider> topicToFactProvider) {
        this.nerdleAnswerer = nerdleAnswerer;
        this.topicToFactProvider = topicToFactProvider;
    }

    @Override
    public Answer answer(Topic topic, Question question) {
        Answer answer = new Answer();
        try {
            List<GroupedAnswer> answers = nerdleAnswerer.getAnswers(topicToFactProvider.get(topic), question.getQuestion(), 1);

            for (GroupedAnswer groupedAnswer : answers) {
                answer.add(groupedAnswer.getSubject());
            }

        } catch (ConfigurationException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return answer;
    }

}