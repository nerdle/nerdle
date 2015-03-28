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

package de.textmining.nerdle.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

import de.textmining.nerdle.question.answering.model.NerdleArg;
import de.textmining.nerdle.question.answering.model.NerdleFact;
import de.textmining.nerdle.question.answering.model.NerdlePredicate;

public class FactFilter {

    static String FILTER = "^[a-zA-Z0-9äöüÄÖÜ.,!?:;\\-' ]*$";
    static String PRONOUN_REGEX = "(all|another|any|anybody|anyone|anything|both|each|each|other|either|everybody|everyone|everything|few|he|her|hers|herself|him|himself|his|i|it|its|itself|many|me|mine|more|most|much|myself|neither|no|one|nobody|none|nothing|one|one|another|other|others|ours|ourselves|several|she|some|somebody|someone|something|that|their|theirs|them|themselves|these|they|this|those|us|we|what|whatever|which|whichever|who|whoever|whom|whomever|whose|you|your|yours|yourself|yourselves)";

    static int MAX_ARGUMENT_LENGTH = 30;
    static int MIN_ARGUMENT_LENGTH = 2;
    static int MAX_SENTENCE_LENGTH = 1000;

    public static void main(String[] args) throws Exception {

        if (args.length != 2) {
            System.err.println("usage: input_dir output_dir.");
            System.err.println("input_dir: directory of the input json files.");
            System.err.println("output_dir: directory of the output json files.");
            return;
        }

        File inputDir = new File(args[0]);

        File outputDir = new File(args[1]);

        File[] files = inputDir.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.toLowerCase().startsWith("part-");
            }
        });

        Gson gson = new Gson();

        BufferedReader reader;

        BufferedWriter writer = new BufferedWriter(new FileWriter(outputDir));

        int index = 0;

        for (File file : files) {

            System.out.println(file.getName());

            reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {

                if ((index % 1000000) == 0) {
                    System.out.println();
                }

                if ((index % 100000) == 0) {
                    System.out.print(".");
                }

                String[] split = line.split("\t");

                NerdleFact fact = gson.fromJson(split[0], NerdleFact.class);

                fact = cleanFact(fact);

                if (fact.getArguments().size() >= 2) {

                    boolean found = false;

                    for (NerdleArg nerdleArg : fact.getArguments()) {
                        if (nerdleArg.getArgLabel().matches("A[0-9]")) {
                            found = true;
                            break;
                        }
                    }

                    if (found && fact.getSentence().length() < MAX_SENTENCE_LENGTH) {
                        writer.write(gson.toJson(fact));
                        writer.newLine();
                    }

                }
                index++;
            }

            reader.close();

        }

        writer.close();

    }

    private static NerdleFact cleanFact(NerdleFact fact) {
        NerdlePredicate predicate = new NerdlePredicate(clean(fact.getPredicate().getText()), fact.getPredicate().getLemma(), fact.getPredicate()
                .getRolesetID());

        List<NerdleArg> arguments = new ArrayList<>();

        for (NerdleArg nerdleArg : fact.getArguments()) {

            String clean = clean(nerdleArg.getText());

            if (!clean.toLowerCase().matches(PRONOUN_REGEX) && clean.length() < MAX_ARGUMENT_LENGTH && clean.length() > MIN_ARGUMENT_LENGTH) {

                arguments.add(new NerdleArg(clean, nerdleArg.getPos(), nerdleArg.getArgLabel(), nerdleArg.getDepLabel()));

            }

        }

        return new NerdleFact(fact.getSentence(), fact.getSource(), predicate, arguments);
    }

    private static String clean(String s) {
        String sr = s.replaceAll("[^a-zA-Z0-9äöüÄÖÜ.,!?:;\\-' ]", "");
        return sr.replaceAll("\\s+", " ").trim();
    }
}