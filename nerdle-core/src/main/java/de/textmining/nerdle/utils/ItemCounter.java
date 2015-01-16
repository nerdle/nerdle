package de.textmining.nerdle.utils;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Created by alan on 12/9/13.
 */
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
