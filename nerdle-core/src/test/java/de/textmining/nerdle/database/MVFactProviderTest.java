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

package de.textmining.nerdle.database;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import de.textmining.nerdle.TestMVConnection;
import de.textmining.nerdle.question.answering.model.NerdleArg;
import de.textmining.nerdle.question.answering.model.NerdleFact;
import de.textmining.nerdle.question.answering.model.NerdlePredicate;
import de.textmining.nerdle.question.answering.string.matcher.SetStringMatcher;

public class MVFactProviderTest {

    private static MVFactProvider factProvider;

    @BeforeClass
    public static void setup() throws Exception {
        factProvider = new MVFactProvider(TestMVConnection.small(), new SetStringMatcher());
    }

    @Test
    public void testFactsByPredicate() throws Exception {
        assertEquals(1, factProvider.getFactsByPredicate(new NerdlePredicate("born", "bear", "bear.02")).size());
        assertEquals(1, factProvider.getFactsByPredicate(new NerdlePredicate("is", "be", "be.01")).size());

        assertEquals(0, factProvider.getFactsByPredicate(new NerdlePredicate("do", "do", "do.01")).size());
    }

    @Test
    public void testFactsByMatch() throws Exception {
        NerdleFact questionFact = new NerdleFact("sentence", "source");
        questionFact.setPredicate(new NerdlePredicate("born", "bear", "bear.02"));
        questionFact.addArgument(new NerdleArg("in Springfield", "in NNP", "AM-LOC", "prep"));

        List<NerdleFact> factsByMatch = factProvider.getFactsByMatch(questionFact, new NerdleArg("", "", "A1", "nsubjpass"));
        assertTrue(factsByMatch.size() > 0);
    }

}
