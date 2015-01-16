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

package de.textmining.nerdle.question.answering.model;

import de.textmining.nerdle.evaluation.EvaluationEntry;
import de.textmining.nerdle.evaluation.Topic;

public class Question {

    private String question;

    public Question() {
        super();
    }

    public Question(String question) {
        this.question = question;
    }

    public Question(Topic topic, EvaluationEntry evaluationEntry) {
        this.question = evaluationEntry.getQuestion();
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

}
