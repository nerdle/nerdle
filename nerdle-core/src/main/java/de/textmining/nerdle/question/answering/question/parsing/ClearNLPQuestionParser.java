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

package de.textmining.nerdle.question.answering.question.parsing;

import java.util.ArrayList;
import java.util.List;

import de.textmining.nerdle.question.answering.model.Question;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.log4j.Logger;

import de.textmining.nerdle.information.extraction.ClearNLPHelper;
import de.textmining.nerdle.question.answering.model.NerdleArg;
import de.textmining.nerdle.question.answering.model.NerdleFact;
import etm.core.configuration.EtmManager;
import etm.core.monitor.EtmMonitor;
import etm.core.monitor.EtmPoint;

public class ClearNLPQuestionParser implements QuestionParser {

    private static final EtmMonitor etmMonitor = EtmManager.getEtmMonitor();

    protected final static Logger log = Logger.getLogger(ClearNLPQuestionParser.class);

    @Override
    public List<NerdleFact> analyzeQuestion(Question question) throws InterruptedException, ConfigurationException {

        EtmPoint point = etmMonitor.createPoint("ClearNLPQuestionParser:analyzeQuestion");

        log.debug("Handling question: " + question.getQuestion());

        List<NerdleFact> questionFacts = ClearNLPHelper.INSTANCE.extractFactsFromSentence(question.getQuestion(), "");
        List<NerdleFact> retFacts = new ArrayList<>();

        for (NerdleFact questionFact : questionFacts) {
            for (NerdleArg nerdleArg : questionFact.getArguments()) {
                if (nerdleArg.getArgLabel().startsWith("R-")) {
                    questionFact.setQuestionArg(new NerdleArg("", "", nerdleArg.getArgLabel().substring(2), ""));
                    retFacts.add(questionFact);
                }
            }
        }
        
        point.collect();

        return retFacts;
    }

}
