package com.iba.iot.datasimulator.session.service.active.processing.reader.filter;

/**
 *
 */
public interface DatasetFilterProcessor<T> {

    /**
     *
     * @param datasetEntry
     * @return
     */
    boolean filter(T datasetEntry);

}
