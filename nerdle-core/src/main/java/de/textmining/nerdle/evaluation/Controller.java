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

import de.textmining.nerdle.database.DBFactProvider;
import de.textmining.nerdle.database.DBSingleton;
import de.textmining.nerdle.question.answering.MatchFactQuestionAnswerer;
import de.textmining.nerdle.question.answering.QuestionAnswerer;
import de.textmining.nerdle.question.answering.fact.matcher.ExactQuestionFactMatcher;
import de.textmining.nerdle.question.answering.question.parsing.ClearNLPQuestionParser;
import etm.core.configuration.BasicEtmConfigurator;
import etm.core.configuration.EtmManager;
import etm.core.monitor.EtmMonitor;
import etm.core.renderer.SimpleTextRenderer;

public class Controller {

    private static EtmMonitor monitor;

    public static void main(String[] args) throws Exception {

        // configure measurement framework
        setup();

        if (args.length != 2) {
            System.err.println("Usage: nerdle_config");
            System.err.println("nerdle_config: path to nerdle_config.properties file.");
            System.err.println("limit: the limit of questions used during the evaluation.");
        }

        String nerdleConfigPath = args[0];
        int limit = Integer.parseInt(args[1]);

        List<Topic> topics = new ArrayList<>();
        topics.add(Topic.SIMPSONS);
//        topics.add(Topic.STAR_TREK);
//        topics.add(Topic.STAR_WARS);

        List<QuestionType> questionsTypes = new ArrayList<>();
        questionsTypes.addAll(Arrays.asList(QuestionType.values()));

        Map<Topic, EvaluationSet> topicResourceMap = new HashMap<>();

        topicResourceMap.put(Topic.SIMPSONS, new EvaluationSet(Controller.class.getResourceAsStream("/simpsons.tsv")));
//        topicResourceMap.put(Topic.STAR_TREK, new EvaluationSet(Controller.class.getResourceAsStream("/star-trek.tsv")));
//        topicResourceMap.put(Topic.STAR_WARS, new EvaluationSet(Controller.class.getResourceAsStream("/star-wars.tsv")));

        DBSingleton dbSingleton = new DBSingleton(nerdleConfigPath);

        ClearNLPQuestionParser questionParser = new ClearNLPQuestionParser();
        ExactQuestionFactMatcher questionFactMatcher = new ExactQuestionFactMatcher();

        DBFactProvider simpsonsFactProvider = new DBFactProvider(dbSingleton.getConnections().get("simpsons"));
//        DBFactProvider startrekFactProvider = new DBFactProvider(dbSingleton.getConnections().get("star-trek"));
//        DBFactProvider starwarsFactProvider = new DBFactProvider(dbSingleton.getConnections().get("star-wars"));

        Map<Topic, QuestionAnswerer> topicQuestionAnswererMap = new HashMap<>();
        topicQuestionAnswererMap.put(Topic.SIMPSONS, new MatchFactQuestionAnswerer(questionParser, questionFactMatcher, simpsonsFactProvider));
//        topicQuestionAnswererMap.put(Topic.STAR_TREK, new MatchFactQuestionAnswerer(questionParser, questionFactMatcher, startrekFactProvider));
//        topicQuestionAnswererMap.put(Topic.STAR_WARS, new MatchFactQuestionAnswerer(questionParser, questionFactMatcher, starwarsFactProvider));

        EvaluationConfig evaluationConfig = new EvaluationConfig(topics, questionsTypes, topicResourceMap, topicQuestionAnswererMap, limit);

        Evaluator evaluator = new Evaluator(evaluationConfig);

        evaluator.start();

        // visualize results
        monitor.render(new SimpleTextRenderer());

        // shutdown measurement framework
        tearDown();
    }

    private static void setup() {
        BasicEtmConfigurator.configure(true);
        monitor = EtmManager.getEtmMonitor();
        monitor.start();
    }

    private static void tearDown() {
        monitor.stop();
    }

}
