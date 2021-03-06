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

package de.textmining.nerdle.evaluation;

import au.com.bytecode.opencsv.CSVReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class EvaluationSet {

    private List<EvaluationEntry> evaluationSet;

    public EvaluationSet() {
        evaluationSet = new ArrayList<>();
    }

    public EvaluationSet(List<EvaluationEntry> evaluationSet) {
        this.evaluationSet = evaluationSet;
    }

    public EvaluationSet(InputStream in) {
        this.evaluationSet = new ArrayList<>();

        try {

            CSVReader csvReadear = new CSVReader(new InputStreamReader(in), '\t');

            String[] nextLine;

            while ((nextLine = csvReadear.readNext()) != null) {
                EvaluationEntry entry = new EvaluationEntry(nextLine);
                evaluationSet.add(entry);
            }

            csvReadear.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public List<EvaluationEntry> getEvaluationSet() {
        return evaluationSet;
    }

    public void setEvaluationSet(List<EvaluationEntry> evaluationSet) {
        this.evaluationSet = evaluationSet;
    }

    public int size() {
        return evaluationSet.size();
    }

}
