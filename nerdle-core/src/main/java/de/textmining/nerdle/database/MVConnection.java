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

import java.util.ArrayList;

import org.h2.mvstore.MVMap;
import org.h2.mvstore.MVStore;

import de.textmining.nerdle.question.answering.model.NerdleFact;

public class MVConnection {

    private MVStore mvStore;

    public MVConnection(MVStore mvStore) {
        this.mvStore = mvStore;
    }

    public MVConnection(String mvPath) {
        this.mvStore = new MVStore.Builder().fileName(mvPath).readOnly().open();
    }

    public MVMap<String, ArrayList<NerdleFact>> getMap() {
        MVMap<String, ArrayList<NerdleFact>> mvMap = mvStore.openMap("data");
        return mvMap;
    }

    public MVStore getMvStore() {
        return mvStore;
    }

    public void setMvStore(MVStore mvStore) {
        this.mvStore = mvStore;
    }

}
