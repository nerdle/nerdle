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

import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.*;

public class EvaluationConfig {

    /**
     * The topics which will be used during the evaluation.
     */
    private List<Topic> topics;

    /**
     * The questions types used during the evaluation.
     */
    private List<QuestionType> questionsTypes;

    /**
     * The resources of the topics.
     */
    private Map<Topic, String> topicResourceMap;

    /**
     * The limit of questions used during the evaluation.
     */
    private int limit = -1;

    public EvaluationConfig() throws URISyntaxException {
        super();
        this.topics = new ArrayList<>();
        this.topics.addAll(Arrays.asList(Topic.values()));

        this.questionsTypes = new ArrayList<>();
        this.questionsTypes.addAll(Arrays.asList(QuestionType.values()));

        this.topicResourceMap = new HashMap<Topic, String>();

        this.topicResourceMap.put(Topic.SIMPSONS, Paths.get(getClass().getResource("/simpsons.tsv").toURI()).toFile().getPath());
        this.topicResourceMap.put(Topic.STAR_TREK, Paths.get(getClass().getResource("/star-trek.tsv").toURI()).toFile().getPath());
        this.topicResourceMap.put(Topic.STAR_WARS, Paths.get(getClass().getResource("/star-wars.tsv").toURI()).toFile().getPath());
    }

    public EvaluationConfig(List<Topic> topics, List<QuestionType> questionsTypes, Map<Topic, String> topicResourceMap, int limit) {
        super();
        this.topics = topics;
        this.questionsTypes = questionsTypes;
        this.topicResourceMap = topicResourceMap;
        this.limit = limit;
    }

    public List<Topic> getTopics() {
        return topics;
    }

    public void setTopics(List<Topic> topics) {
        this.topics = topics;
    }

    public List<QuestionType> getQuestionsTypes() {
        return questionsTypes;
    }

    public void setQuestionsTypes(List<QuestionType> questionsTypes) {
        this.questionsTypes = questionsTypes;
    }

    public Map<Topic, String> getTopicResourceMap() {
        return topicResourceMap;
    }

    public void setTopicResourceMap(Map<Topic, String> topicResourceMap) {
        this.topicResourceMap = topicResourceMap;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

}