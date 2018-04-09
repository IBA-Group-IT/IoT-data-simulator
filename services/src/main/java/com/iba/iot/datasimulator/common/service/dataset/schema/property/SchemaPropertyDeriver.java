package com.iba.iot.datasimulator.common.service.dataset.schema.property;

import com.fasterxml.jackson.databind.JsonNode;
import com.iba.iot.datasimulator.common.model.schema.property.SchemaProperty;

/**
 *
 */
public interface SchemaPropertyDeriver {

    /**
     *
     * @param datasetValue
     * @param position
     * @return
     */
    SchemaProperty derive(String datasetValue, String position);

    /**
     *
     * @param jsonNode
     * @param position
     * @return
     */
    SchemaProperty derive(JsonNode jsonNode, String position);
}
