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

package de_tu_berlin.dima.nerdle;

import java.util.ArrayList;
import java.util.List;

import de_tu_berlin.dima.nerdle.model.NerdleFact;

public class NerdleFactExtractor {

    public List<NerdleFact> process(String articleText, String source) {
        ClearNLPHelper clearNLPHelper = ClearNLPHelper.INSTANCE;

        List<NerdleFact> nerdleFacts = new ArrayList<NerdleFact>();

        nerdleFacts.addAll(clearNLPHelper.extractFactsFromArticleText(articleText, source));

        return nerdleFacts;
    }

}
