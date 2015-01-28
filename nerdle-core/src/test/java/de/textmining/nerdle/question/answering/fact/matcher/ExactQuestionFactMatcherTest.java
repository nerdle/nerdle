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

package de.textmining.nerdle.question.answering.fact.matcher;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Map.Entry;
import java.util.SortedSet;

import org.junit.BeforeClass;
import org.junit.Test;

import de.textmining.nerdle.TestDBConnection;
import de.textmining.nerdle.database.DBFactProvider;
import de.textmining.nerdle.question.answering.model.NerdleFact;
import de.textmining.nerdle.question.answering.model.Question;
import de.textmining.nerdle.question.answering.question.parsing.ClearNLPQuestionParser;

public class ExactQuestionFactMatcherTest {

    private static ClearNLPQuestionParser questionParser;
    private static DBFactProvider factProvider;
    private static ExactQuestionFactMatcher questionFactMatcher;

    @BeforeClass
    public static void setup() throws Exception {
        questionParser = new ClearNLPQuestionParser();
        factProvider = new DBFactProvider(TestDBConnection.small());
        questionFactMatcher = new ExactQuestionFactMatcher();
    }

    @Test
    public void testNoQuestionArg() throws Exception {
        Question question = new Question("Which one has devoted her life to celibacy?");
        List<NerdleFact> analyzedQuestions = questionParser.analyzeQuestion(question);
        SortedSet<Entry<String, Float>> answers = questionFactMatcher.getAnswers(factProvider, analyzedQuestions);

        assertEquals(0, answers.size());
    }
}
