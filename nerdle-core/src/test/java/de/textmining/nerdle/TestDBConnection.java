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

public class TestDBConnection {

    private static DBConnection small = null;

    public synchronized static DBConnection small() {

        if (small == null) {
            H2Store h2Store = new H2Store("jdbc:h2:mem:small");

            h2Store.addFact(1, "source", "sentence", "born", "bear", "bear.02");

            h2Store.addArgument(1, "Homer", "NNP", "A1", "nsubjpass", 1);
            h2Store.addArgument(2, "Sprinflied", "NNP", "AM-LOC", "pobj", 1);

            h2Store.addFact(2, "source", "sentence", "is", "be", "be.01");

            h2Store.addArgument(3, "Homer", "NNP", "A1", "nsubj", 2);
            h2Store.addArgument(4, "cool", "NNP", "A2", "acomp", 2);

            h2Store.persist();
            h2Store.createIndex();

            small = new DBConnection(h2Store.getConn());
        }

        return small;
    }

}