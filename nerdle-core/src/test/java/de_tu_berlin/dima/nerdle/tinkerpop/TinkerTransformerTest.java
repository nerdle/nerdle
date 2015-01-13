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

package de_tu_berlin.dima.nerdle.tinkerpop;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.BeforeClass;
import org.junit.Test;

import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.tg.TinkerGraph;

import de_tu_berlin.dima.nerdle.model.NerdleArg;
import de_tu_berlin.dima.nerdle.model.NerdleFact;
import de_tu_berlin.dima.nerdle.model.NerdlePredicate;

public class TinkerTransformerTest {

    private static TinkerGraph graph;
    private static NerdleFact fact;

    @BeforeClass
    public static void setup() throws Exception {
        List<NerdleArg> args = new ArrayList<NerdleArg>();
        args.add(new NerdleArg("in Ulm", "IN NNP", "AM-LOC", "prep"));
        args.add(new NerdleArg("on March 14th", "NN NNP NN", "AM-TMP", "prep"));
        args.add(new NerdleArg("Albert Einstein", "NNP NNP", "A1", "nsubjpass"));

        String sentence = "Albert Einstein was born on March 14th in Ulm.";
        String source = "http://www.einstein.de/info";

        fact = new NerdleFact(sentence, source, new NerdlePredicate("born", "bear", "bear.02"), args);

        graph = new TinkerGraph();

        graph.createKeyIndex(TinkerpopTranformer.PROPERTY_ROLE, Vertex.class);

        TinkerpopTranformer.transform(fact, graph);
    }

    @Test
    public void test() throws Exception {
        Iterable<Vertex> vertices = graph.getVertices(TinkerpopTranformer.PROPERTY_CLAUSE_TYPE, TinkerpopTranformer.VALUE_CLAUSE_TYPE_PREDICATE);

        Vertex vertex = vertices.iterator().next();

        NerdleFact transformedFact = TinkerpopTranformer.transform(vertex);

        Assert.assertEquals(fact.getPredicate(), transformedFact.getPredicate());
        Assert.assertEquals(fact.getSentence(), transformedFact.getSentence());
        Assert.assertEquals(fact.getSource(), transformedFact.getSource());

        for (NerdleArg nerdleArg : fact.getArguments()) {
            Assert.assertTrue(transformedFact.getArguments().contains(nerdleArg));
        }
    }
}
