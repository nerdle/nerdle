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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.gremlin.java.GremlinPipeline;
import com.tinkerpop.pipes.PipeFunction;

import de_tu_berlin.dima.nerdle.model.NerdleArg;
import de_tu_berlin.dima.nerdle.model.NerdleFact;
import de_tu_berlin.dima.nerdle.model.NerdlePredicate;
import de_tu_berlin.dima.nerdle.stringmetric.SetStringMatcher;
import de_tu_berlin.dima.nerdle.stringmetric.StringMatcher;
import de_tu_berlin.dima.nerdle.tinkerpop.TinkerpopTranformer;

public class TinkerpopFactProvider implements FactProvider {

    private StringMatcher stringMatcher = new SetStringMatcher();
    private Graph graph;

    public TinkerpopFactProvider(Graph graph) {
        this.graph = graph;
    }

    @Override
    public List<NerdleFact> getFactsByPredicate(NerdlePredicate questionPredicate) {
        Iterator<Vertex> iterator = graph.getVertices(TinkerpopTranformer.PROPERTY_ROLE, questionPredicate.getRolesetID()).iterator();

        List<NerdleFact> facts = new ArrayList<NerdleFact>();

        while (iterator.hasNext()) {
            Vertex vertex = (Vertex) iterator.next();
            facts.add(TinkerpopTranformer.transform(vertex));
        }

        return facts;
    }

    @Override
    public List<NerdleFact> getFactsByMatch(NerdleFact questionFact, final NerdleArg searchArg) {
        final List<NerdleArg> questionsArgs = new ArrayList<NerdleArg>();
        questionsArgs.addAll(questionFact.getArguments());
        questionsArgs.remove(searchArg);

        final NerdlePredicate questionPredicate = questionFact.getPredicate();

        GremlinPipeline<Iterable<Vertex>, Vertex> pipeline = new GremlinPipeline<Iterable<Vertex>, Vertex>();

        pipeline.filter(new PipeFunction<Vertex, Boolean>() {

            @Override
            public Boolean compute(Vertex vertex) {

                NerdleFact answerFact = TinkerpopTranformer.transform(vertex);

                boolean foundArg = false;
                boolean foundSearchArg = false;

                final List<NerdleArg> answerArgs = answerFact.getArguments();

                for (NerdleArg answerArg : answerArgs) {

                    if (stringMatcher.argumentAndLabelMatch(questionsArgs, answerArg))
                        foundArg = true;

                    if (answerArg.getArgLabel().equals(searchArg.getArgLabel()))
                        foundSearchArg = true;

                }

                return (foundArg && foundSearchArg);

            }

        });

        pipeline.start(graph.getVertices(TinkerpopTranformer.PROPERTY_ROLE, questionPredicate.getRolesetID()));

        List<Vertex> vertices = pipeline.toList();

        List<NerdleFact> facts = new ArrayList<NerdleFact>();

        for (Vertex vertex : vertices) {
            facts.add(TinkerpopTranformer.transform(vertex));
        }

        return facts;
    }

}