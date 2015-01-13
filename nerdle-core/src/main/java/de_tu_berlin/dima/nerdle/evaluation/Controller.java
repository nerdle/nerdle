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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.tu_berlin.dima.qaeval.EvaluationConfig;
import de.tu_berlin.dima.qaeval.Evaluator;
import de.tu_berlin.dima.qaeval.QuestionAnswerer;
import de.tu_berlin.dima.qaeval.Topic;
import de_tu_berlin.dima.nerdle.provider.DBFactProvider;
import de_tu_berlin.dima.nerdle.provider.DBSingleton;
import de_tu_berlin.dima.nerdle.provider.FactProvider;
import de_tu_berlin.dima.nerdle.qa.NerdleAnswerer;

public class Controller {
    public static void main(String[] args) throws Exception {
        EvaluationConfig evaluationConfig = new EvaluationConfig();
        evaluationConfig.setLimit(10);
        List<Topic> topics = new ArrayList<>();
        topics.add(Topic.SIMPSONS);
        evaluationConfig.setTopics(topics);

        Evaluator evaluator = new Evaluator(evaluationConfig);
        NerdleAnswerer nerdleAnswerer = new NerdleAnswerer();
        HashMap<Topic, FactProvider> topicToFactProvider = new HashMap<>();

        DBSingleton dbSingleton = new DBSingleton();
        topicToFactProvider.put(Topic.SIMPSONS, new DBFactProvider(dbSingleton.getConnections().get("nerdle_simpsons")));

        QuestionAnswerer questionAnswerer = new NQuestionAnswerer(nerdleAnswerer, topicToFactProvider);
        evaluator.start(questionAnswerer);
    }
}
