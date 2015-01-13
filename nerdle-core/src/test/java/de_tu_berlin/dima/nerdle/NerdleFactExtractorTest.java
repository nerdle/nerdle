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

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import de_tu_berlin.dima.nerdle.model.NerdleFact;

public class NerdleFactExtractorTest {

    @Test
    public void test() {

        NerdleFactExtractor nerdleFactExtractor = new NerdleFactExtractor();

        String articleText = "Homer works as a low level safety inspector at the Springfield Nuclear Power Plant. "
                + "He spends a great deal of his time at Moe's Tavern.";

        String source = "http://simpsons.wikia.com/wiki/Homer_Simpson";

        List<NerdleFact> nerdleFacts = nerdleFactExtractor.process(articleText, source);

        assertEquals(2, nerdleFacts.size());

    }

}
