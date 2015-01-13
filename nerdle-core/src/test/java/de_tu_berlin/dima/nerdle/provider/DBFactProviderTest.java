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

package de_tu_berlin.dima.nerdle.provider;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import de_tu_berlin.dima.nerdle.model.NerdleArg;
import de_tu_berlin.dima.nerdle.model.NerdleFact;
import de_tu_berlin.dima.nerdle.model.NerdlePredicate;
import de_tu_berlin.dima.nerdle.util.ResourceManager;

public class DBFactProviderTest {

    private static DBFactProvider dbFactProvider;

    @BeforeClass
    public static void init() {
        DBSingleton dbSingleton = new DBSingleton(ResourceManager.getResourcePath(File.separator + "nerdle_test_config.properties"));
        dbFactProvider = new DBFactProvider(dbSingleton.getConnections().get("nerdle_test"));
    }

    @Test
    @Ignore
    public void testFactsByPredicate() {
        List<NerdleFact> factsByPredicate = dbFactProvider.getFactsByPredicate(new NerdlePredicate("is", "be", "be.01"));
        assertEquals(2, factsByPredicate.size());
    }

    @Test
    @Ignore
    public void testFactsFactsByMatch() {
        NerdleFact questionFact = new NerdleFact("", "");
        questionFact.setPredicate(new NerdlePredicate("is", "be", "be.01"));
        questionFact.addArgument(new NerdleArg("funny", "JJ", "A2", "acomp"));

        List<NerdleFact> factsByMatch = dbFactProvider.getFactsByMatch(questionFact, new NerdleArg("", "", "A1", "nsubj"));
        assertEquals(1, factsByMatch.size());
    }

}
