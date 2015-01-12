package de_tu_berlin.dima.nerdle.qa;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import de_tu_berlin.dima.nerdle.model.NerdleArg;
import de_tu_berlin.dima.nerdle.model.NerdleFact;
import de_tu_berlin.dima.nerdle.model.NerdlePredicate;

public class ScoreCalculatorTest {

    private static NerdleFact answerExtraction;

    @BeforeClass
    public static void setup() {

        List<NerdleArg> nerdleArgs = new ArrayList<NerdleArg>();
        nerdleArgs.add(new NerdleArg("in Ulm", "IN NNP", "AM-LOC", "prep"));
        nerdleArgs.add(new NerdleArg("on March 14th", "NN NNP NN", "AM-TMP", "prep"));
        nerdleArgs.add(new NerdleArg("Albert Einstein", "NNP NNP", "A1", "nsubjpass"));

        String sentence = "Albert Einstein was born on March 14th in Ulm.";
        String source = "http://www.einstein.de/info";

        answerExtraction = new NerdleFact(sentence, source, new NerdlePredicate("born", "bear", "bear.02"), nerdleArgs);
    }

    @Test
    public void testExactMatch() {
        List<NerdleArg> nerdleArgs = new ArrayList<NerdleArg>();
        nerdleArgs.add(new NerdleArg("in Ulm", "IN NNP", "AM-LOC", "prep"));
        nerdleArgs.add(new NerdleArg("on March 14th", "NN NNP NN", "AM-TMP", "prep"));
        NerdleArg searchArg = new NerdleArg("Peter", "NNP", "A1", "nsubjpass");
        nerdleArgs.add(searchArg);

        String sentence = "Albert Einstein was born on March 14th in Ulm.";
        String source = "http://www.einstein.de/info";

        NerdleFact questionFact = new NerdleFact(sentence, source, new NerdlePredicate("born", "bear", "bear.02"), nerdleArgs);

        double score = ScoreCalculator.calculate(questionFact, answerExtraction, searchArg);

        assertEquals(1.0, score, 0.0);
    }

    @Test
    public void testMissingArgument() {
        List<NerdleArg> nerdleArgs = new ArrayList<NerdleArg>();
        nerdleArgs.add(new NerdleArg("in Ulm", "IN NNP", "AM-LOC", "prep"));
        nerdleArgs.add(new NerdleArg("on March 14th", "NN NNP NN", "AM-TMP", "prep"));
        nerdleArgs.add(new NerdleArg("before the war", "NN NNP NN", "A2", "prep"));
        NerdleArg searchArg = new NerdleArg("Peter", "NNP", "A1", "nsubjpass");
        nerdleArgs.add(searchArg);

        String sentence = "Albert Einstein was born on March 14th in Ulm.";
        String source = "http://www.einstein.de/info";

        NerdleFact questionFact = new NerdleFact(sentence, source, new NerdlePredicate("born", "bear", "bear.02"), nerdleArgs);

        double score = ScoreCalculator.calculate(questionFact, answerExtraction, searchArg);

        assertEquals(0.75, score, 0.01);
    }

}
