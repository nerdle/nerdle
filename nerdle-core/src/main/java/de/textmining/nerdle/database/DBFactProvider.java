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

import com.google.gson.Gson;

import de.textmining.nerdle.question.answering.model.NerdleArg;
import de.textmining.nerdle.question.answering.model.NerdleFact;
import de.textmining.nerdle.question.answering.model.NerdlePredicate;
import de.textmining.nerdle.question.answering.string.matcher.ExactStringMatcher;
import de.textmining.nerdle.question.answering.string.matcher.StringMatcher;
import etm.core.configuration.EtmManager;
import etm.core.monitor.EtmMonitor;
import etm.core.monitor.EtmPoint;

public class DBFactProvider implements FactProvider {

    private static final EtmMonitor etmMonitor = EtmManager.getEtmMonitor();

    private StringMatcher stringMatcher = new ExactStringMatcher();

    private DBConnection dbConnection;

    public DBFactProvider(DBConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    @Override
    public List<NerdleFact> getFactsByPredicate(NerdlePredicate questionPredicate) {

        EtmPoint point = etmMonitor.createPoint("DBFactProvider:getFactsByPredicate");

        List<NerdleFact> facts = new ArrayList<>();

        PreparedStatement pst = null;
        ResultSet rs = null;

        try {

            String stm = "SELECT * FROM facts WHERE facts.rolesetid = ?";
            pst = dbConnection.getConnection().prepareStatement(stm);
            pst.setString(1, questionPredicate.getRolesetID());
            EtmPoint point2 = etmMonitor.createPoint("DBFactProvider:Q1");
            rs = pst.executeQuery();
            point2.collect();

            facts = getFacts(rs);

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            point.collect();
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

        EtmPoint point = etmMonitor.createPoint("DBFactProvider:getFactsByMatch");

        final List<NerdleArg> questionsArgs = new ArrayList<NerdleArg>();
        questionsArgs.addAll(questionFact.getArguments());
        questionsArgs.remove(searchArg);

        List<NerdleFact> facts = new ArrayList<>();

        PreparedStatement pst = null;
        ResultSet rs = null;

        try {

            String stm = "SELECT * FROM facts WHERE facts.rolesetid = ?";
            pst = dbConnection.getConnection().prepareStatement(stm);
            NerdlePredicate questionPredicate = questionFact.getPredicate();
            pst.setString(1, questionPredicate.getRolesetID());

            EtmPoint point2 = etmMonitor.createPoint("DBFactProvider:Q2");
            rs = pst.executeQuery();
            point2.collect();

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
            point.collect();
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
    public int getFactsCountByPredicate(NerdlePredicate questionPredicate) {
        EtmPoint point = etmMonitor.createPoint("DBFactProvider:getFactsCountByPredicate");

        PreparedStatement pst = null;
        ResultSet rs = null;

        int count = 0;

        try {

            String stm = "SELECT count(*) FROM facts WHERE facts.rolesetid = ?";
            pst = dbConnection.getConnection().prepareStatement(stm);
            pst.setInt(1, questionPredicate.getRolesetID().hashCode());
            EtmPoint point2 = etmMonitor.createPoint("DBFactProvider:Q3");
            rs = pst.executeQuery();
            point2.collect();

            if (rs.next()) {
                count = rs.getInt(1);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            point.collect();
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

        return count;
    }

    private List<NerdleFact> getFacts(ResultSet rs) throws SQLException {

        Gson gson = new Gson();

        EtmPoint point = etmMonitor.createPoint("DBFactProvider:getFacts");

        List<NerdleFact> facts = new ArrayList<>();

        while (rs.next()) {
            String factJson = rs.getString(3);

            EtmPoint point2 = etmMonitor.createPoint("DBFactProvider:Gson");
            NerdleFact fact = gson.fromJson(factJson, NerdleFact.class);
            point2.collect();

            facts.add(fact);
        }

        point.collect();

        return facts;
    }

}