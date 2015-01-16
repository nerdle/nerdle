package de.textmining.nerdle.question.answering.question.parsing;

import de.textmining.nerdle.question.answering.model.Question;
import de.textmining.nerdle.question.answering.model.NerdleFact;
import org.apache.commons.configuration.ConfigurationException;

import java.util.List;

/**
 * Created by alan on 1/16/15.
 */
public interface QuestionParser {
    List<NerdleFact> analyzeQuestion(Question question) throws InterruptedException, ConfigurationException;
}
