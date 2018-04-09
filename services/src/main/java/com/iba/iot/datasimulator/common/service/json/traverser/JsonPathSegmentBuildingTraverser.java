package com.iba.iot.datasimulator.common.service.json.traverser;

import com.fasterxml.jackson.databind.JsonNode;

/**
 *
 */
public interface JsonPathSegmentBuildingTraverser {

    /**
     *
     * @param parent
     * @param pathSegment
     * @return
     */
    JsonNode nextNode(JsonNode parent, String pathSegment, boolean createIfNull);

    /**
     *
     * @param parent
     * @param pathSegment
     * @param value
     * @return
     */
    void buildValueNode(JsonNode parent, String pathSegment, Object value);

    /**
     *
     * @param parent
     * @param pathSegment
     */
    String getValueNode(JsonNode parent, String pathSegment);
}
