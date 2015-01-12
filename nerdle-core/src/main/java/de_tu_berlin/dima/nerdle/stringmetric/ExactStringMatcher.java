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

package de_tu_berlin.dima.nerdle.stringmetric;

import java.util.List;

import de_tu_berlin.dima.nerdle.model.NerdleArg;

public class ExactStringMatcher implements StringMatcher {

    @Override
    public double distance(String a, String b) {
        // TODO Auto-generated method stub
        return 0;
    }

    public boolean match(String a, String b) {
        return ((a.toLowerCase().trim()).equals(b.toLowerCase().trim()));
    }

    @Override
    public double argumentDistance(List<NerdleArg> arguments, NerdleArg argument) {
        // TODO Auto-generated method stub
        return 0;
    }

    public boolean argumentAndLabelMatch(List<NerdleArg> arguments, NerdleArg argument) {

        for (NerdleArg a : arguments) {
            boolean match = match(a.getText(), argument.getText());

            if (match) {
                return true;
            }
        }
        return false;
    }

    public boolean exploreArgumentAndLabelMatch(List<NerdleArg> arguments, NerdleArg argument) {

        for (NerdleArg a : arguments) {

            boolean matchArgument = true;

            if (!argument.getText().isEmpty()) {
                matchArgument = match(a.getText(), argument.getText());
            }

            boolean matchLabel = true;

            if (!argument.getArgLabel().isEmpty()) {
                matchLabel = a.getArgLabel().equals(argument.getArgLabel());
            }

            if (matchArgument && matchLabel) {
                return true;
            }
        }

        return false;
    }

}
