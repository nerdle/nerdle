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

import de.textmining.nerdle.database.FactProvider;
import de.textmining.nerdle.database.MVFactProvider;
import de.textmining.nerdle.database.MVSingleton;
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

        if (args.length != 4) {
            printUsage();
            return;
        }

        Topic topic = null;

        try {
            topic = Topic.valueOf(args[2].toUpperCase());
        } catch (Exception e) {
            System.err.println("topic: the topic used during the evaluation. simpsons, star_wars or star_trek");
            return;
        }

        System.out.println(args[3]);
        System.out.println();

        String nerdleConfigPath = args[0];
        int limit = Integer.parseInt(args[1]);

        List<Topic> topics = new ArrayList<>();
        topics.add(topic);

        List<QuestionType> questionsTypes = new ArrayList<>();
        questionsTypes.addAll(Arrays.asList(QuestionType.values()));

        Map<Topic, EvaluationSet> topicResourceMap = new HashMap<>();

        switch (topic) {
        case SIMPSONS:
            topicResourceMap.put(Topic.SIMPSONS, new EvaluationSet(Controller.class.getResourceAsStream("/simpsons.tsv")));
            break;
        case STAR_TREK:
            topicResourceMap.put(Topic.STAR_TREK, new EvaluationSet(Controller.class.getResourceAsStream("/star-trek.tsv")));
            break;
        case STAR_WARS:
            topicResourceMap.put(Topic.STAR_WARS, new EvaluationSet(Controller.class.getResourceAsStream("/star-wars.tsv")));
            break;
        }

        MVSingleton mvSingleton = new MVSingleton(nerdleConfigPath);

        ClearNLPQuestionParser questionParser = new ClearNLPQuestionParser();
        ExactQuestionFactMatcher questionFactMatcher = new ExactQuestionFactMatcher();

        FactProvider factProvider = null;

        switch (topic) {
        case SIMPSONS:
            factProvider = new MVFactProvider(mvSingleton.getConnections().get("simpsons"));
            break;
        case STAR_TREK:
            factProvider = new MVFactProvider(mvSingleton.getConnections().get("star-trek"));
            break;
        case STAR_WARS:
            factProvider = new MVFactProvider(mvSingleton.getConnections().get("star-wars"));
            break;
        }

        Map<Topic, QuestionAnswerer> topicQuestionAnswererMap = new HashMap<>();
        topicQuestionAnswererMap.put(topic, new MatchFactQuestionAnswerer(questionParser, questionFactMatcher, factProvider));

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

    private static void printUsage() {
        System.err.println("Usage: nerdle_config limit topic");
        System.err.println("nerdle_config: path to nerdle_config.properties file.");
        System.err.println("limit: the limit of questions used during the evaluation.");
        System.err.println("topic: the topic used during the evaluation. simpsons, star_wars or star_trek");
        System.err.println("desc: description of evaluation");
    }

}
