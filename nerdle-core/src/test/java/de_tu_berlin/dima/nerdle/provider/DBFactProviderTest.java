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

import de.textmining.nerdle.database.DBFactProvider;
import de.textmining.nerdle.database.DBSingleton;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import de.textmining.nerdle.question.answering.model.NerdleArg;
import de.textmining.nerdle.question.answering.model.NerdleFact;
import de.textmining.nerdle.question.answering.model.NerdlePredicate;
import de.textmining.nerdle.utils.ResourceManager;

public class DBFactProviderTest {

    private static DBFactProvider dbFactProvider;

    @BeforeClass
    public static void init() {
     //   System.out.println("dbFactProvider = " + dbFactProvider);
        DBSingleton dbSingleton = new DBSingleton(ResourceManager.getResourcePath(File.separator + "nerdle_test_config.properties"));
        dbFactProvider = new DBFactProvider(dbSingleton.getConnections().get("postgres"));
        System.out.println("dbFactProvider = " + dbFactProvider);
    }

    @Test
    @Ignore
    public void testFactsByPredicate() {
        List<NerdleFact> factsByPredicate = dbFactProvider.getFactsByPredicate(new NerdlePredicate("is", "be", "buy.01"));
        int i = 0;
        for (NerdleFact nerdleFact : factsByPredicate) {
            System.out.println("nerdleFact = " + nerdleFact);
            if (i++ > 5) break;
        }
      //  assertEquals(2, factsByPredicate.size());
    }

    @Test
    @Ignore
    public void testFactsFactsByMatch() {
        NerdleFact questionFact = new NerdleFact("", "");
        questionFact.setPredicate(new NerdlePredicate("is", "be", "be.01"));
        questionFact.addArgument(new NerdleArg("funny", "JJ", "A2", "acomp"));

        List<NerdleFact> factsByMatch = dbFactProvider.getFactsByMatch(questionFact, new NerdleArg("", "", "A1", ""));
        int i = 0;
        for (NerdleFact nerdleFact : factsByMatch) {
            System.out.println("nerdleFact = " + nerdleFact);
            if (i++ > 5) break;
        }
    }

}
