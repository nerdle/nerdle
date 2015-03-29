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

package de.textmining.nerdle.question.answering;

import de.textmining.nerdle.question.answering.model.Answer;
import de.textmining.nerdle.question.answering.model.Question;

import java.util.ArrayList;
import java.util.List;

/**
 * Dummy implementation of @QuestionAnswerer interface.
 * 
 */
public class DummyQuestionAnswerer implements QuestionAnswerer {

    @Override
    public Answer answer(Question question) {
        Answer answer = new Answer();
        List<String> answers = new ArrayList<>();

        if (Math.random() > 0.2) {
            answers.add("dummy answer");
            answers.add("wrong answer");

            if (Math.random() > 0.5) {
                answers.add("correct_answer");
            }
        }

        answer.setAnswers(answers);

        return answer;
    }

}
