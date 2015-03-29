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

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

import de.textmining.nerdle.TestDBConnection;
import de.textmining.nerdle.database.DBFactProvider;
import de.textmining.nerdle.question.answering.fact.matcher.QuestionFactMatcher;
import de.textmining.nerdle.question.answering.model.Answer;
import de.textmining.nerdle.question.answering.model.Question;
import de.textmining.nerdle.question.answering.question.parsing.ClearNLPQuestionParser;

public class MatchFactQuestionAnswererTest {

    private static QuestionAnswerer questionAnswerer;

    @BeforeClass
    public static void setup() throws Exception {
        ClearNLPQuestionParser questionParser = new ClearNLPQuestionParser();
        QuestionFactMatcher questionFactMatcher = new QuestionFactMatcher();
        DBFactProvider factProvider = new DBFactProvider(TestDBConnection.small());

        questionAnswerer = new MatchFactQuestionAnswerer(questionParser, questionFactMatcher, factProvider);
    }

    @Test
    public void testWho() {
        Question question = new Question("Who was born in Springfield?");
        Answer answer = questionAnswerer.answer(question);
        assertEquals(0, answer.getAnswers().size());

        question = new Question("Who is cool?");
        answer = questionAnswerer.answer(question);
        assertEquals(1, answer.getAnswers().size());

        assertEquals("Homer", answer.getAnswers().get(0));
    }

    @Test
    public void testNoQuestionArg() {
        Question question = new Question("Which one has devoted her life to celibacy?");
        Answer answer = questionAnswerer.answer(question);

        assertEquals(0, answer.getAnswers().size());
    }
}
