package de.textmining.nerdle.utils;

import com.google.common.collect.Multimap;
import com.google.common.primitives.Ints;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: alan
 */
public class MapSorter {

    public static <K, V extends Comparable<? super V>> List<Map.Entry<K, Collection<String>>> multimapSortedByListSize(
            final Multimap<K, String> multiMap) {

        List<Map.Entry<K, Collection<String>>> entries =
                new ArrayList<Map.Entry<K, Collection<String>>>();
        entries.addAll(multiMap.asMap().entrySet());

        Collections.sort(entries, new Comparator<Map.Entry<K, Collection<String>>>() {
            //@Override
            public int compare(Map.Entry<K, Collection<String>> e1,
                               Map.Entry<K, Collection<String>> e2) {
                return Ints.compare(e2.getValue().size(), e1.getValue().size());
            }
        });

        return entries;
    }

    public static <K, V extends Comparable<? super V>> SortedSet<Map.Entry<K, V>> sortByValueDesc(
            Map<K, V> map) {
        return entriesSortedByValues(map, false);
    }

    public static <K, V extends Comparable<? super V>> SortedSet<Map.Entry<K, V>> entriesSortedByValues(
            Map<K, V> map, boolean invertOrder) {

        int c = -1;
        if (invertOrder) {
            c = 1;
        }
        final int c1 = c;

        SortedSet<Map.Entry<K, V>> sortedEntries = new TreeSet<Map.Entry<K, V>>(
                new Comparator<Map.Entry<K, V>>() {
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
