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

package de_tu_berlin.dima.nerdle.qa;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.junit.BeforeClass;
import org.junit.Test;

import com.tinkerpop.blueprints.impls.tg.TinkerGraph;

import de_tu_berlin.dima.nerdle.matcher.FactMatcher;
import de_tu_berlin.dima.nerdle.matcher.NerdleFactMatcher;
import de_tu_berlin.dima.nerdle.matcher.TinkerpopFactMatcher;
import de_tu_berlin.dima.nerdle.model.NerdleArg;
import de_tu_berlin.dima.nerdle.model.NerdleFact;
import de_tu_berlin.dima.nerdle.model.NerdlePredicate;
import de_tu_berlin.dima.nerdle.provider.FactProvider;
import de_tu_berlin.dima.nerdle.provider.TinkerpopFactProvider;
import de_tu_berlin.dima.nerdle.qa.QuestionAnswer;
import de_tu_berlin.dima.nerdle.qa.QuestionAnswerResponse;
import de_tu_berlin.dima.nerdle.tinkerpop.TinkerpopTranformer;

public class QuestionAnswerTest {

    private static FactProvider factProvider;
    private static QuestionAnswer answerQuestion;

    @BeforeClass
    public static void setup() {

        TinkerGraph graph = new TinkerGraph();

        // Albert Einstein was born on March 14th in Ulm.
        List<NerdleArg> nerdleArgs = new ArrayList<NerdleArg>();
        nerdleArgs.add(new NerdleArg("in Ulm", "IN NNP", "AM-LOC", "prep"));
        nerdleArgs.add(new NerdleArg("on March 14th", "NN NNP NN", "AM-TMP", "prep"));
        nerdleArgs.add(new NerdleArg("Albert Einstein", "NNP NNP", "A1", "nsubjpass"));

        String sentence = "Albert Einstein was born on March 14th in Ulm.";
        String source = "http://www.einstein.de/info";

        NerdleFact nerdleFact = new NerdleFact(sentence, source, new NerdlePredicate("born", "bear", "bear.02"), nerdleArgs);

        TinkerpopTranformer.transform(nerdleFact, graph);

        // Albert Einstein was born in Ulm.
        nerdleArgs = new ArrayList<NerdleArg>();
        nerdleArgs.add(new NerdleArg("in Ulm", "IN NNP", "AM-LOC", "prep"));
        nerdleArgs.add(new NerdleArg("Albert Einstein", "NNP NNP", "A1", "nsubjpass"));

        sentence = "Albert Einstein was born in Ulm.";

        nerdleFact = new NerdleFact(sentence, source, new NerdlePredicate("born", "bear", "bear.02"), nerdleArgs);

        TinkerpopTranformer.transform(nerdleFact, graph);

        // Albert Einstein is a professor.
        nerdleArgs = new ArrayList<NerdleArg>();
        nerdleArgs.add(new NerdleArg("professor", "NN", "A2", "attr"));
        nerdleArgs.add(new NerdleArg("Albert Einstein", "NNP NNP", "A1", "nsubjpass"));

        sentence = "Albert Einstein is a professor.";

        nerdleFact = new NerdleFact(sentence, source, new NerdlePredicate("is", "be", "be.01"), nerdleArgs);

        TinkerpopTranformer.transform(nerdleFact, graph);

        // Homer spends a great deal of his time at Moe's Tavern.
        nerdleArgs = new ArrayList<NerdleArg>();
        nerdleArgs.add(new NerdleArg("Homer", "NNP", "A0", "nsubj"));
        nerdleArgs.add(new NerdleArg("at Moe 's Tavern", "IN NNP POS NNP", "AM-LOC", "prep"));
        nerdleArgs.add(new NerdleArg("a great deal of his time", "DT JJ NN IN PRP$ NN", "A1", "dobj"));

        source = "http://simpsons.wikia.com/wiki/Homer_Simpson";
        sentence = "Homer spends a great deal of his time at Moe's Tavern.";

        nerdleFact = new NerdleFact(sentence, source, new NerdlePredicate("spends", "spend", "spend.02"), nerdleArgs);

        TinkerpopTranformer.transform(nerdleFact, graph);

        factProvider = new TinkerpopFactProvider(graph);

        FactMatcher factMatcher = new TinkerpopFactMatcher();
        NerdleFactMatcher nerdleFactMatcher = new NerdleFactMatcher(factMatcher);
        answerQuestion = new QuestionAnswer(nerdleFactMatcher);

    }

    @Test
    public void answerToWho() throws InterruptedException, ConfigurationException {

        QuestionAnswerResponse questionAnswerResponse;

        questionAnswerResponse = answerQuestion.answerToGeneric(factProvider, "Who was born in Ulm.");

        assertEquals(2, questionAnswerResponse.getAnswerToScore().size());

        questionAnswerResponse = answerQuestion.answerToGeneric(factProvider, "Who was born on March 14th.");

        assertEquals(1, questionAnswerResponse.getAnswerToScore().size());

        questionAnswerResponse = answerQuestion.answerToGeneric(factProvider, "Who was born on March 14th in Berlin.");

        assertEquals(1, questionAnswerResponse.getAnswerToScore().size());

        questionAnswerResponse = answerQuestion.answerToGeneric(factProvider, "Who spends a great deal of his time.");

        assertEquals(1, questionAnswerResponse.getAnswerToScore().size());

    }

    @Test
    public void answerToWhich() throws InterruptedException, ConfigurationException {

        QuestionAnswerResponse questionAnswerResponse = answerQuestion.answerToWhich(factProvider, "Which professor was born on March 14th in Ulm.");

        assertEquals(3, questionAnswerResponse.getAnswerToScore().size());

    }

    @Test
    public void answerToWhere() throws InterruptedException, ConfigurationException {

        QuestionAnswerResponse questionAnswerResponse;

        questionAnswerResponse = answerQuestion.answerToGeneric(factProvider, "Where was Albert Einstein born?");

        assertEquals(2, questionAnswerResponse.getAnswerToScore().size());

        questionAnswerResponse = answerQuestion.answerToGeneric(factProvider, "Where was Albert Einstein born on March 14th?");

        assertEquals(2, questionAnswerResponse.getAnswerToScore().size());
    }

    @Test
    public void answerToWhen() throws InterruptedException, ConfigurationException {

        QuestionAnswerResponse questionAnswerResponse;

        questionAnswerResponse = answerQuestion.answerToGeneric(factProvider, "When was Albert Einstein born?");

        assertEquals(1, questionAnswerResponse.getAnswerToScore().size());

        questionAnswerResponse = answerQuestion.answerToGeneric(factProvider, "When was Albert Einstein born in Ulm?");

        assertEquals(0, questionAnswerResponse.getAnswerToScore().size());

        questionAnswerResponse = answerQuestion.answerToGeneric(factProvider, "When was Albert Einstein born in Berlin?");

        assertEquals(0, questionAnswerResponse.getAnswerToScore().size());
    }

}
