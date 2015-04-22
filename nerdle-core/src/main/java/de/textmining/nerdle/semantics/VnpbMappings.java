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

package de.textmining.nerdle.semantics;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import de.textmining.nerdle.question.answering.model.NerdleFact;
import de.textmining.nerdle.question.answering.model.NerdlePredicate;

public class VnpbMappings {

    private static VnpbMappings instance;

    public static VnpbMappings getInstance() {
        if (VnpbMappings.instance == null) {
            VnpbMappings.instance = new VnpbMappings();
        }
        return VnpbMappings.instance;
    }

    private HashMap<String, List<String>> pbToSynonyms = new HashMap<>();

    private VnpbMappings() {
        try {
            load("semlink-122c/vnpbMappings");
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void load(String path) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        DocumentBuilder builder = factory.newDocumentBuilder();

        Document document = builder.parse(ClassLoader.getSystemResourceAsStream(path));

        NodeList nodeList = document.getDocumentElement().getChildNodes();

        HashMap<String, List<String>> vnToPB = new HashMap<>();
        HashMap<String, List<String>> pbToVN = new HashMap<>();

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);

            if (node instanceof Element) {

                NodeList childNodes = node.getChildNodes();

                for (int j = 0; j < childNodes.getLength(); j++) {

                    Node cNode = childNodes.item(j);

                    if (cNode instanceof Element) {

                        String pbRoleset = cNode.getAttributes().getNamedItem("pb-roleset").getNodeValue();
                        String vnClass = cNode.getAttributes().getNamedItem("vn-class").getNodeValue();

                        if (!vnToPB.containsKey(vnClass)) {
                            vnToPB.put(vnClass, new ArrayList<String>());
                        }

                        vnToPB.get(vnClass).add(pbRoleset);

                        if (!pbToVN.containsKey(pbRoleset)) {
                            pbToVN.put(pbRoleset, new ArrayList<String>());
                        }

                        pbToVN.get(pbRoleset).add(vnClass);

                    }

                }
            }
        }

        for (String pbRolset : pbToVN.keySet()) {
            List<String> synonyms = new ArrayList<String>();
            for (String vnClass : pbToVN.get(pbRolset)) {
                synonyms.addAll(vnToPB.get(vnClass));
            }

            while (synonyms.contains(pbRolset))
                synonyms.remove(pbRolset);

            pbToSynonyms.put(pbRolset, synonyms);
        }

    }

    public List<String> getSynonyms(String pbRoleset) {
        return pbToSynonyms.get(pbRoleset);
    }

    public List<NerdleFact> getSynonyms(NerdleFact fact, int limit) {

        List<NerdleFact> facts = new ArrayList<>();

        List<String> synonyms = pbToSynonyms.get(fact.getPredicate().getRolesetID());

        if (synonyms != null) {
            for (String synonym : synonyms) {
                NerdleFact fact2 = fact.clone();
                fact2.setPredicate(new NerdlePredicate("dummy", "dummy", synonym));
                facts.add(fact2);
            }
        }

        if (facts.size() > limit) {
            return facts.subList(0, limit);
        }

        return facts;
    }

    public HashMap<String, List<String>> getPbToSynonyms() {
        return pbToSynonyms;
    }

    public void setPbToSynonyms(HashMap<String, List<String>> pbToSynonyms) {
        this.pbToSynonyms = pbToSynonyms;
    }

}
