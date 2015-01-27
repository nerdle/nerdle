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

import de.textmining.nerdle.evaluation.metrics.JaccardMetric;
import de.textmining.nerdle.information.extraction.ClearNLPHelper;
import de.textmining.nerdle.question.answering.QuestionAnswerer;
import de.textmining.nerdle.question.answering.model.Answer;
import de.textmining.nerdle.question.answering.model.Question;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;

public class Evaluator {

    private EvaluationConfig evaluationConfig;
    private BooleanAnswerEvaluator booleanAnswerEvaluator;

    public Evaluator(EvaluationConfig evaluationConfig) {
        super();
        this.evaluationConfig = evaluationConfig;
        this.booleanAnswerEvaluator = new BooleanAnswerEvaluator(new JaccardMetric());
    }

    public void start(QuestionAnswerer questionAnswerer) throws URISyntaxException, FileNotFoundException {

        ClearNLPHelper.INSTANCE.getClass();

        System.out.println("QA Evaluaton Framework Results");
        System.out.println("==============================");
        System.out.println();
        System.out.println("Evaluated topics: " + evaluationConfig.getTopics());
        System.out.println();

        for (Topic topic : evaluationConfig.getTopics()) {

            System.out.println("topic = " + topic);
            int questions = 0;

            int correctAnswers = 0;
            int incorrectAnswers = 0;
            int noAnswers = 0;

            String filename = evaluationConfig.getTopicResourceMap().get(topic);

            if (filename != null) {
                EvaluationSet evaluationSet = new EvaluationSet(topic, new FileInputStream(filename));

                int index = 0;
                for (EvaluationEntry evaluationEntry : evaluationSet.getEvaluationSet()) {

                    if (index == evaluationConfig.getLimit()) {
                        break;
                    }

                    // increment
                    questions++;

                    Question question = new Question(evaluationEntry.getQuestion());
                    Answer answerToEvaluate = questionAnswerer.answer(question);

                    if (answerToEvaluate.getAnswers().size() > 0) {
                        BooleanJudgment judgment = (BooleanJudgment) booleanAnswerEvaluator.evaluate(answerToEvaluate, evaluationEntry);

                        if (judgment.isCorrect()) {
                            // increment
                            correctAnswers++;
                        } else {
                            // increment
                            incorrectAnswers++;
                        }

                    } else {
                        // increment
                        noAnswers++;
                    }

                    index++;

                }
            }

            // print results
            System.out.println("Topic: " + topic);
            System.out.println(String.format("%-20s%10s", "Total questions:", questions));
            System.out.println(String.format("%-20s%10s", "Correct Answers:", correctAnswers));
            System.out.println(String.format("%-20s%10s", "Incorrect Answers:", incorrectAnswers));
            System.out.println(String.format("%-20s%10s", "No Answers:", noAnswers));
            System.out.println(String.format("%-20s%10s", "Precision:", (1.0 * correctAnswers) / (1.0 * questions)));
            System.out.println();

        }

    }
}
