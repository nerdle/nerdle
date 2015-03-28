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

package de.textmining.nerdle;

import de.textmining.nerdle.database.DBConnection;
import de.textmining.nerdle.database.H2Store;
import de.textmining.nerdle.question.answering.model.NerdleArg;
import de.textmining.nerdle.question.answering.model.NerdleFact;
import de.textmining.nerdle.question.answering.model.NerdlePredicate;

public class TestDBConnection {

    private static DBConnection small = null;

    public synchronized static DBConnection small() {

        if (small == null) {
            H2Store h2Store = new H2Store("jdbc:h2:mem:small");

            NerdleFact nerdleFact1 = new NerdleFact("sentence", "source");
            nerdleFact1.setPredicate(new NerdlePredicate("born", "bear", "bear.02"));
            nerdleFact1.addArgument(new NerdleArg("Homer", "NNP", "A1", "nsubjpass"));
            nerdleFact1.addArgument(new NerdleArg("Sprinflied", "NNP", "AM-LOC", "pobj"));

            NerdleFact nerdleFact2 = new NerdleFact("sentence", "source");
            nerdleFact2.setPredicate(new NerdlePredicate("is", "be", "be.01"));
            nerdleFact2.addArgument(new NerdleArg("Homer", "NNP", "A1", "nsubj"));
            nerdleFact2.addArgument(new NerdleArg("cool", "NNP", "A2", "acomp"));
            
            h2Store.addFact(1, nerdleFact1);
            h2Store.addFact(2, nerdleFact2);
            
            h2Store.persist();
            h2Store.createIndex();

            small = new DBConnection(h2Store.getConn());
        }

        return small;
    }

}