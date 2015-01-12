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

package de_tu_berlin.dima.nerdle.qa;

import java.util.ArrayList;
import java.util.List;

import de_tu_berlin.dima.nerdle.model.NerdleArg;
import de_tu_berlin.dima.nerdle.model.NerdleFact;
import de_tu_berlin.dima.nerdle.stringmetric.SetStringMatcher;
import de_tu_berlin.dima.nerdle.stringmetric.StringMatcher;

public class ScoreCalculator {

    private static StringMatcher stringMatcher = new SetStringMatcher();

    public static double calculate(NerdleFact questionExtraction, NerdleFact answerExtraction, NerdleArg searchArg) {

        double purity = 0.0;

        List<Double> scores = new ArrayList<Double>();

        List<NerdleArg> questionArgs = new ArrayList<>();
        questionArgs.addAll(questionExtraction.getArguments());
        questionArgs.remove(searchArg);

        for (NerdleArg nerdleArg : questionArgs) {
            scores.add(getArgumentScore(answerExtraction.getArguments(), nerdleArg));
        }

        // check for dependency labels
        if (searchArg != null) {
            boolean foundSearchArg = false;
            for (NerdleArg answerArg : answerExtraction.getArguments()) {
                if (answerArg.getArgLabel().equals(searchArg.getArgLabel()) && answerArg.getDepLabel().equals(searchArg.getDepLabel()))
                    foundSearchArg = true;
            }

            if (foundSearchArg) {
                scores.add(1.0);
            } else {
                scores.add(0.5);
            }
        }

        purity = calculatePurity(scores);

        return purity;
    }

    /* HELPER */

    private static double getArgumentScore(List<NerdleArg> arguments, NerdleArg argument) {
        return stringMatcher.argumentDistance(arguments, argument);
    }

    public static double calculatePurity(List<Double> scores) {
        double sum = 0.0;
        for (Double score : scores) {
            sum += score;
        }
        return sum / scores.size();
    }
}
