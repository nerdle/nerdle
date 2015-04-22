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

import java.util.ArrayList;

import org.h2.mvstore.MVMap;
import org.h2.mvstore.MVStore;

import de.textmining.nerdle.database.MVConnection;
import de.textmining.nerdle.question.answering.model.NerdleArg;
import de.textmining.nerdle.question.answering.model.NerdleFact;
import de.textmining.nerdle.question.answering.model.NerdlePredicate;

public class TestMVConnection {

    private static MVConnection small = null;

    public synchronized static MVConnection small() {

        if (small == null) {

            MVStore mvStore = new MVStore.Builder().fileName(null).open();
            MVMap<String, ArrayList<NerdleFact>> mvMap = mvStore.openMap("data");
            
            NerdleFact nerdleFact1 = new NerdleFact("sentence", "source");
            nerdleFact1.setPredicate(new NerdlePredicate("born", "bear", "bear.02"));
            nerdleFact1.addArgument(new NerdleArg("Homer", "NNP", "A1", "nsubjpass"));
            nerdleFact1.addArgument(new NerdleArg("in Springfield", "in NNP", "AM-LOC", "prep"));

            NerdleFact nerdleFact2 = new NerdleFact("sentence", "source");
            nerdleFact2.setPredicate(new NerdlePredicate("is", "be", "be.01"));
            nerdleFact2.addArgument(new NerdleArg("Homer", "NNP", "A1", "nsubj"));
            nerdleFact2.addArgument(new NerdleArg("cool", "NNP", "A2", "acomp"));
            
            NerdleFact nerdleFact3 = new NerdleFact("sentence", "source");
            nerdleFact3.setPredicate(new NerdlePredicate("belchs", "belch", "belch.01"));
            nerdleFact3.addArgument(new NerdleArg("Homer", "NNP", "A0", "nn"));
            nerdleFact3.addArgument(new NerdleArg("at home", "NNP", "AM-LOC", "prep"));
            
            ArrayList<NerdleFact> list1 = new ArrayList<NerdleFact>();
            list1.add(nerdleFact1);

            ArrayList<NerdleFact> list2 = new ArrayList<NerdleFact>();
            list2.add(nerdleFact2);
            
            ArrayList<NerdleFact> list3 = new ArrayList<NerdleFact>();
            list3.add(nerdleFact3);
            
            mvMap.put("bear.02", list1);
            mvMap.put("be.01", list2);
            mvMap.put("belch.01", list3);
            
            small = new MVConnection(mvStore);

        }

        return small;
    }

}