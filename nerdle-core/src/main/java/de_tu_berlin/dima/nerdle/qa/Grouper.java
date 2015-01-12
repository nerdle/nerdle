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

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import de_tu_berlin.dima.nerdle.model.NerdleArg;
import de_tu_berlin.dima.nerdle.model.NerdleFact;
import de_tu_berlin.dima.nerdle.stringmetric.ExactStringMatcher;

public class Grouper {

    private static ExactStringMatcher exactStringMatcher = new ExactStringMatcher();

    /**
     * creates groups of answers
     * 
     * @param questionAnswerResponse
     * @return Map groupKey -> (groupString , List<Fact>)
     */
    public static HashMap<String, Entry<String, List<NerdleFact>>> group(QuestionAnswerResponse questionAnswerResponse) {

        HashMap<String, Entry<String, List<NerdleFact>>> groupMap = new HashMap<>();

        for (NerdleFact answer : questionAnswerResponse.getAnswerToScore().keySet()) {

            for (NerdleArg argument : answer.getArguments()) {
                if (argument.getArgLabel().equals(questionAnswerResponse.getAnswerToSearchArg().get(answer).getArgLabel())) {
                    Entry<String, String> group = createGroupingEntry(argument.getText());
                    if (!groupMap.containsKey(group.getKey())) {
                        Entry<String, List<NerdleFact>> entry = new SimpleEntry<String, List<NerdleFact>>(group.getValue(), new ArrayList<NerdleFact>());
                        groupMap.put(group.getKey(), entry);
                    }
                    groupMap.get(group.getKey()).getValue().add(answer);
                }
            }

        }

        return groupMap;
    }

    /**
     * creates a groupable key from string by removing punctuation and spaces
     */
    private static Entry<String, String> createGroupingEntry(String text) {
        String key = text.toLowerCase().trim();
        key = key.replaceFirst("^by ", "");
        key = key.replaceAll("[^A-Za-z0-9]", "");
        String val = text;
        val = val.replaceFirst("^by ", "");
        val = val.replaceAll(" ,", ",");
        val = val.replaceAll(" 's ", "'s ");

        // capitalize first letter
        val = val.substring(0, 1).toUpperCase() + val.substring(1);

        return new SimpleEntry<>(key, val);
    }

    public static boolean groupMath(NerdleFact answerA, NerdleArg searchArgA, NerdleFact answerB, NerdleArg searchArgB) {

        for (NerdleArg argumentA : answerA.getArguments()) {

            for (NerdleArg argumentB : answerB.getArguments()) {

                if (argumentA.getArgLabel().equals(searchArgA.getArgLabel()) && argumentB.getArgLabel().equals(searchArgB.getArgLabel())
                        && exactStringMatcher.match(argumentA.getText(), argumentB.getText())) {
                    return true;
                }

            }

        }

        return false;
    }

}
