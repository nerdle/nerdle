package de.textmining.nerdle.question.answering;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

import de.textmining.nerdle.TestDBConnection;
import de.textmining.nerdle.database.DBFactProvider;
import de.textmining.nerdle.question.answering.fact.matcher.ExactQuestionFactMatcher;
import de.textmining.nerdle.question.answering.model.Answer;
import de.textmining.nerdle.question.answering.model.Question;
import de.textmining.nerdle.question.answering.question.parsing.ClearNLPQuestionParser;

public class MatchFactQuestionAnswererTest {

    private static QuestionAnswerer questionAnswerer;

    @BeforeClass
    public static void setup() throws Exception {
        ClearNLPQuestionParser questionParser = new ClearNLPQuestionParser();
        ExactQuestionFactMatcher questionFactMatcher = new ExactQuestionFactMatcher();
        DBFactProvider factProvider = new DBFactProvider(TestDBConnection.small());

        questionAnswerer = new MatchFactQuestionAnswerer(questionParser, questionFactMatcher, factProvider);
    }

    @Test
    public void testWho() {
        Question question = new Question("Who was born in Springfield?");
        Answer answer = questionAnswerer.answer(question);
        assertEquals(0, answer.getAnswers().size());

        question = new Question("Who is cool?");
        answer = questionAnswerer.answer(question);
        assertEquals(1, answer.getAnswers().size());

        assertEquals("Homer", answer.getAnswers().get(0));
    }

    @Test
    public void testNoQuestionArg() {
        Question question = new Question("Which one has devoted her life to celibacy?");
        Answer answer = questionAnswerer.answer(question);

        assertEquals(0, answer.getAnswers().size());
    }
}
