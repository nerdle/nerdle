package de.textmining.nerdle.database;

import java.util.ArrayList;
import java.util.List;

import de.textmining.nerdle.question.answering.model.NerdleArg;
import de.textmining.nerdle.question.answering.model.NerdleFact;
import de.textmining.nerdle.question.answering.model.NerdlePredicate;
import de.textmining.nerdle.question.answering.string.matcher.StringMatcher;
import etm.core.configuration.EtmManager;
import etm.core.monitor.EtmMonitor;
import etm.core.monitor.EtmPoint;

public class MVFactProvider implements FactProvider {

    private static final EtmMonitor etmMonitor = EtmManager.getEtmMonitor();

    private StringMatcher stringMatcher;

    private MVConnection mvConnection;

    public MVFactProvider(MVConnection mvConnection, StringMatcher stringMatcher) {
        this.stringMatcher = stringMatcher;
        this.mvConnection = mvConnection;
    }

    @Override
    public List<NerdleFact> getFactsByPredicate(NerdlePredicate questionPredicate) {

        EtmPoint point = etmMonitor.createPoint("MVFactProvider:getFactsByPredicate");

        ArrayList<NerdleFact> facts = new ArrayList<>();

        if (mvConnection.getMap().containsKey(questionPredicate.getRolesetID())) {
            facts = mvConnection.getMap().get(questionPredicate.getRolesetID());
        }

        point.collect();

        return facts;
    }

    @Override
    public int getFactsCountByPredicate(NerdlePredicate questionPredicate) {
        EtmPoint point = etmMonitor.createPoint("MVFactProvider:getFactsCountByPredicate");

        int size = 0;

        if (mvConnection.getMap().containsKey(questionPredicate.getRolesetID())) {
            size = mvConnection.getMap().get(questionPredicate.getRolesetID()).size();
        }

        point.collect();

        return size;
    }

    @Override
    public List<NerdleFact> getFactsByMatch(NerdleFact questionFact, NerdleArg searchArg) {
        EtmPoint point = etmMonitor.createPoint("MVFactProvider:getFactsByMatch");

        final List<NerdleArg> questionsArgs = new ArrayList<NerdleArg>();
        questionsArgs.addAll(questionFact.getArguments());
        questionsArgs.remove(searchArg);

        List<NerdleFact> facts = new ArrayList<>();

        if (mvConnection.getMap().containsKey(questionFact.getPredicate().getRolesetID())) {
            ArrayList<NerdleFact> answerFacts = mvConnection.getMap().get(questionFact.getPredicate().getRolesetID());

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

        }

        point.collect();

        return facts;
    }

}
