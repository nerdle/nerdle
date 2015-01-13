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

package de_tu_berlin.dima.nerdle.provider;

import java.io.File;
import java.util.HashMap;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.impls.neo4j.Neo4jGraph;

import de_tu_berlin.dima.nerdle.util.ResourceManager;

public class TinkerpopGraphSingleton {

    private static TinkerpopGraphSingleton instance;

    private HashMap<String, Graph> graphs;
    private PropertiesConfiguration propertiesConfiguration;

    private TinkerpopGraphSingleton() {
        try {
            propertiesConfiguration = new PropertiesConfiguration(ResourceManager.getResourcePath(File.separator + "nerdle_config.properties"));
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
        String[] graphStrings = propertiesConfiguration.getStringArray("tinkerpop.graphs");

        String graphsBasePath = propertiesConfiguration.getString("tinkerpop.basePath");

        graphs = new HashMap<String, Graph>();

        for (String graph : graphStrings) {
            Graph g = new Neo4jGraph(graphsBasePath + File.separator + graph);
            graphs.put(graph, g);
        }
    }

    public static TinkerpopGraphSingleton getInstance() throws ConfigurationException {
        if (TinkerpopGraphSingleton.instance == null) {
            TinkerpopGraphSingleton.instance = new TinkerpopGraphSingleton();
        }
        return TinkerpopGraphSingleton.instance;
    }

    public HashMap<String, Graph> getGraphs() {
        return graphs;
    }

    public void shutdown() {
        for (Graph graph : graphs.values()) {
            graph.shutdown();
        }
    }
}
