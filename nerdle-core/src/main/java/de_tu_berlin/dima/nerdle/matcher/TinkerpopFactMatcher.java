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

package de_tu_berlin.dima.nerdle.matcher;

import java.util.List;

import de_tu_berlin.dima.nerdle.model.NerdleArg;
import de_tu_berlin.dima.nerdle.model.NerdleFact;
import de_tu_berlin.dima.nerdle.model.NerdlePredicate;
import de_tu_berlin.dima.nerdle.provider.FactProvider;
import de_tu_berlin.dima.nerdle.provider.TinkerpopFactProvider;

/**
 *
 * @author Umar Maqsud (umar.maqsud@campus.tu-berlin.de)
 *
 */

public class TinkerpopFactMatcher implements FactMatcher {

    @Override
    public List<NerdleFact> match(FactProvider factProvider, NerdleFact questionFact, NerdleArg searchArg) {

        if (!(factProvider instanceof TinkerpopFactProvider)) {
            throw new IllegalArgumentException("Please use a TinkerpopFactMatcher.");
        }

        return factProvider.getFactsByMatch(questionFact, searchArg);
    }

    @Override
    public List<NerdleFact> explore(FactProvider factProvider, NerdlePredicate questionPredicate) {

        if (!(factProvider instanceof TinkerpopFactProvider)) {
            throw new IllegalArgumentException("Please use a TinkerpopFactMatcher.");
        }

        // TODO Auto-generated method stub
        return null;
    }

}