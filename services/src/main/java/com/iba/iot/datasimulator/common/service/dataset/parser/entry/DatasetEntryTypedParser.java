package com.iba.iot.datasimulator.common.service.dataset.parser.entry;

import com.iba.iot.datasimulator.common.model.TypedEntity;
import com.iba.iot.datasimulator.common.model.schema.SchemaRootMetadataType;

import java.io.IOException;

/**
 *
 */
public interface DatasetEntryTypedParser extends TypedEntity<SchemaRootMetadataType> {

    /**
     *
     * @param datasetEntry
     * @param position
     * @return
     */
    String getValue(String datasetEntry, String position) throws IOException;

}
