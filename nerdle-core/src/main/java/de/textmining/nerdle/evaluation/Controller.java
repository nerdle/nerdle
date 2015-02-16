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

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.textmining.nerdle.database.DBFactProvider;
import de.textmining.nerdle.database.DBSingleton;
import de.textmining.nerdle.question.answering.MatchFactQuestionAnswerer;
import de.textmining.nerdle.question.answering.QuestionAnswerer;
import de.textmining.nerdle.question.answering.fact.matcher.ExactQuestionFactMatcher;
import de.textmining.nerdle.question.answering.question.parsing.ClearNLPQuestionParser;

public class Controller {

    public static void main(String[] args) throws Exception {
        List<Topic> topics = new ArrayList<>();
        topics.add(Topic.SIMPSONS);
//        topics.add(Topic.STAR_TREK);
//        topics.add(Topic.STAR_WARS);

        List<QuestionType> questionsTypes = new ArrayList<>();
        questionsTypes.addAll(Arrays.asList(QuestionType.values()));

        Map<Topic, String> topicResourceMap = new HashMap<>();

        topicResourceMap.put(Topic.SIMPSONS, Paths.get(Controller.class.getResource("/simpsons.tsv").toURI()).toFile().getPath());
        // topicResourceMap.put(Topic.STAR_TREK,
        // Paths.get(Controller.class.getResource("/star-trek.tsv").toURI()).toFile().getPath());
        // topicResourceMap.put(Topic.STAR_WARS,
        // Paths.get(Controller.class.getResource("/star-wars.tsv").toURI()).toFile().getPath());

        DBSingleton dbSingleton = new DBSingleton();

        ClearNLPQuestionParser questionParser = new ClearNLPQuestionParser();
        ExactQuestionFactMatcher questionFactMatcher = new ExactQuestionFactMatcher();

        DBFactProvider factProvider = new DBFactProvider(dbSingleton.getConnections().get("simpsons"));

        QuestionAnswerer questionAnswerer = new MatchFactQuestionAnswerer(questionParser, questionFactMatcher, factProvider);

        Map<Topic, QuestionAnswerer> topicQuestionAnswererMap = new HashMap<>();
        topicQuestionAnswererMap.put(Topic.SIMPSONS, questionAnswerer);
        // topicQuestionAnswererMap.put(Topic.STAR_TREK, questionAnswerer);
        // topicQuestionAnswererMap.put(Topic.STAR_WARS, questionAnswerer);

        EvaluationConfig evaluationConfig = new EvaluationConfig(topics, questionsTypes, topicResourceMap, topicQuestionAnswererMap, 1);

        Evaluator evaluator = new Evaluator(evaluationConfig);

        evaluator.start();
    }

}
