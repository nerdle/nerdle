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

package de.textmining.nerdle.question.parsing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import de.textmining.nerdle.information.extraction.ClearNLPHelper;
import de.textmining.nerdle.question.answering.model.NerdleArg;
import de.textmining.nerdle.question.answering.model.NerdleFact;
import de.textmining.nerdle.question.answering.model.NerdlePredicate;
import de.textmining.nerdle.question.answering.model.Question;
import de.textmining.nerdle.question.answering.question.parsing.ClearNLPQuestionParser;

public class ClearNLPQuestionParserTest {

    private static ClearNLPQuestionParser questionParser;

    @BeforeClass
    public static void setup() {
        questionParser = new ClearNLPQuestionParser();
        ClearNLPHelper.INSTANCE.getClass();
    }

    @Test
    public void test() throws Exception {

    }

    @Test
    public void testWho() throws Exception {
        Question question = new Question("Who is Homer’s father?");

        List<NerdleFact> analyzedQuestions = questionParser.analyzeQuestion(question);

        assertEquals(1, analyzedQuestions.size());

        NerdleFact analyzedQuestion = analyzedQuestions.get(0);

        // predicate
        assertEquals("be.01", analyzedQuestion.getPredicate().getRolesetID());
        assertEquals(new NerdlePredicate("is", "be", "be.01"), analyzedQuestion.getPredicate());

        // arguments
        assertTrue(analyzedQuestion.getArguments().contains(new NerdleArg("Who", "WP", "R-A1", "nsubj")));
        assertTrue(analyzedQuestion.getArguments().contains(new NerdleArg("Homer 's father", "NNP POS NN", "A2", "attr")));

        // question argument
        assertNotNull(analyzedQuestion.getQuestionArg());
        assertEquals(new NerdleArg("", "", "A1", ""), analyzedQuestion.getQuestionArg());
    }

    @Test
    public void testNoQuestionArg() throws Exception {
        Question question = new Question("Which one has devoted her life to celibacy?");

        List<NerdleFact> analyzedQuestions = questionParser.analyzeQuestion(question);

        assertEquals(0, analyzedQuestions.size());

        // NerdleFact analyzedQuestion = analyzedQuestions.get(0);

        // question argument
        // assertEquals(null, analyzedQuestion.getQuestionArg());
    }

}
