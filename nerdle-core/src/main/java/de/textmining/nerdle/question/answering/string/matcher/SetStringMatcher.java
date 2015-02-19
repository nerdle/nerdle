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

package de.textmining.nerdle.question.answering.string.matcher;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import de.textmining.nerdle.question.answering.model.NerdleArg;

public class SetStringMatcher implements StringMatcher {

    @Override
    public boolean match(String a, String b) {

        StringTokenizer tokenizerA = new StringTokenizer(a);

        List<String> listA = new ArrayList<>();

        while (tokenizerA.hasMoreElements()) {
            listA.add(((String) tokenizerA.nextElement()).toLowerCase().trim());
        }

        StringTokenizer tokenizerB = new StringTokenizer(b);

        List<String> listB = new ArrayList<>();

        while (tokenizerB.hasMoreElements()) {
            listB.add(((String) tokenizerB.nextElement()).toLowerCase().trim());
        }

        if (listA.containsAll(listB) || listB.containsAll(listA)) {
            return true;
        }

        return false;
    }

    @Override
    public boolean argumentAndLabelMatch(List<NerdleArg> arguments, NerdleArg argument) {

        for (NerdleArg a : arguments) {
            boolean match = match(a.getText(), argument.getText());

            boolean matchLabel = a.getArgLabel().equals(argument.getArgLabel());

            if (match && matchLabel) {
                return true;
            }
        }
        return false;
    }

}
