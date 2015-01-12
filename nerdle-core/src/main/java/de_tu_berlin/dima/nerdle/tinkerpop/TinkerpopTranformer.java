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
import java.util.Iterator;
import java.util.List;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;

import de_tu_berlin.dima.nerdle.model.NerdleArg;
import de_tu_berlin.dima.nerdle.model.NerdleFact;
import de_tu_berlin.dima.nerdle.model.NerdlePredicate;

public class TinkerpopTranformer {

    public final static String PROPERTY_CLAUSE_TYPE = "clauseType";

    public final static String VALUE_CLAUSE_TYPE_PREDICATE = "P";
    public final static String VALUE_CLAUSE_TYPE_ARGUMENT = "A";

    // NerdleFact
    public final static String PROPERTY_SOURCE = "source";
    public final static String PROPERTY_SENTENCE = "sentence";

    // NerdlePredicate
    public final static String PROPERTY_TEXT = "text";
    public final static String PROPERTY_LEMMA = "lemma";
    public final static String PROPERTY_ROLE = "role";

    // NerldeArgument
    public final static String PROPERTY_ARG_LABEL = "arglabel";
    public final static String PROPERTY_DEP_LABEL = "deplabel";
    public final static String PROPERTY_POS = "pos";

    public static void transform(NerdleFact fact, Graph graph) {
        Vertex predicate = createPredicateVertex(fact.getPredicate(), fact.getSentence(), fact.getSource(), graph);

        for (NerdleArg nerdleArg : fact.getArguments()) {
            Vertex argument = createArgumentVertex(nerdleArg, graph);
            graph.addEdge(null, predicate, argument, "hasArgument");
        }
    }

    private static Vertex createArgumentVertex(NerdleArg nerdleArg, Graph graph) {

        Vertex vertex = graph.addVertex(null);
        vertex.setProperty(PROPERTY_CLAUSE_TYPE, VALUE_CLAUSE_TYPE_ARGUMENT);
        vertex.setProperty(PROPERTY_TEXT, nerdleArg.getText());
        vertex.setProperty(PROPERTY_ARG_LABEL, nerdleArg.getArgLabel());
        vertex.setProperty(PROPERTY_DEP_LABEL, nerdleArg.getDepLabel());
        vertex.setProperty(PROPERTY_POS, nerdleArg.getPos());

        return vertex;
    }

    private static Vertex createPredicateVertex(NerdlePredicate nerdlePredicate, String sentence, String source, Graph graph) {

        Vertex vertex = graph.addVertex(null);

        vertex.setProperty(PROPERTY_CLAUSE_TYPE, VALUE_CLAUSE_TYPE_PREDICATE);
        vertex.setProperty(PROPERTY_SENTENCE, sentence);
        vertex.setProperty(PROPERTY_SOURCE, source);

        vertex.setProperty(PROPERTY_TEXT, nerdlePredicate.getText());
        vertex.setProperty(PROPERTY_LEMMA, nerdlePredicate.getLemma());
        vertex.setProperty(PROPERTY_ROLE, nerdlePredicate.getRolesetID());

        return vertex;
    }

    public static NerdleFact transform(Vertex predicateVertex) {

        NerdlePredicate predicate = new NerdlePredicate(predicateVertex.getProperty(PROPERTY_TEXT).toString(), predicateVertex.getProperty(PROPERTY_LEMMA)
                .toString(), predicateVertex.getProperty(PROPERTY_ROLE).toString());

        List<NerdleArg> args = new ArrayList<NerdleArg>();
        Iterator<Vertex> argIt = predicateVertex.getVertices(Direction.OUT, "hasArgument").iterator();

        while (argIt.hasNext()) {
            Vertex vertex = (Vertex) argIt.next();
            args.add(new NerdleArg((String) vertex.getProperty(PROPERTY_TEXT), (String) vertex.getProperty(PROPERTY_POS), (String) vertex
                    .getProperty(PROPERTY_ARG_LABEL), (String) vertex.getProperty(PROPERTY_DEP_LABEL)));
        }

        return new NerdleFact((String) predicateVertex.getProperty(PROPERTY_SENTENCE), (String) predicateVertex.getProperty(PROPERTY_SOURCE), predicate, args);
    }

}
