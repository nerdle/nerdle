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

public class NerdlePredicate implements Serializable {

    private static final long serialVersionUID = 1L;

    private String text;

    private String lemma;

    private String rolesetID;

    public NerdlePredicate(String text, String lemma, String rolesetID) {
        this.text = text;
        this.lemma = lemma;
        this.rolesetID = rolesetID;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getLemma() {
        return lemma;
    }

    public void setLemma(String lemma) {
        this.lemma = lemma;
    }

    public String getRolesetID() {
        return rolesetID;
    }

    public void setRolesetID(String rolesetID) {
        this.rolesetID = rolesetID;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((lemma == null) ? 0 : lemma.hashCode());
        result = prime * result + ((rolesetID == null) ? 0 : rolesetID.hashCode());
        result = prime * result + ((text == null) ? 0 : text.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        NerdlePredicate other = (NerdlePredicate) obj;
        if (lemma == null) {
            if (other.lemma != null)
                return false;
        } else if (!lemma.equals(other.lemma))
            return false;
        if (rolesetID == null) {
            if (other.rolesetID != null)
                return false;
        } else if (!rolesetID.equals(other.rolesetID))
            return false;
        if (text == null) {
            if (other.text != null)
                return false;
        } else if (!text.equals(other.text))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "NerdlePredicate [text=" + text + ", lemma=" + lemma + ", rolesetID=" + rolesetID + "]";
    }

}
