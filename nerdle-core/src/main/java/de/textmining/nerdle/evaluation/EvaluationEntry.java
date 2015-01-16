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

import java.util.HashSet;
import java.util.Set;

public class EvaluationEntry {

    private String qid;
    private String type;
    private String question;
    private String answer;
    private String source;
    private Set<String> answerChoices = new HashSet<>();

    public EvaluationEntry() {
        super();
    }

    public EvaluationEntry(String qid, String type, String question, String answer, String source) {
        super();
        this.qid = qid;
        this.type = type;
        this.question = question;
        this.answer = answer;
        this.source = source;
    }

    public EvaluationEntry(String[] entry) {
        System.out.println(entry.length);
        if (entry.length != 7) {
            throw new IllegalArgumentException("Entry must have 7 fields.");
        }
        this.qid = "";
        this.type = "";
        this.question = entry[0];
        this.answerChoices.add(entry[1]);
        this.answerChoices.add(entry[2]);
        this.answerChoices.add(entry[3]);
        this.answerChoices.add(entry[4]);
        this.answer = entry[5];
        this.source = entry[6];
    }

    public String getQid() {
        return qid;
    }

    public void setQid(String qid) {
        this.qid = qid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Set<String> getAnswerChoices() {
        return answerChoices;
    }

    public void setAnswerChoices(Set<String> answerChoices) {
        this.answerChoices = answerChoices;
    }


}
