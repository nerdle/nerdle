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

import java.io.File;
import java.util.HashMap;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import de.textmining.nerdle.utils.ResourceManager;

public class DBSingleton {

    private HashMap<String, DBConnection> connections;

    public DBSingleton(String path) {
        try {
            PropertiesConfiguration propertiesConfiguration = new PropertiesConfiguration(path);

            String base_url = propertiesConfiguration.getString("db.base_url");
            String user = propertiesConfiguration.getString("db.user");
            String password = propertiesConfiguration.getString("db.password");
            String[] databases = propertiesConfiguration.getStringArray("db.databases");

            connections = new HashMap<>();

            for (String database : databases) {
                String url = base_url + database;
                connections.put(database, new DBConnection(url, user, password));
            }

        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
    }

    public DBSingleton() {
        try {
            PropertiesConfiguration propertiesConfiguration = new PropertiesConfiguration(ResourceManager.getResourcePath(File.separator
                    + "nerdle_config.properties"));

            String base_url = propertiesConfiguration.getString("db.base_url");
            String user = propertiesConfiguration.getString("db.user");
            String password = propertiesConfiguration.getString("db.password");
            String[] databases = propertiesConfiguration.getStringArray("db.databases");

            connections = new HashMap<>();

            for (String database : databases) {
                String url = base_url + database;
                connections.put(database, new DBConnection(url, user, password));
            }

        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
    }

    public HashMap<String, DBConnection> getConnections() {
        return connections;
    }

    public void setConnections(HashMap<String, DBConnection> connections) {
        this.connections = connections;
    }

}