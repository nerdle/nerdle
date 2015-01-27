package de.textmining.nerdle.question.answering;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.BeforeClass;
import org.junit.Test;

import de.textmining.nerdle.database.DBFactProvider;
import de.textmining.nerdle.database.DBSingleton;
import de.textmining.nerdle.question.answering.fact.matcher.ExactQuestionFactMatcher;
import de.textmining.nerdle.question.answering.model.Answer;
import de.textmining.nerdle.question.answering.model.Question;
import de.textmining.nerdle.question.answering.question.parsing.ClearNLPQuestionParser;
import de.textmining.nerdle.utils.ResourceManager;

public class MatchFactQuestionAnswererTest {

    private static QuestionAnswerer questionAnswerer;

    @BeforeClass
    public static void setup() {
        DBSingleton dbSingleton = new DBSingleton(ResourceManager.getResourcePath(File.separator + "nerdle_test_config.properties"));
        ClearNLPQuestionParser questionParser = new ClearNLPQuestionParser();
        ExactQuestionFactMatcher questionFactMatcher = new ExactQuestionFactMatcher();
        DBFactProvider factProvider = new DBFactProvider(dbSingleton.getConnections().get("nerdle_test"));

        questionAnswerer = new MatchFactQuestionAnswerer(questionParser, questionFactMatcher, factProvider);
    }

    @Test
    public void testNoQuestionArg() {
        Question question = new Question("Which one has devoted her life to celibacy?");
        Answer answer = questionAnswerer.answer(question);

        assertEquals(0, answer.getAnswers().size());
    }
}
