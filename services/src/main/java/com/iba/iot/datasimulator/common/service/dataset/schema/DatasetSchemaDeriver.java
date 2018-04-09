package com.iba.iot.datasimulator.common.service.dataset.schema;

import com.iba.iot.datasimulator.common.model.TypedEntity;
import com.iba.iot.datasimulator.common.model.schema.Schema;
import com.iba.iot.datasimulator.definition.model.DatasetType;

import java.io.IOException;

/**
 *
 */
public interface DatasetSchemaDeriver extends TypedEntity<DatasetType>{

    /**
     *
     * @param datasetEntry
     * @return
     */
    Schema derive(String datasetEntry) throws IOException;
}
