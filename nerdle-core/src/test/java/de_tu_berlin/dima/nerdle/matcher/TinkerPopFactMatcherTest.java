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

package de_tu_berlin.dima.nerdle.matcher;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.tinkerpop.blueprints.impls.tg.TinkerGraph;

import de_tu_berlin.dima.nerdle.model.NerdleArg;
import de_tu_berlin.dima.nerdle.model.NerdleFact;
import de_tu_berlin.dima.nerdle.model.NerdlePredicate;
import de_tu_berlin.dima.nerdle.provider.TinkerpopFactProvider;
import de_tu_berlin.dima.nerdle.tinkerpop.TinkerpopTranformer;

public class TinkerPopFactMatcherTest {

    private static TinkerpopFactProvider tinkerpopFactProvider;
    private static TinkerpopFactMatcher tinkerpopFactmatcher;

    @BeforeClass
    public static void setup() {
        TinkerGraph graph = new TinkerGraph();

        /* Add extractions to graph */

        // exact answer
        List<NerdleArg> nerdleArgs = new ArrayList<NerdleArg>();
        nerdleArgs.add(new NerdleArg("in Ulm", "IN NNP", "AM-LOC", "prep"));
        nerdleArgs.add(new NerdleArg("on March 14th", "NN NNP NN", "AM-TMP", "prep"));
        nerdleArgs.add(new NerdleArg("Albert Einstein", "NNP NNP", "A1", "nsubjpass"));

        String sentence = "Albert Einstein was born on March 14th in Ulm.";
        String source = "http://www.einstein.de/info";

        NerdleFact nerdleFact = new NerdleFact(sentence, source, new NerdlePredicate("born", "bear", "bear.02"), nerdleArgs);

        TinkerpopTranformer.transform(nerdleFact, graph);

        // one argument missing
        nerdleArgs = new ArrayList<NerdleArg>();
        nerdleArgs.add(new NerdleArg("in Ulm", "IN NNP", "AM-LOC", "prep"));
        nerdleArgs.add(new NerdleArg("Albert Einstein", "NNP NNP", "A1", "nsubjpass"));

        nerdleFact = new NerdleFact(sentence, source, new NerdlePredicate("born", "bear", "bear.02"), nerdleArgs);

        TinkerpopTranformer.transform(nerdleFact, graph);

        // with label A0
        source = "http://simpsons.wikia.com/wiki/Homer_Simpson";
        sentence = "Homer spends a great deal of his time at Moe's Tavern.";

        nerdleArgs = new ArrayList<NerdleArg>();
        nerdleArgs.add(new NerdleArg("Homer", "NNP", "A0", "nsubj"));
        nerdleArgs.add(new NerdleArg("at Moe 's Tavern", "IN NNP POS NNP", "AM-LOC", "prep"));
        nerdleArgs.add(new NerdleArg("a great deal of his time", "DT JJ NN IN PRP$ NN", "A1", "dobj"));

        nerdleFact = new NerdleFact(sentence, source, new NerdlePredicate("spends", "spend", "spend.02"), nerdleArgs);

        TinkerpopTranformer.transform(nerdleFact, graph);

        tinkerpopFactProvider = new TinkerpopFactProvider(graph);
        tinkerpopFactmatcher = new TinkerpopFactMatcher();
    }

    @Test
    public void testExactMatching() {
        List<NerdleArg> nerdleArgs = new ArrayList<NerdleArg>();
        nerdleArgs.add(new NerdleArg("in Ulm", "IN NNP", "AM-LOC", "prep"));
        nerdleArgs.add(new NerdleArg("on March 14th", "NN NNP NN", "AM-TMP", "prep"));
        nerdleArgs.add(new NerdleArg("Albert Einstein", "NNP NNP", "A1", "nsubjpass"));

        String sentence = "Peter was born on March 14th in Ulm.";
        String source = "http://www.einstein.de/info";

        NerdleFact questionFact = new NerdleFact(sentence, source, new NerdlePredicate("born", "bear", "bear.02"), nerdleArgs);

        List<NerdleFact> matchedFacts = tinkerpopFactmatcher.match(tinkerpopFactProvider, questionFact, nerdleArgs.get(2));

        assertEquals(2, matchedFacts.size());
    }

    @Test
    public void testPartialArguments() {
        List<NerdleArg> nerdleArgs = new ArrayList<NerdleArg>();
        nerdleArgs.add(new NerdleArg("in Ulm", "IN NNP", "AM-LOC", "prep"));
        nerdleArgs.add(new NerdleArg("Albert Einstein", "NNP NNP", "A1", "nsubjpass"));

        String sentence = "Peter was born on March 14th in Ulm.";
        String source = "http://www.einstein.de/info";

        NerdleFact questionFact = new NerdleFact(sentence, source, new NerdlePredicate("born", "bear", "bear.02"), nerdleArgs);

        List<NerdleFact> matchedFacts = tinkerpopFactmatcher.match(tinkerpopFactProvider, questionFact, nerdleArgs.get(1));

        assertEquals(2, matchedFacts.size());
    }

    @Test
    public void testSubject() {
        List<NerdleArg> nerdleArgs = new ArrayList<NerdleArg>();
        nerdleArgs.add(new NerdleArg("Peter", "NNP", "A0", "nsubj"));
        nerdleArgs.add(new NerdleArg("at Moe 's Tavern", "IN NNP POS NNP", "AM-LOC", "prep"));
        nerdleArgs.add(new NerdleArg("a great deal of his time", "DT JJ NN IN PRP$ NN", "A1", "dobj"));

        String source = "http://simpsons.wikia.com/wiki/Homer_Simpson";
        String sentence = "Homer spends a great deal of his time at Moe's Tavern.";

        NerdleFact questionFact = new NerdleFact(sentence, source, new NerdlePredicate("spends", "spend", "spend.02"), nerdleArgs);

        List<NerdleFact> matchedFacts = tinkerpopFactmatcher.match(tinkerpopFactProvider, questionFact, nerdleArgs.get(0));

        assertEquals(1, matchedFacts.size());
    }

    @Test
    public void testExplore() {
    }

}
