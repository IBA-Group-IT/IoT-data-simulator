package com.iba.iot.datasimulator.session.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.iba.iot.datasimulator.common.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
public class JsonTraverserUtil {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(JsonTraverserUtil.class);

    /** **/
    private static final Pattern ARRAY_PATH_SEGMENT = Pattern.compile("\\[\\d+\\]");

    /** **/
    private static final Pattern ARRAY_BRACKETS_INDEX = Pattern.compile("\\d+");

    /** **/
    private static final JsonNodeFactory nodeFactory = JsonNodeFactory.instance;

    /**
     *
     * @param segment
     * @return
     */
    public static boolean isObjectPathSegment(String segment) {
        return !ARRAY_PATH_SEGMENT.matcher(segment).find();
    }

    /**
     *
     * @param segment
     * @return
     */
    public static boolean isArrayPathSegment(String segment) {
        return ARRAY_PATH_SEGMENT.matcher(segment).find();
    }

    /**
     *
     * @param segment
     * @return
     */
    public static boolean isRootArrayPathSegment(String segment) {
        return isArrayPathSegment(segment) && StringUtils.isEmpty(getArrayName(segment));
    }

    /**
     *
     * @param arrayPathSegment
     * Examples of array path segments:
     *   second[2][1]
     *   third[1][2][3]
     *   first[23]
     *
     * @return
     */
    public static String getArrayName(String arrayPathSegment) {

        int bracketIndex = arrayPathSegment.indexOf(StringUtil.OPENING_SQUARE_BRACKET);
        return arrayPathSegment.substring(0, bracketIndex);
    }

    /**
     * @param arrayPathSegment
     * Examples of array path segments:
     *   second[2][1]
     *   third[1][2][3]
     *   first[23]
     *
     * @return
     */
    public static Collection<Integer> getArrayIndexes(String arrayPathSegment) {

        Collection<Integer> indexes = new ArrayList<>();

        int bracketIndex = arrayPathSegment.indexOf(StringUtil.OPENING_SQUARE_BRACKET);
        String arrayBracketsPart = arrayPathSegment.substring(bracketIndex);

        Matcher matcher = ARRAY_BRACKETS_INDEX.matcher(arrayBracketsPart);
        while (matcher.find()) {

            /**
             * In path segments arrays indexes are started from 1, so, real array index should be decreased
             */

            indexes.add(Integer.valueOf(matcher.group()) - 1);
        }

        return indexes;
    }

    /**
     *
     * @param node
     * @param objectNodeProcessor
     * @param arrayNodeProcessor
     * @return
     */
    public static <T> T processContainerNode(JsonNode node,
                                             Function<ObjectNode, T> objectNodeProcessor,
                                             Function<ArrayNode, T> arrayNodeProcessor) {

        if (node instanceof ObjectNode) {
            return objectNodeProcessor.apply((ObjectNode) node);
        } else if (node instanceof ArrayNode) {
            return arrayNodeProcessor.apply((ArrayNode) node);
        }

        return null;
    }

    /**
     *
     * @param node
     * @param objectNodeHandler
     * @param arrayNodeHandler
     */
    public static void handleContainerNode(JsonNode node,
                                           Consumer<ObjectNode> objectNodeHandler,
                                           Consumer<ArrayNode> arrayNodeHandler) {

        if (node instanceof ObjectNode) {
            objectNodeHandler.accept((ObjectNode) node);
        } else if (node instanceof ArrayNode){
            arrayNodeHandler.accept((ArrayNode) node);
        }
    }

    /**
     *
     * @param parent
     * @param name
     * @param createIfNull
     * @return
     */
    public static ObjectNode getOrCreateObject(ObjectNode parent, String name, boolean createIfNull) {

        ObjectNode child = (ObjectNode) parent.get(name);
        if (child == null && createIfNull) {
            child = nodeFactory.objectNode();
            parent.set(name, child);
        }

        return child;
    }

    /**
     *
     * @param parent
     * @param index
     * @param createIfNull
     * @return
     */
    public static ObjectNode getOrCreateObject(ArrayNode parent, int index, boolean createIfNull) {

        ObjectNode child = (ObjectNode) parent.get(index);
        if (child == null && createIfNull) {
            child = nodeFactory.objectNode();
            parent.add(child);
        }

        return child;
    }

    /**
     *
     * @param parent
     * @param arrayName
     * @param createIfNull
     * @return
     */
    public static ArrayNode getOrCreateArray(ObjectNode parent, String arrayName, boolean createIfNull) {

        ArrayNode child = (ArrayNode) parent.get(arrayName);
        if (child == null && createIfNull) {
            child = nodeFactory.arrayNode();
            parent.set(arrayName, child);
        }
        return child;
    }

    /**
     *
     * @param parent
     * @param indexes
     * @param createIfNull
     * @return
     */
    public static ArrayNode processIntermediateArrays(ArrayNode parent, Collection<Integer> indexes, boolean createIfNull) {

        if (parent == null) {
            return null;
        }

        for (int index : indexes) {

            parent = JsonTraverserUtil.getOrCreateArray(parent, index, createIfNull);
            if (parent == null) {
                return null;
            }
        }

        return parent;
    }

    /**
     *
     * @param parent
     * @param index
     * @return
     */
    public static ArrayNode getOrCreateArray(ArrayNode parent, int index, boolean createIfNull) {

        ArrayNode child = (ArrayNode) parent.get(index);
        if (child == null && createIfNull) {

            child = nodeFactory.arrayNode();
            parent.add(child);
        }

        return child;
    }


    /**
     *
     * @param value
     * @return
     */
    public static JsonNode buildValueJsonNode(Object value) {

        if (value == null) {
            return nodeFactory.nullNode();
        }

        if (value instanceof Integer) {
            return nodeFactory.numberNode((Integer) value);
        }

        if (value instanceof Double) {
            return nodeFactory.numberNode((Double) value);
        }

        if (value instanceof Float) {
            return nodeFactory.numberNode((Float) value);
        }

        if (value instanceof Long) {
            return nodeFactory.numberNode((Long) value);
        }

        if (value instanceof Boolean) {
            return nodeFactory.booleanNode((Boolean) value);
        }

        if (value instanceof JsonNode) {
            return (JsonNode) value;
        }

        // Default primitive type in case cannot detect any specific one
        return nodeFactory.textNode(value.toString());
    }

    /**
     *
     * @param arrayNode
     * @param index
     * @return
     */
    public static String getArrayValue(ArrayNode arrayNode, int index) {

        if (arrayNode != null && arrayNode.size() > index) {
            return arrayNode.get(index).asText();
        }

        return null;
    }
}
