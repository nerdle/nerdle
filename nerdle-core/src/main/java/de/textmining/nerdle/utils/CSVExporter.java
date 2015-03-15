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
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import au.com.bytecode.opencsv.CSVWriter;

import com.google.gson.Gson;

import de.textmining.nerdle.question.answering.model.NerdleArg;
import de.textmining.nerdle.question.answering.model.NerdleFact;
import de.textmining.nerdle.question.answering.model.NerdlePredicate;

public class CSVExporter {

    static String FILTER = "^[a-zA-Z0-9äöüÄÖÜ.,!?:;\\-' ]*$";
    static String PRONOUN_REGEX = "(all|another|any|anybody|anyone|anything|both|each|each|other|either|everybody|everyone|everything|few|he|her|hers|herself|him|himself|his|i|it|its|itself|many|me|mine|more|most|much|myself|neither|no|one|nobody|none|nothing|one|one|another|other|others|ours|ourselves|several|she|some|somebody|someone|something|that|their|theirs|them|themselves|these|they|this|those|us|we|what|whatever|which|whichever|who|whoever|whom|whomever|whose|you|your|yours|yourself|yourselves)";

    static int MAX_ARGUMENT_LENGTH = 30;
    static int MIN_ARGUMENT_LENGTH = 2;
    static int MAX_SENTENCE_LENGTH = 1000;

    public static void main(String[] args) throws Exception {

        if (args.length != 3) {
            System.err.println("usage: input_dir csv_dir topic.");
            System.err.println("input_dir: directory of the input json files.");
            System.err.println("csv_dir: directory of the the csv files.");
            System.err.println("topic: name the topic.");
            return;
        }

        File inputDir = new File(args[0]);
        if (!inputDir.isDirectory()) {
            System.err.println(inputDir);
            System.err.println("Directory does not exist.");
            return;
        }

        File csvDir = new File(args[1]);
        String topicName = args[2];

        File topicDir = new File(csvDir.getPath() + File.separator + topicName + File.separator);

        if (topicDir.exists()) {
            System.err.println(inputDir);
            System.err.println("Topic already exists.");
            return;
        } else {
            topicDir.mkdirs();
        }

        File[] files = inputDir.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.toLowerCase().startsWith("part-");
            }
        });

        CSVWriter factswriter = new CSVWriter(new FileWriter(topicDir.getPath() + File.separator + "facts.csv"), '\t', CSVWriter.NO_QUOTE_CHARACTER,
                CSVWriter.NO_ESCAPE_CHARACTER);

        CSVWriter argumentswriter = new CSVWriter(new FileWriter(topicDir.getPath() + File.separator + "arguments.csv"), '\t', CSVWriter.NO_QUOTE_CHARACTER,
                CSVWriter.NO_ESCAPE_CHARACTER);

        Gson gson = new Gson();

        BufferedReader reader;
        int index = 0;

        int factIndex = 1;
        int argumentIndex = 1;

        for (File file : files) {

            System.out.println(file.getName());

            reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {

                // if (factIndex == 10) {
                // System.out.println("STOP");
                // break;
                // }

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
                        writeEntriesCSV(factswriter, factIndex, argumentswriter, argumentIndex, fact);
                        factIndex++;
                        argumentIndex += fact.getArguments().size();
                    }

                }
                index++;
            }
        }

        factswriter.close();
        argumentswriter.close();

    }

    private static void writeEntriesCSV(CSVWriter factswriter, int factIndex, CSVWriter argumentswriter, int argumentIndex, NerdleFact fact) {
        factswriter.writeNext(factEntries(fact, factIndex));
        int index = 0;
        for (NerdleArg arg : fact.getArguments()) {
            argumentswriter.writeNext(argumentEntries(arg, factIndex, argumentIndex + index));
            index++;
        }
    }

    private static String[] factEntries(NerdleFact fact, int factIndex) {

        String[] arr = new String[6];
        arr[0] = "" + factIndex;
        arr[1] = fact.getSource();
        arr[2] = fact.getSentence();
        arr[3] = fact.getPredicate().getText();
        arr[4] = fact.getPredicate().getLemma();
        arr[5] = fact.getPredicate().getRolesetID();

        return arr;
    }

    private static String[] argumentEntries(NerdleArg arg, int factIndex, int argumentIndex) {

        String[] arr = new String[6];
        arr[0] = "" + argumentIndex;
        arr[1] = arg.getText();
        arr[2] = arg.getPos();
        arr[3] = arg.getArgLabel();
        arr[4] = arg.getDepLabel();
        arr[5] = "" + factIndex;

        return arr;
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