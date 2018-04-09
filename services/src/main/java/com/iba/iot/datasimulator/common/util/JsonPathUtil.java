package com.iba.iot.datasimulator.common.util;

import com.iba.iot.datasimulator.common.model.path.ArraySegment;
import com.iba.iot.datasimulator.common.model.path.JsonPathSegments;
import com.iba.iot.datasimulator.session.util.JsonTraverserUtil;

import java.util.Arrays;
import java.util.LinkedList;

/**
 *
 */
public class JsonPathUtil {

    /**
     *
     * @param path
     * @param jsonPathDelimiter
     * @return
     */
    public static JsonPathSegments splitJsonPath(String path, String jsonPathDelimiter) {

        LinkedList<String> pathSegments = new LinkedList<>(Arrays.asList(path.split(jsonPathDelimiter)));
        String terminalSegment = pathSegments.pollLast();

        return new JsonPathSegments(pathSegments, terminalSegment);
    }

    /**
     *
     * @param arrayPathSegment
     * @return
     */
    public static ArraySegment splitArraySegment(String arrayPathSegment) {

        String arrayName = JsonTraverserUtil.getArrayName(arrayPathSegment);
        LinkedList<Integer> indexes = new LinkedList<>(JsonTraverserUtil.getArrayIndexes(arrayPathSegment));
        int lastIndex = indexes.pollLast();

        return new ArraySegment(arrayName, indexes, lastIndex);
    }

}
