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

public class NerdleArg implements Serializable {

    private static final long serialVersionUID = 1L;

    private String text;

	private String pos;

	private String argLabel;

	private String depLabel;

	public NerdleArg(String text, String pos, String argLabel, String depLabel) {
		this.text = text;
		this.pos = pos;
		this.argLabel = argLabel;
		this.depLabel = depLabel;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getArgLabel() {
		return argLabel;
	}

	public void setArgLabel(String argLabel) {
		this.argLabel = argLabel;
	}

	public String getDepLabel() {
		return depLabel;
	}

	public void setDepLabel(String depLabel) {
		this.depLabel = depLabel;
	}

	public String getPos() {
		return pos;
	}

	public void setPos(String pos) {
		this.pos = pos;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((argLabel == null) ? 0 : argLabel.hashCode());
		result = prime * result
				+ ((depLabel == null) ? 0 : depLabel.hashCode());
		result = prime * result + ((pos == null) ? 0 : pos.hashCode());
		result = prime * result + ((text == null) ? 0 : text.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		// if (getClass() != obj.getClass())
		// return false;
		NerdleArg other = (NerdleArg) obj;
		if (argLabel == null) {
			if (other.argLabel != null)
				return false;
		} else if (!argLabel.equals(other.argLabel))
			return false;
		if (depLabel == null) {
			if (other.depLabel != null)
				return false;
		} else if (!depLabel.equals(other.depLabel))
			return false;
		if (pos == null) {
			if (other.pos != null)
				return false;
		} else if (!pos.equals(other.pos))
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
		return "NerdleArg [text=" + text + ", pos=" + pos + ", argLabel="
				+ argLabel + ", depLabel=" + depLabel + "]";
	}

}
