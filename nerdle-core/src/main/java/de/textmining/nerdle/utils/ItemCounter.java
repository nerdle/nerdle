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

package de.textmining.nerdle.utils;

import com.google.common.collect.Maps;

import java.util.Map;

public class ItemCounter {

    Map<String, Float> internalMap = Maps.newHashMap();

    public void put(String item, Float count) {
        if (internalMap.containsKey(item))
            internalMap.put(item, count + internalMap.get(item));
        else internalMap.put(item, count);
    }

    public Float getItemCount(String item) {
        return internalMap.get(item);
    }

    public Map<String, Float> getAll() {
        return internalMap;
    }
}
