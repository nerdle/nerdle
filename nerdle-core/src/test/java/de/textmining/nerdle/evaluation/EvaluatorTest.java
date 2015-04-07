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

package de.textmining.nerdle.evaluation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;

import de.textmining.nerdle.question.answering.DummyQuestionAnswerer;
import de.textmining.nerdle.question.answering.QuestionAnswerer;

public class EvaluatorTest {

    @Test
    @Ignore
    public void test() throws Exception {
        List<Topic> topics = new ArrayList<>();
        topics.addAll(Arrays.asList(Topic.values()));

        List<QuestionType> questionsTypes = new ArrayList<>();
        questionsTypes.addAll(Arrays.asList(QuestionType.values()));

        Map<Topic, EvaluationSet> topicResourceMap = new HashMap<>();

        topicResourceMap.put(Topic.SIMPSONS, new EvaluationSet(Controller.class.getResourceAsStream("/simpsons.tsv")));
        topicResourceMap.put(Topic.STAR_TREK, new EvaluationSet(Controller.class.getResourceAsStream("/star-trek.tsv")));
        topicResourceMap.put(Topic.STAR_WARS, new EvaluationSet(Controller.class.getResourceAsStream("/star-wars.tsv")));

        QuestionAnswerer questionAnswerer = new DummyQuestionAnswerer();

        Map<Topic, QuestionAnswerer> topicQuestionAnswererMap = new HashMap<>();
        topicQuestionAnswererMap.put(Topic.SIMPSONS, questionAnswerer);
        topicQuestionAnswererMap.put(Topic.STAR_TREK, questionAnswerer);
        topicQuestionAnswererMap.put(Topic.STAR_WARS, questionAnswerer);

        EvaluationConfig evaluationConfig = new EvaluationConfig(topics, questionsTypes, topicResourceMap, topicQuestionAnswererMap, 10);

        Evaluator evaluator = new Evaluator(evaluationConfig);

        evaluator.start();
    }

}
