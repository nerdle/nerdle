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

package de_tu_berlin.dima.nerdle.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import com.clearnlp.component.AbstractComponent;
import com.clearnlp.dependency.DEPNode;
import com.clearnlp.dependency.DEPTree;
import com.clearnlp.dependency.srl.SRLArc;
import com.clearnlp.dependency.srl.SRLTree;
import com.clearnlp.nlp.NLPGetter;
import com.clearnlp.nlp.NLPMode;
import com.clearnlp.reader.AbstractReader;
import com.clearnlp.segmentation.AbstractSegmenter;
import com.clearnlp.tokenization.AbstractTokenizer;
import com.clearnlp.util.pair.StringIntPair;
import com.google.common.collect.Lists;
import com.google.gson.Gson;

public class ClearNLPQueryParser {

    Gson gson = new Gson();

    private List<AbstractComponent> clearNLPinstance;
    private AbstractSegmenter clearNLPsegmenter;

    public ClearNLPQueryParser() {
        try {
            clearNLPinstance = initClearNLP();
        } catch (IOException ex) {
        }

    }

    private List<AbstractComponent> initClearNLP() throws IOException {
        if (clearNLPinstance == null) {
            final String language = AbstractReader.LANG_EN;
            final String modelType = "general-en";
            AbstractTokenizer tokenizer = NLPGetter.getTokenizer(language);
            AbstractComponent tagger = NLPGetter.getComponent(modelType, language, NLPMode.MODE_POS);
            AbstractComponent parser = NLPGetter.getComponent(modelType, language, NLPMode.MODE_DEP);
            AbstractComponent identifier = NLPGetter.getComponent(modelType, language, NLPMode.MODE_PRED);
            AbstractComponent classifier = NLPGetter.getComponent(modelType, language, NLPMode.MODE_ROLE);
            AbstractComponent labeler = NLPGetter.getComponent(modelType, language, NLPMode.MODE_SRL);
            clearNLPinstance = Lists.newArrayList(tagger, parser, identifier, classifier, labeler);
            clearNLPsegmenter = NLPGetter.getSegmenter(language, tokenizer);
        }
        return clearNLPinstance;
    }

    public void parseQuery(String query) {

        DEPTree tree;

        BufferedReader buffer = new BufferedReader(new StringReader(query));
        for (List<String> tokens : clearNLPsegmenter.getSentences(buffer)) {
            /* dependency tree */
            tree = NLPGetter.toDEPTree(tokens);
            String sentence = tree.toStringRaw();

            System.out.println("\nSENT: " + sentence);

            /* pos tagging */
            for (AbstractComponent component : clearNLPinstance) {
                component.process(tree);
            }

            /* semantic role labeling */
            srl(tree, sentence);

            System.out.println(tree.toStringSRL());

        }

    }

    private void srl(DEPTree tree, String sentence) {
        List<Integer> handled = Lists.newArrayList();
        StringIntPair[][] roots = tree.getSHeads();
        for (StringIntPair[] root : roots) {
            for (StringIntPair stringIntPair : root) {
                if (handled.contains(stringIntPair.i))
                    continue;
                handled.add(stringIntPair.i);

                SRLTree srlTree = tree.getSRLTree(stringIntPair.i);
                DEPNode depNode = tree.get(stringIntPair.i);

                /* predicate */
                System.out.println("PRED: " + depNode.form + ":" + depNode.lemma + ":" + srlTree.getRolesetID());

                /* arguments */
                List<SRLArc> arguments = srlTree.getArguments();
                for (SRLArc argument : arguments) {
                    List<DEPNode> dependentNodeList = argument.getNode().getSubNodeSortedList();
                    StringBuilder arg = new StringBuilder();
                    // build argument string
                    for (DEPNode node : dependentNodeList) {
                        if (arg.length() > 0)
                            arg.append(" ");
                        arg.append(node.form);
                    }
                    String argStr = arg.toString();
                    System.out.println("ARG : " + argStr + ":" + argument.getLabel() + ":" + argument.getNode().getLabel() + ":"
                            + argument.getNode().getHead().lemma);
                }
            }
        }
    }

}
