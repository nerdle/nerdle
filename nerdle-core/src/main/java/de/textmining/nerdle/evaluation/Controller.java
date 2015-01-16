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

import de.textmining.nerdle.question.answering.fact.matcher.ExactQuestionFactMatcher;
import de.textmining.nerdle.question.answering.question.parsing.ClearNLPQuestionParser;
import de.textmining.nerdle.question.answering.MatchFactQuestionAnswerer;
import de.textmining.nerdle.database.DBFactProvider;
import de.textmining.nerdle.database.DBSingleton;
import de.textmining.nerdle.question.answering.QuestionAnswerer;

public class Controller {

    public static void main(String[] args) throws Exception {

        EvaluationConfig evaluationConfig = new EvaluationConfig();
        evaluationConfig.setLimit(5);

        Evaluator evaluator = new Evaluator(evaluationConfig);

        DBSingleton dbSingleton = new DBSingleton();

        ClearNLPQuestionParser questionParser = new ClearNLPQuestionParser();
        ExactQuestionFactMatcher questionFactMatcher = new ExactQuestionFactMatcher();
        DBFactProvider factProvider = new DBFactProvider(dbSingleton.getConnections().get("postgres"));

        QuestionAnswerer questionAnswerer = new MatchFactQuestionAnswerer(questionParser, questionFactMatcher,
                factProvider);

        evaluator.start(questionAnswerer);
    }

}
