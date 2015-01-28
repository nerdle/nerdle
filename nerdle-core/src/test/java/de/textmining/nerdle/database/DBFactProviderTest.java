package de.textmining.nerdle.database;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import de.textmining.nerdle.TestDBConnection;
import de.textmining.nerdle.question.answering.model.NerdleArg;
import de.textmining.nerdle.question.answering.model.NerdleFact;
import de.textmining.nerdle.question.answering.model.NerdlePredicate;

public class DBFactProviderTest {

    private static DBFactProvider factProvider;

    @BeforeClass
    public static void setup() throws Exception {
        factProvider = new DBFactProvider(TestDBConnection.small());
    }

    @Test
    public void test() throws Exception {
        List<NerdleFact> factsByPredicate = factProvider.getFactsByPredicate(new NerdlePredicate("born", "bear", "bear.02"));
        assertEquals(1, factsByPredicate.size());

        NerdleFact questionFact = new NerdleFact("sentence", "source");
        questionFact.setPredicate(new NerdlePredicate("born", "bear", "bear.02"));
        questionFact.addArgument(new NerdleArg("Sprinflied", "NNP", "AM-LOC", "pobj"));

        List<NerdleFact> factsByMatch = factProvider.getFactsByMatch(questionFact, new NerdleArg("", "", "A1", "nsubjpass"));
        assertTrue(factsByMatch.size() > 0);
    }

}
