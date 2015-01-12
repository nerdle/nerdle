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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.junit.BeforeClass;
import org.junit.Test;

import com.tinkerpop.blueprints.impls.tg.TinkerGraph;

import de_tu_berlin.dima.nerdle.matcher.DBFactMatcher;
import de_tu_berlin.dima.nerdle.matcher.FactMatcher;
import de_tu_berlin.dima.nerdle.matcher.NerdleFactMatcher;
import de_tu_berlin.dima.nerdle.matcher.TinkerpopFactMatcher;
import de_tu_berlin.dima.nerdle.model.NerdleArg;
import de_tu_berlin.dima.nerdle.model.NerdleFact;
import de_tu_berlin.dima.nerdle.model.NerdlePredicate;
import de_tu_berlin.dima.nerdle.provider.DBFactProvider;
import de_tu_berlin.dima.nerdle.provider.DBSingleton;
import de_tu_berlin.dima.nerdle.provider.TinkerpopFactProvider;
import de_tu_berlin.dima.nerdle.tinkerpop.TinkerpopTranformer;
import de_tu_berlin.dima.nerdle.util.ResourceManager;

public class NerdleAnswererTest {

    private static TinkerpopFactProvider tinkerpopFactProvider;
    private static QuestionAnswer tinkerpopAnswerQuestion;
    private static NerdleAnswerer tinkerpopNerdleAnswerer;

    private static DBFactProvider dbFactProvider;
    private static QuestionAnswer dbAnswerQuestion;
    private static NerdleAnswerer dbNerdleAnswerer;

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

        // TINKERPOP
        tinkerpopFactProvider = new TinkerpopFactProvider(graph);

        FactMatcher tinkerpopFactMatcher = new TinkerpopFactMatcher();
        NerdleFactMatcher tinkerpopNerdleFactMatcher = new NerdleFactMatcher(tinkerpopFactMatcher);
        tinkerpopAnswerQuestion = new QuestionAnswer(tinkerpopNerdleFactMatcher);

        tinkerpopNerdleAnswerer = new NerdleAnswerer(tinkerpopAnswerQuestion);

        // DB
        DBSingleton dbSingleton = new DBSingleton(ResourceManager.getResourcePath(File.separator + "nerdle_test_config.properties"));
        dbFactProvider = new DBFactProvider(dbSingleton.getConnections().get("nerdle_test"));
        
        FactMatcher dbFactMatcher = new DBFactMatcher();
        NerdleFactMatcher dbNerdleFactMatcher = new NerdleFactMatcher(dbFactMatcher);
        dbAnswerQuestion = new QuestionAnswer(dbNerdleFactMatcher);

        dbNerdleAnswerer = new NerdleAnswerer(dbAnswerQuestion);
    }

    @Test
    public void answerToWho() throws InterruptedException, ConfigurationException {
        // TINKERPOP
        List<GroupedAnswer> answers;

        answers = tinkerpopNerdleAnswerer.getAnswers(tinkerpopFactProvider, "Who was born in Ulm?", 1);
        assertEquals(1, answers.size());
        assertEquals("Albert Einstein", answers.get(0).getSubject());

        answers = tinkerpopNerdleAnswerer.getAnswers(tinkerpopFactProvider, "Who was born on March 14th?", 1);
        assertEquals(1, answers.size());
        assertEquals("Albert Einstein", answers.get(0).getSubject());

        answers = tinkerpopNerdleAnswerer.getAnswers(tinkerpopFactProvider, "Who was born on March 14th in Berlin?", 1);
        assertEquals(1, answers.size());
        assertEquals("Albert Einstein", answers.get(0).getSubject());

        answers = tinkerpopNerdleAnswerer.getAnswers(tinkerpopFactProvider, "Who spends a great deal of his time?", 1);
        assertEquals(1, answers.size());
        assertEquals("Homer", answers.get(0).getSubject());

        // DB
        answers = dbNerdleAnswerer.getAnswers(dbFactProvider, "Who is funny?", 1);
        assertEquals(1, answers.size());
        assertEquals("Bart", answers.get(0).getSubject());
    }

}
