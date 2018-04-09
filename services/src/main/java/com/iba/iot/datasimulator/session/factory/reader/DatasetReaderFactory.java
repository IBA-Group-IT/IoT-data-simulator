package com.iba.iot.datasimulator.session.factory.reader;

import com.iba.iot.datasimulator.common.model.schema.Schema;
import com.iba.iot.datasimulator.definition.model.Dataset;
import com.iba.iot.datasimulator.session.model.active.filter.DatasetFilter;
import com.iba.iot.datasimulator.session.service.active.processing.reader.DatasetReader;

/**
 *
 */
public interface DatasetReaderFactory {

    /**
     *
     * @param dataset
     * @return
     */
    DatasetReader<byte[]> buildByteDatasetReader(Dataset dataset);

    /**
     *
     * @param dataset
     * @return
     */
    DatasetReader<String> buildStringDatasetReader(Dataset dataset);

    /**
     *
     * @param dataset
     * @param datasetFilter
     * @param schema
     * @return
     */
    DatasetReader<String> buildStringDatasetReader(Dataset dataset, DatasetFilter datasetFilter, Schema schema);
}

