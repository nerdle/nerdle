package de.textmining.nerdle.question.answering.fact.matcher;

import de.textmining.nerdle.database.FactProvider;
import de.textmining.nerdle.question.answering.model.NerdleFact;

import java.util.List;
import java.util.Map;
import java.util.SortedSet;

/**
 * 
 * The FactMatcher takes a question interpreted as a list of @NerdleFact and
 * determines possible answers from the @FactProvider. The answers are sorted by
 * score, highest first.
 * 
 */
public interface FactMatcher {

    /**
     * Get matching answers from the @FactProvider for a question expressed as
     * 
     * @List<NerdleFact>
     */
    SortedSet<Map.Entry<String, Float>> getAnswers(FactProvider factProvider, List<NerdleFact> questionDesciption);
}
