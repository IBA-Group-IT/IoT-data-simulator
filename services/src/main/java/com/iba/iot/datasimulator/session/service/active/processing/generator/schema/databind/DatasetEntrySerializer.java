package com.iba.iot.datasimulator.session.service.active.processing.generator.schema.databind;

import com.iba.iot.datasimulator.common.model.TypedEntity;
import com.iba.iot.datasimulator.common.model.schema.SchemaRootMetadataType;
import com.iba.iot.datasimulator.common.model.schema.SchemaType;

import java.util.Map;

/**
 *
 */
public interface DatasetEntrySerializer extends TypedEntity<SchemaRootMetadataType> {

    /**
     *
     * @param processingResults
     * @param schemaType
     * @return
     */
    String serialize(Map<String, Object> processingResults, SchemaType schemaType);
}
