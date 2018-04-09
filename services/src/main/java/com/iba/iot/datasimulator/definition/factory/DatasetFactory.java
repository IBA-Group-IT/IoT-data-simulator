package com.iba.iot.datasimulator.definition.factory;

import com.iba.iot.datasimulator.definition.model.Dataset;
import com.iba.iot.datasimulator.definition.model.DatasetType;

/**
 *
 */
public interface DatasetFactory {

    /**
     *
     * @param fileName
     * @param datasetType
     * @return
     */
    Dataset buildFromUploadRequest(String fileName, DatasetType datasetType);
}
