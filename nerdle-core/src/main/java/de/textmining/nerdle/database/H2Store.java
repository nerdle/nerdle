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

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

public class H2Store {

    private final String SQL_CREATE_FACTS = "CREATE TABLE IF NOT EXISTS facts(id integer NOT NULL PRIMARY KEY, source varchar, sentence varchar, text varchar, lemma varchar, rolesetid varchar)";
    private final String SQL_CREATE_ARGUMENT = "CREATE TABLE IF NOT EXISTS arguments(id integer NOT NULL PRIMARY KEY, text varchar, pos varchar, arglabel varchar, deplabel varchar, fact_id integer NOT NULL, FOREIGN KEY (fact_id) REFERENCES facts(id))";
    private PreparedStatement fact_insert;
    private PreparedStatement argument_insert;
    private Connection conn = null;

    public H2Store(String dbURL) {
        try {

            Class.forName("org.h2.Driver");

            conn = DriverManager.getConnection(dbURL);

            Statement stat = conn.createStatement();
            stat.executeUpdate(SQL_CREATE_FACTS);
            stat.executeUpdate(SQL_CREATE_ARGUMENT);

            fact_insert = conn.prepareStatement("INSERT INTO facts VALUES (?,?,?,?,?,?);");
            argument_insert = conn.prepareStatement("INSERT INTO arguments VALUES (?,?,?,?,?,?);");

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void addFact(int id, String source, String sentence, String text, String lemma, String rolesetid) {
        try {
            fact_insert.setInt(1, id);
            fact_insert.setString(2, source);
            fact_insert.setString(3, sentence);
            fact_insert.setString(4, text);
            fact_insert.setString(5, lemma);
            fact_insert.setString(6, rolesetid);
            fact_insert.addBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addFacts(String csvFile) {
        System.out.println("Adding facts...");
        try {
            CSVReader reader = new CSVReader(new FileReader(csvFile), '\t', CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.NO_ESCAPE_CHARACTER);
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                addFact(Integer.parseInt(nextLine[0]), nextLine[1], nextLine[2], nextLine[3], nextLine[4], nextLine[5]);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addArgument(int id, String text, String pos, String argLabel, String depLabel, int factId) {
        try {
            argument_insert.setInt(1, id);
            argument_insert.setString(2, text);
            argument_insert.setString(3, pos);
            argument_insert.setString(4, argLabel);
            argument_insert.setString(5, depLabel);
            argument_insert.setInt(6, factId);
            argument_insert.addBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addArguments(String csvFile) {
        System.out.println("Adding arguments...");
        String[] nextLine = null;
        try {
            CSVReader reader = new CSVReader(new FileReader(csvFile), '\t', CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.NO_ESCAPE_CHARACTER);
            while ((nextLine = reader.readNext()) != null) {
                addArgument(Integer.parseInt(nextLine[0]), nextLine[1], nextLine[2], nextLine[3], nextLine[4], Integer.parseInt(nextLine[5]));
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void persist() {
        System.out.println("Persist relation database...");
        try {
            fact_insert.executeBatch();
            fact_insert.clearBatch();
            argument_insert.executeBatch();
            argument_insert.clearBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createIndex() {
        System.out.println("Creating relational index...");
        try {
            Statement stat = conn.createStatement();
            System.out.println("Create facts_rolesetID_idx...");
            stat.execute("CREATE INDEX facts_rolesetID_idx ON facts(rolesetid);");
            System.out.println("Create arguments_fact_id_idx...");
            stat.execute("CREATE INDEX arguments_fact_id_idx ON arguments(fact_id);");
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
