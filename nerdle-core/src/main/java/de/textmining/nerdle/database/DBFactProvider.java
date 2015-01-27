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

package de.textmining.nerdle.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import de.textmining.nerdle.question.answering.model.NerdleArg;
import de.textmining.nerdle.question.answering.model.NerdleFact;
import de.textmining.nerdle.question.answering.model.NerdlePredicate;
import de.textmining.nerdle.question.answering.string.matcher.ExactStringMatcher;
import de.textmining.nerdle.question.answering.string.matcher.StringMatcher;

public class DBFactProvider implements FactProvider {

    private StringMatcher stringMatcher = new ExactStringMatcher();

    private DBConnection dbConnection;

    public DBFactProvider(DBConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    @Override
    public List<NerdleFact> getFactsByPredicate(NerdlePredicate questionPredicate) {

        List<NerdleFact> facts = new ArrayList<>();

        PreparedStatement pst = null;
        ResultSet rs = null;

        try {

            String stm = "SELECT * FROM facts, arguments WHERE facts.id = arguments.fact_id AND facts.rolesetid = ?";
            pst = dbConnection.getConnection().prepareStatement(stm);
            pst.setString(1, questionPredicate.getRolesetID());
            rs = pst.executeQuery();

            facts = getFacts(rs);

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pst != null) {
                    pst.close();
                }

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        return facts;
    }

    @Override
    public List<NerdleFact> getFactsByMatch(NerdleFact questionFact, NerdleArg searchArg) {

        final List<NerdleArg> questionsArgs = new ArrayList<NerdleArg>();
        questionsArgs.addAll(questionFact.getArguments());
        questionsArgs.remove(searchArg);

        List<NerdleFact> facts = new ArrayList<>();

        PreparedStatement pst = null;
        ResultSet rs = null;

        try {

            String stm = "SELECT * FROM facts, arguments WHERE facts.id = arguments.fact_id AND facts.rolesetid = ?";
            pst = dbConnection.getConnection().prepareStatement(stm);
            NerdlePredicate questionPredicate = questionFact.getPredicate();
            pst.setString(1, questionPredicate.getRolesetID());
            rs = pst.executeQuery();

            List<NerdleFact> answerFacts = getFacts(rs);

            for (NerdleFact answerFact : answerFacts) {

                boolean foundArg = false;
                boolean foundSearchArg = false;

                final List<NerdleArg> answerArgs = answerFact.getArguments();

                for (NerdleArg answerArg : answerArgs) {

                    if (stringMatcher.argumentAndLabelMatch(questionsArgs, answerArg))
                        foundArg = true;
                    
                    if (answerArg.getArgLabel().equals(searchArg.getArgLabel()))
                        foundSearchArg = true;

                }

                if (foundArg && foundSearchArg) {
                    facts.add(answerFact);
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pst != null) {
                    pst.close();
                }

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        return facts;
    }

    private List<NerdleFact> getFacts(ResultSet rs) throws SQLException {

        List<NerdleFact> facts = new ArrayList<>();

        NerdleFact fact = null;

        int currentFactId = -1;

        while (rs.next()) {

            int id = rs.getInt(1);

            // new fact
            if (fact == null || currentFactId != id) {

                // add fact to list
                if (fact != null) {
                    facts.add(fact);
                }

                currentFactId = id;

                String source = rs.getString(2);
                String sentence = rs.getString(3);
                String text = rs.getString(4);
                String lemma = rs.getString(5);
                String rolesetID = rs.getString(6);

                NerdlePredicate predicate = new NerdlePredicate(text, lemma, rolesetID);

                fact = new NerdleFact(sentence, source);
                fact.setPredicate(predicate);
            }

            String argText = rs.getString(8);
            String pos = rs.getString(9);
            String argLabel = rs.getString(10);
            String depLabel = rs.getString(11);

            NerdleArg arg = new NerdleArg(argText, pos, argLabel, depLabel);

            fact.addArgument(arg);

        }

        // the last fact
        if (fact != null) {
            facts.add(fact);
        }

        return facts;
    }

}