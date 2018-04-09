package com.iba.iot.datasimulator.common.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 *
 */
public class CollectionUtil {

    /**
     *
     * @param initial
     * @param <T>
     * @return
     */
    public static <T> Collection<T> reverse(Collection<T> initial) {

        List<T> list = new ArrayList<>(initial);
        Collections.reverse(list);

        return list;
    }
}
