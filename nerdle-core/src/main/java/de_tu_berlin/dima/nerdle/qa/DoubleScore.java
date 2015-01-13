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

public class DoubleScore {

    private double maxScore;

    private double sumScore;

    public double getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(double a) {
        this.maxScore = a;
    }

    public double getSumScore() {
        return sumScore;
    }

    public void setSumScore(double b) {
        this.sumScore = b;
    }

    public DoubleScore(double a, double b) {
        super();
        this.maxScore = a;
        this.sumScore = b;
    }

}
