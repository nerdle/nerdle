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
