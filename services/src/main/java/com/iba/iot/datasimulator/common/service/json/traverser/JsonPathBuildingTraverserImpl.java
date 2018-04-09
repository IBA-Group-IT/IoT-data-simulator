package com.iba.iot.datasimulator.common.service.json.traverser;

import com.fasterxml.jackson.databind.JsonNode;
import com.iba.iot.datasimulator.common.model.path.JsonPathSegments;
import com.iba.iot.datasimulator.common.util.JsonPathUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Possible paths examples:
 *   first
 *   second[1]/some/value
 *   second[2][1]
 *   first/second/third[1][2][3]/fourth
 */
@Component
public class JsonPathBuildingTraverserImpl implements JsonPathBuildingTraverser {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(JsonPathBuildingTraverserImpl.class);

    @Value("${session.processing.json.path.delimiter}")
    private String jsonPathDelimiter;

    @Autowired
    private JsonPathSegmentBuildingTraverser segmentTraverser;

    @Override
    public void build(String path, Object value, JsonNode root) {

        logger.debug(">>> Json traverser: setting value {} for path {}", value, path);
        JsonPathSegments jsonPathSegments = JsonPathUtil.splitJsonPath(path, jsonPathDelimiter);

        JsonNode parent = iterateIntermediateSegments(root, jsonPathSegments, true);
        segmentTraverser.buildValueNode(parent, jsonPathSegments.getTerminalSegment(), value);
    }

    @Override
    public String get(String path, JsonNode root) {

        logger.debug(">>> Json traverser: getting value for path {} from json node: {}", path, root.asText());
        JsonPathSegments jsonPathSegments = JsonPathUtil.splitJsonPath(path, jsonPathDelimiter);

        JsonNode parent = iterateIntermediateSegments(root, jsonPathSegments, false);
        return segmentTraverser.getValueNode(parent, jsonPathSegments.getTerminalSegment());
    }

    /**
     *
     * @param root
     * @param jsonPathSegments
     * @param createIfNull
     * @return
     */
    private JsonNode iterateIntermediateSegments(JsonNode root, JsonPathSegments jsonPathSegments, boolean createIfNull) {

        JsonNode parent = root;
        for (String pathSegment : jsonPathSegments.getIntermediateSegments()) {
            parent = segmentTraverser.nextNode(parent, pathSegment, createIfNull);
        }

        return parent;
    }
}
