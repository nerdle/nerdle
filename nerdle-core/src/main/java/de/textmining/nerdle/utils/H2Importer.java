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

import com.google.gson.Gson;

import de.textmining.nerdle.database.H2Store;
import de.textmining.nerdle.question.answering.model.NerdleFact;

public class H2Importer {
    public static void main(String[] args) throws Exception {

        if (args.length != 3) {
            System.err.println("usage: input_dir h2store_dir mode.");
            System.err.println("input_dir: directory of the input json files.");
            System.err.println("h2store_dir: directory of the the mvstore files.");
            System.err.println("mode: full or eval.");
            return;
        }

        File inputDir = new File(args[0]);
        File h2store_dir = new File(args[1]);
        String mode = args[2];

        if (!mode.equals("full") && !mode.equals("eval")) {
            System.err.println("mode: full or eval.");
            return;
        }

        Class.forName("org.h2.Driver");
        H2Store h2Store = new H2Store("jdbc:h2:" + h2store_dir);
        h2Store.createIndex();

        Gson gson = new Gson();

        int factIndex = 1;

        BufferedReader reader = new BufferedReader(new FileReader(inputDir));
        String line;
        while ((line = reader.readLine()) != null) {

            if ((factIndex % 1000000) == 0) {
                System.out.println();
            }

            if ((factIndex % 100000) == 0) {
                System.out.print(".");
            }

            NerdleFact fact = gson.fromJson(line, NerdleFact.class);

            if (mode.equals("eval")) {
                fact.setSentence("");
                fact.setSource("");
            }

            h2Store.addFact(factIndex, fact);

            factIndex++;
        }

        reader.close();
        h2Store.persist();
        h2Store.close();
    }
}
