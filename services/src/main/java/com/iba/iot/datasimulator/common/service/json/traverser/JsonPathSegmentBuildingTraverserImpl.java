package com.iba.iot.datasimulator.common.service.json.traverser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.iba.iot.datasimulator.common.model.path.ArraySegment;
import com.iba.iot.datasimulator.common.util.JsonPathUtil;
import com.iba.iot.datasimulator.session.util.JsonTraverserUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class JsonPathSegmentBuildingTraverserImpl implements JsonPathSegmentBuildingTraverser {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(JsonPathSegmentBuildingTraverserImpl.class);

    @Override
    public JsonNode nextNode(JsonNode parent, String pathSegment, boolean createIfNull) {

        return JsonTraverserUtil.processContainerNode(
            parent,
            objectNodeParent -> nextNodeForObjectParent(objectNodeParent, pathSegment, createIfNull),
            arrayNodeParent -> nextNodeForArrayParent(arrayNodeParent, pathSegment, createIfNull)
        );
    }

    @Override
    public void buildValueNode(JsonNode parent, String pathSegment, Object value) {

        JsonNode valueNode = JsonTraverserUtil.buildValueJsonNode(value);
        JsonTraverserUtil.handleContainerNode(
            parent,
            objectNodeParent -> buildValueNodeForObjectParent(objectNodeParent, pathSegment, valueNode),
            arrayNodeParent -> buildValueNodeForArrayParent(arrayNodeParent, pathSegment, valueNode)
        );
    }

    @Override
    public String getValueNode(JsonNode parent, String pathSegment) {

        return JsonTraverserUtil.processContainerNode(
            parent,
            objectNodeParent -> getValueNodeForObjectParent(pathSegment, objectNodeParent),
            arrayNodeParent -> getValueNodeForArrayParent(pathSegment, arrayNodeParent)
        );
    }

    /**
     *
     * @param parent
     * @param pathSegment
     * @param createIfNull
     * @return
     */
    private JsonNode nextNodeForObjectParent(ObjectNode parent, String pathSegment, boolean createIfNull) {

        if (JsonTraverserUtil.isObjectPathSegment(pathSegment)) {
            return processObjectPathSegmentForObjectParent(parent, pathSegment, createIfNull);
        } else {
            return processArrayPathSegmentForObjectParent(parent, pathSegment, createIfNull);
        }
    }

    /**
     *
     * @param parent
     * @param pathSegment
     * @param createIfNull
     * @return
     */
    private JsonNode nextNodeForArrayParent(ArrayNode parent, String pathSegment,  boolean createIfNull) {

        // the only possible use case here is root empty array node as parent
        // and first array segment without name. Examples:

        // [1]
        // [23][33][234]

        if (JsonTraverserUtil.isRootArrayPathSegment(pathSegment)) {

            ArraySegment arraySegment = JsonPathUtil.splitArraySegment(pathSegment);
            ArrayNode array = JsonTraverserUtil.processIntermediateArrays(parent, arraySegment.getIntermediateArrayIndexes(), createIfNull);
            return JsonTraverserUtil.getOrCreateObject(array, arraySegment.getLastArrayIndex(), createIfNull);
        }

        return processError(parent, pathSegment);
    }


    /**
     *
     * @param parent
     * @param pathSegment
     * @return
     */
    private JsonNode processObjectPathSegmentForObjectParent(ObjectNode parent, String pathSegment, boolean createIfNull) {
        return JsonTraverserUtil.getOrCreateObject(parent, pathSegment, createIfNull);
    }

    /**
     *  Array path segment examples:
     *
     *  first[1]
     *  second[3][2][2]
     *  third[56][2]
     *
     * @param parent
     * @param pathSegment
     * @return
     */
    private JsonNode processArrayPathSegmentForObjectParent(ObjectNode parent, String pathSegment, boolean createIfNull) {

        ArraySegment arraySegment = JsonPathUtil.splitArraySegment(pathSegment);

        ArrayNode array = JsonTraverserUtil.getOrCreateArray(parent, arraySegment.getArrayName(), createIfNull);
        array = JsonTraverserUtil.processIntermediateArrays(array, arraySegment.getIntermediateArrayIndexes(), createIfNull);
        return JsonTraverserUtil.getOrCreateObject(array, arraySegment.getLastArrayIndex(), createIfNull);
    }


    /**
     *
     * @param parent
     * @param pathSegment
     * @param valueNode
     */
    private void buildValueNodeForObjectParent(ObjectNode parent, String pathSegment, JsonNode valueNode) {

        if (JsonTraverserUtil.isObjectPathSegment(pathSegment)) {
            buildObjectSegmentValueForObjectParent(parent, pathSegment, valueNode);
        } else {
            buildArraySegmentValueForObjectParent(parent, pathSegment, valueNode);
        }
    }

    /**
     *
     * @param parent
     * @param pathSegment
     * @param valueNode
     */
    private void buildObjectSegmentValueForObjectParent(ObjectNode parent, String pathSegment, JsonNode valueNode) {
        parent.set(pathSegment, valueNode);
    }

    /**
     *
     * @param parent
     * @param pathSegment
     * @param valueNode
     */
    private void buildArraySegmentValueForObjectParent(ObjectNode parent, String pathSegment, JsonNode valueNode) {

        ArraySegment arraySegment = JsonPathUtil.splitArraySegment(pathSegment);

        ArrayNode child = JsonTraverserUtil.getOrCreateArray(parent, arraySegment.getArrayName(), true);
        child = JsonTraverserUtil.processIntermediateArrays(child, arraySegment.getIntermediateArrayIndexes(), true);
        child.add(valueNode);
    }

    /**
     *
     * @param parent
     * @param pathSegment
     * @param valueNode
     */
    private void buildValueNodeForArrayParent(ArrayNode parent, String pathSegment, JsonNode valueNode) {

        if (JsonTraverserUtil.isRootArrayPathSegment(pathSegment)) {

            ArraySegment arraySegment = JsonPathUtil.splitArraySegment(pathSegment);
            ArrayNode array = JsonTraverserUtil.processIntermediateArrays(parent, arraySegment.getIntermediateArrayIndexes(), true);
            array.add(valueNode);

        } else {
            processError(parent, pathSegment);
        }
    }

    /**
     *
     * @param pathSegment
     * @param objectNodeParent
     * @return
     */
    private String getValueNodeForObjectParent(String pathSegment, ObjectNode objectNodeParent) {

        if (JsonTraverserUtil.isObjectPathSegment(pathSegment)) {
            return getObjectSegmentValueForObjectParent(pathSegment, objectNodeParent);
        } else {
            return getArraySegmentValueForObjectParent(pathSegment, objectNodeParent);
        }
    }

    /**
     *
     * @param pathSegment
     * @param objectNodeParent
     * @return
     */
    private String getObjectSegmentValueForObjectParent(String pathSegment, ObjectNode objectNodeParent) {
        return objectNodeParent.get(pathSegment).asText();
    }

    /**
     *
     * @param pathSegment
     * @param objectNodeParent
     * @return
     */
    private String getArraySegmentValueForObjectParent(String pathSegment, ObjectNode objectNodeParent) {

        ArraySegment arraySegment = JsonPathUtil.splitArraySegment(pathSegment);

        ArrayNode arrayNode = JsonTraverserUtil.getOrCreateArray(objectNodeParent, arraySegment.getArrayName(), false);
        arrayNode = JsonTraverserUtil.processIntermediateArrays(arrayNode, arraySegment.getIntermediateArrayIndexes(), true);
        return JsonTraverserUtil.getArrayValue(arrayNode, arraySegment.getLastArrayIndex());
    }

    /**
     *
     * @param pathSegment
     * @param arrayNodeParent
     * @return
     */
    private String getValueNodeForArrayParent(String pathSegment, ArrayNode arrayNodeParent) {

        if (JsonTraverserUtil.isRootArrayPathSegment(pathSegment)) {

            ArraySegment arraySegment = JsonPathUtil.splitArraySegment(pathSegment);
            ArrayNode arrayNode = JsonTraverserUtil.processIntermediateArrays(arrayNodeParent, arraySegment.getIntermediateArrayIndexes(), false);
            return JsonTraverserUtil.getArrayValue(arrayNode, arraySegment.getLastArrayIndex());
        }

        return null;
    }

    /**
     *
     * @param parent
     * @param pathSegment
     * @return
     */
    private JsonNode processError(JsonNode parent, String pathSegment) {

        logger.error(">>> Json traversing error for parent node {} and path segment: {}", parent.asText(), pathSegment);
        throw new RuntimeException("Json traversing error");
    }
}
