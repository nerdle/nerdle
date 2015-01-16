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

import de.textmining.nerdle.evaluation.metrics.StringSetMetric;
import de.textmining.nerdle.question.answering.model.Answer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BooleanAnswerEvaluator extends AnswerEvaluator {

    private StringSetMetric stringSetMetric;

    public BooleanAnswerEvaluator(StringSetMetric stringSetMetric) {
        this.stringSetMetric = stringSetMetric;
    }

    @Override
    public Judgment evaluate(Answer answerToEvaluate, EvaluationEntry evaluationEntry) {

        List<String> answers = answerToEvaluate.getAnswers();

        String correctAnswer = evaluationEntry.getAnswer();
        List<String> correctAnswerSet = new ArrayList<>();
        correctAnswerSet.add(correctAnswer);

        for (String answer : answers) {

            String[] tokens = answer.split("\\s+");
            List<String> answerSet = Arrays.asList(tokens);

            if (stringSetMetric.compare(correctAnswerSet, answerSet) > 0.0) {
                return new BooleanJudgment(true);
            }
        }

        return new BooleanJudgment(false);
    }

}
