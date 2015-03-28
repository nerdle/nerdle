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

import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.h2.mvstore.MVStore;

import etm.core.configuration.EtmManager;
import etm.core.monitor.EtmMonitor;
import etm.core.monitor.EtmPoint;

public class MVSingleton {

    private static final EtmMonitor etmMonitor = EtmManager.getEtmMonitor();

    private HashMap<String, MVConnection> connections;

    public MVSingleton(String path) {
        try {
            config(path);

        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
    }

    public MVSingleton() {
        try {
            config(Paths.get(getClass().getResource("/nerdle_config.properties").toURI()).toFile().getPath());
        } catch (ConfigurationException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private void config(String path) throws ConfigurationException {

        EtmPoint point = etmMonitor.createPoint("DBSingleton:config");

        PropertiesConfiguration propertiesConfiguration = new PropertiesConfiguration(path);

        Set<String> mvIds = new TreeSet<>();

        Iterator keys = propertiesConfiguration.getKeys("mv");

        while (keys.hasNext()) {
            String key = (String) keys.next();
            String[] split = key.split("\\.");
            mvIds.add(split[0] + "." + split[1]);
        }

        connections = new HashMap<>();

        for (String dbId : mvIds) {
            String mvName = propertiesConfiguration.getString(dbId + "." + "name");
            String mvPath = propertiesConfiguration.getString(dbId + "." + "path");

            MVStore s = new MVStore.Builder().fileName(mvPath).readOnly().open();

            connections.put(mvName, new MVConnection(s));
        }

        point.collect();

    }

    public HashMap<String, MVConnection> getConnections() {
        return connections;
    }

    public void setConnections(HashMap<String, MVConnection> connections) {
        this.connections = connections;
    }

    public static EtmMonitor getEtmmonitor() {
        return etmMonitor;
    }

}