package com.iba.iot.datasimulator.session.factory.filter;

import com.iba.iot.datasimulator.common.model.schema.Schema;
import com.iba.iot.datasimulator.session.model.active.filter.DatasetFilter;
import com.iba.iot.datasimulator.session.service.active.processing.reader.filter.DatasetFilterProcessor;

/**
 *
 */
public interface DatasetFilterProcessorFactory {

    /**
     *
     * @param datasetFilter
     * @param schema
     * @return
     */
    DatasetFilterProcessor<String> build(DatasetFilter datasetFilter, Schema schema);
}
