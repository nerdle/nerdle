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

package de.textmining.nerdle.information.extraction;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
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
import com.clearnlp.util.UTArray;
import com.clearnlp.util.pair.StringIntPair;
import com.google.common.collect.Lists;

import de.textmining.nerdle.question.answering.model.NerdleArg;
import de.textmining.nerdle.question.answering.model.NerdleFact;
import de.textmining.nerdle.question.answering.model.NerdlePredicate;

public enum ClearNLPHelper {
    INSTANCE;

    private final String language = AbstractReader.LANG_EN;

    private AbstractTokenizer tokenizer = NLPGetter.getTokenizer(language);
    private AbstractSegmenter segmenter = NLPGetter.getSegmenter(language, tokenizer);
    private AbstractComponent tagger;
    private AbstractComponent parser;
    private AbstractComponent identifier;
    private AbstractComponent classifier;
    private AbstractComponent labeler;

    ClearNLPHelper() {
        try {
            tagger = NLPGetter.getComponent("general-en", language, NLPMode.MODE_POS);
            parser = NLPGetter.getComponent("general-en", language, NLPMode.MODE_DEP);
            identifier = NLPGetter.getComponent("general-en", language, NLPMode.MODE_PRED);
            classifier = NLPGetter.getComponent("general-en", language, NLPMode.MODE_ROLE);
            labeler = NLPGetter.getComponent("general-en", language, NLPMode.MODE_SRL);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> getSentences(String articleText) {
        List<String> sentences = new ArrayList<String>();

        for (List<String> sentence : segmenter.getSentences(new BufferedReader(new StringReader(articleText))))
            sentences.add(UTArray.join(sentence, " "));

        return sentences;
    }

    public List<NerdleFact> extractFactsFromSentence(String sentence, String source) {

        DEPTree tree = NLPGetter.toDEPTree(tokenizer.getTokens(sentence));

        List<AbstractComponent> components = Lists.newArrayList(tagger, parser, identifier, classifier, labeler);

        for (AbstractComponent component : components)
            component.process(tree);

        List<Integer> handled = Lists.newArrayList();
        StringIntPair[][] roots = tree.getSHeads();

        List<NerdleFact> nerdleFacts = new ArrayList<NerdleFact>();

        for (StringIntPair[] root : roots) {
            for (StringIntPair stringIntPair : root) {

                if (handled.contains(stringIntPair.i))
                    continue;

                handled.add(stringIntPair.i);

                SRLTree srlTree = tree.getSRLTree(stringIntPair.i);
                DEPNode depNode = tree.get(stringIntPair.i);

                // NerdleFact
                NerdleFact nerdleFact = new NerdleFact(sentence, source);

                String rolesetID = srlTree.getRolesetID();
                String lemma = depNode.lemma;
                String text = depNode.form;

                nerdleFact.setPredicate(new NerdlePredicate(text, lemma, rolesetID));

                List<SRLArc> arguments = srlTree.getArguments();

                for (SRLArc argument : arguments) {

                    List<DEPNode> dependentNodeList = argument.getNode().getSubNodeSortedList();

                    String argumentText = "";
                    String argumentPos = "";
                    for (DEPNode node : dependentNodeList) {
                        argumentText += node.form + " ";
                        argumentPos += node.pos + " ";
                    }

                    argumentText = argumentText.trim();
                    argumentPos = argumentPos.trim();

                    String argLabel = argument.getLabel();
                    String depLabel = argument.getNode().getLabel();

                    nerdleFact.addArgument(new NerdleArg(argumentText, argumentPos, argLabel, depLabel));

                }

                nerdleFacts.add(nerdleFact);

            }
        }

        return nerdleFacts;

    }

    public List<NerdleFact> extractFactsFromArticleText(String articleText, String source) {

        List<String> sentences = getSentences(articleText);

        List<NerdleFact> nerdleFacts = new ArrayList<NerdleFact>();

        for (String sentence : sentences) {
            nerdleFacts.addAll(extractFactsFromSentence(sentence, source));
        }

        return nerdleFacts;
    }

    public void pirntExtractions(String text) {

        DEPTree tree;

        BufferedReader buffer = new BufferedReader(new StringReader(text));
        for (List<String> tokens : segmenter.getSentences(buffer)) {
            /* dependency tree */
            tree = NLPGetter.toDEPTree(tokens);
            String sentence = tree.toStringRaw();

            System.out.println("\nSENT: " + sentence);

            List<AbstractComponent> components = Lists.newArrayList(tagger, parser, identifier, classifier, labeler);

            /* pos tagging */
            for (AbstractComponent component : components) {
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
