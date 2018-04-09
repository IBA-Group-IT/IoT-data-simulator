package com.iba.iot.datasimulator.common.service.dataset.parser.entry;

import com.iba.iot.datasimulator.common.model.schema.SchemaRootMetadataType;

import java.io.IOException;

/**
 *
 */
public interface DatasetEntryParser {

    /**
     *
     * @param datasetEntry
     * @param position
     * @param schemaType
     * @return
     * @throws IOException
     */
    String getValue(String datasetEntry, String position, SchemaRootMetadataType schemaType) throws IOException;
}
