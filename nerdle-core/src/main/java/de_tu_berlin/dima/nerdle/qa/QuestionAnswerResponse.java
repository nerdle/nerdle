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

package de_tu_berlin.dima.nerdle.qa;

import java.util.HashMap;
import java.util.List;

import de_tu_berlin.dima.nerdle.model.NerdleArg;
import de_tu_berlin.dima.nerdle.model.NerdleFact;

public class QuestionAnswerResponse {

    private HashMap<NerdleFact, Double> answerToScore;
    private HashMap<NerdleFact, NerdleFact> answerToQuestion;
    private QuestionType questionType;
    private HashMap<NerdleFact, NerdleArg> answerToSearchArg;
    private List<NerdleFact> questionExtractions;

    public QuestionAnswerResponse(HashMap<NerdleFact, Double> answerToScore, HashMap<NerdleFact, NerdleFact> answerToQuestion, QuestionType questionType,
            HashMap<NerdleFact, NerdleArg> answerToSearchArg, List<NerdleFact> questionExtractions) {

        this.answerToScore = answerToScore;
        this.answerToQuestion = answerToQuestion;
        this.questionType = questionType;
        this.answerToSearchArg = answerToSearchArg;
        this.questionExtractions = questionExtractions;
    }

    public HashMap<NerdleFact, Double> getAnswerToScore() {
        return answerToScore;
    }

    public void setAnswerToScore(HashMap<NerdleFact, Double> answerToScore) {
        this.answerToScore = answerToScore;
    }

    public HashMap<NerdleFact, NerdleFact> getAnswerToQuestion() {
        return answerToQuestion;
    }

    public void setAnswerToQuestion(HashMap<NerdleFact, NerdleFact> answerToQuestion) {
        this.answerToQuestion = answerToQuestion;
    }

    public QuestionType getQuestionType() {
        return questionType;
    }

    public void setQuestionType(QuestionType questionType) {
        this.questionType = questionType;
    }

    public HashMap<NerdleFact, NerdleArg> getAnswerToSearchArg() {
        return answerToSearchArg;
    }

    public void setAnswerToSearchArg(HashMap<NerdleFact, NerdleArg> answerToSearchArg) {
        this.answerToSearchArg = answerToSearchArg;
    }

    public List<NerdleFact> getQuestionExtractions() {
        return questionExtractions;
    }

    public void setQuestionExtractions(List<NerdleFact> questionExtractions) {
        this.questionExtractions = questionExtractions;
    }

}
