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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import com.google.gson.Gson;

import de.textmining.nerdle.question.answering.model.NerdleFact;

public class H2Store {

    private final String SQL_CREATE_FACTS = "CREATE TABLE IF NOT EXISTS facts(id integer NOT NULL PRIMARY KEY, rolesetid varchar, fact varchar)";
    private PreparedStatement fact_insert;
    private Connection conn = null;
    Gson gson = new Gson();

    public H2Store(Connection conn) {
        this.conn = conn;
    }

    public H2Store(String dbURL) {
        try {

            Class.forName("org.h2.Driver");

            conn = DriverManager.getConnection(dbURL);

            Statement stat = conn.createStatement();
            stat.executeUpdate(SQL_CREATE_FACTS);

            fact_insert = conn.prepareStatement("INSERT INTO facts VALUES (?,?,?);");

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void addFact(int id, NerdleFact fact) {

        try {
            fact_insert.setInt(1, id);
            fact_insert.setString(2, fact.getPredicate().getRolesetID());
            fact_insert.setString(3, gson.toJson(fact, NerdleFact.class));
            fact_insert.addBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void persist() {
        System.out.println("Persist relation database...");
        try {
            fact_insert.executeBatch();
            fact_insert.clearBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createIndex() {
        System.out.println("Creating relational index...");
        try {
            Statement stat = conn.createStatement();
            System.out.println("Create facts_rolesetID_idx...");
            int executeUpdate = stat.executeUpdate("CREATE INDEX facts_rolesetID_idx ON facts(rolesetid);");
            System.out.println(executeUpdate);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }

}
