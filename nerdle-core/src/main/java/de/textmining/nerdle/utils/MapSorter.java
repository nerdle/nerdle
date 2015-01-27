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

import com.google.common.collect.Multimap;
import com.google.common.primitives.Ints;

import java.util.*;

public class MapSorter {

    public static <K, V extends Comparable<? super V>> List<Map.Entry<K, Collection<String>>> multimapSortedByListSize(final Multimap<K, String> multiMap) {

        List<Map.Entry<K, Collection<String>>> entries = new ArrayList<Map.Entry<K, Collection<String>>>();
        entries.addAll(multiMap.asMap().entrySet());

        Collections.sort(entries, new Comparator<Map.Entry<K, Collection<String>>>() {
            // @Override
            public int compare(Map.Entry<K, Collection<String>> e1, Map.Entry<K, Collection<String>> e2) {
                return Ints.compare(e2.getValue().size(), e1.getValue().size());
            }
        });

        return entries;
    }

    public static <K, V extends Comparable<? super V>> SortedSet<Map.Entry<K, V>> sortByValueDesc(Map<K, V> map) {
        return entriesSortedByValues(map, false);
    }

    public static <K, V extends Comparable<? super V>> SortedSet<Map.Entry<K, V>> entriesSortedByValues(Map<K, V> map, boolean invertOrder) {

        int c = -1;
        if (invertOrder) {
            c = 1;
        }
        final int c1 = c;

        SortedSet<Map.Entry<K, V>> sortedEntries = new TreeSet<Map.Entry<K, V>>(new Comparator<Map.Entry<K, V>>() {
            // @Override
            public int compare(Map.Entry<K, V> e1, Map.Entry<K, V> e2) {
                if (e1.getValue().compareTo(e2.getValue()) == 0)
                    return 1;

                return c1 * e1.getValue().compareTo(e2.getValue());
            }
        });
        sortedEntries.addAll(map.entrySet());
        return sortedEntries;
    }
}
