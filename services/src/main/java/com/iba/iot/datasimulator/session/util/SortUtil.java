package com.iba.iot.datasimulator.session.util;

import java.util.Comparator;
import java.util.Map;

/**
 *
 */
public class SortUtil {

    /**
     *
     * @return
     */
    public static Comparator<? super Map.Entry<String, Object>> stringKeyComparator =
            (first, second) -> first.getKey().compareToIgnoreCase(second.getKey());

}
