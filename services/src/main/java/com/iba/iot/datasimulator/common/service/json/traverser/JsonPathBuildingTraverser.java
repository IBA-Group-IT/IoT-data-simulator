package com.iba.iot.datasimulator.common.service.json.traverser;

import com.fasterxml.jackson.databind.JsonNode;

/**
 *
 */
public interface JsonPathBuildingTraverser {

    /**
     *  @param path
     * @param value
     * @param root
     */
    void build(String path, Object value, JsonNode root);

    /**
     *
     * @param path
     * @param root
     * @return
     */
    String get(String path, JsonNode root);

}
