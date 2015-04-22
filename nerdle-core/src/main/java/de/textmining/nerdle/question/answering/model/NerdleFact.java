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

package de.textmining.nerdle.question.answering.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class NerdleFact implements Serializable {

    private static final long serialVersionUID = 1L;

    private String sentence;

    private String source;

    private NerdlePredicate predicate;

    private List<NerdleArg> arguments;

    private NerdleArg questionArg = null;

    public String getSentence() {
        return sentence;
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public NerdlePredicate getPredicate() {
        return predicate;
    }

    public void setPredicate(NerdlePredicate predicate) {
        this.predicate = predicate;
    }

    public List<NerdleArg> getArguments() {
        return arguments;
    }

    public void setArguments(List<NerdleArg> arguments) {
        this.arguments = arguments;
    }

    public void addArgument(NerdleArg arg) {
        arguments.add(arg);
    }
    
    public NerdleFact(String sentence, String source, NerdlePredicate predicate, List<NerdleArg> arguments, NerdleArg questionArg) {
        this.sentence = sentence;
        this.source = source;
        this.predicate = predicate;
        this.arguments = arguments;
        this.questionArg = questionArg;
    }

    public NerdleFact(String sentence, String source, NerdlePredicate predicate, List<NerdleArg> arguments) {
        this.sentence = sentence;
        this.source = source;
        this.predicate = predicate;
        this.arguments = arguments;
    }

    public NerdleFact(String sentence, String source) {
        this.sentence = sentence;
        this.source = source;
        this.arguments = new ArrayList<NerdleArg>();
    }

    public NerdleArg getQuestionArg() {
        return questionArg;
    }

    public void setQuestionArg(NerdleArg questionArg) {
        this.questionArg = questionArg;
    }

    @Override
    public NerdleFact clone() {
        return new NerdleFact(sentence, source, predicate, new ArrayList<>(arguments), questionArg);
    }

    @Override
    public int hashCode() {
        // TODO removed source to remove duplicates
        final int prime = 31;
        int result = 1;
        result = prime * result + ((arguments == null) ? 0 : arguments.hashCode());
        result = prime * result + ((predicate == null) ? 0 : predicate.hashCode());
        result = prime * result + ((sentence == null) ? 0 : sentence.hashCode());
        // result = prime * result + ((source == null) ? 0 : source.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        // TODO removed source to remove duplicates
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        NerdleFact other = (NerdleFact) obj;
        if (arguments == null) {
            if (other.arguments != null)
                return false;
        } else if (!arguments.equals(other.arguments))
            return false;
        if (predicate == null) {
            if (other.predicate != null)
                return false;
        } else if (!predicate.equals(other.predicate))
            return false;
        if (sentence == null) {
            if (other.sentence != null)
                return false;
        } else if (!sentence.equals(other.sentence))
            return false;
        // if (source == null) {
        // if (other.source != null)
        // return false;
        // } else if (!source.equals(other.source))
        // return false;
        return true;
    }

    @Override
    public String toString() {
        return "NerdleFact [sentence=" + sentence + ", source=" + source + ", predicate=" + predicate + ", arguments=" + arguments + "]";
    }

    public String getArgument(String questionArg) {
        for (NerdleArg argument : arguments) {
            if (argument.getArgLabel().equals(questionArg)) return argument.getText();
        }

        return null;
    }

}
