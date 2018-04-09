package com.iba.iot.datasimulator.session.service.active.processing.generator.schema.databind;

import com.iba.iot.datasimulator.common.model.TypedEntity;
import com.iba.iot.datasimulator.common.model.schema.SchemaRootMetadataType;

import java.io.IOException;
import java.util.Map;

/**
 *
 */
public interface DatasetEntryDeserializer extends TypedEntity<SchemaRootMetadataType> {

    /**
     *
     * @param datasetEntry
     * @return
     */
    Map<String, String> deserialize(String datasetEntry, boolean isDatasetProvided) throws IOException;
}
