package com.iba.iot.datasimulator.definition.dao;

import com.iba.iot.datasimulator.definition.model.DataDefinition;
import com.iba.iot.datasimulator.definition.model.Dataset;

import java.util.Collection;
import java.util.List;

/**
 *
 */
public interface DatasetDatabaseDao {

    /**
     *
     * @param dataset
     */
    Dataset save(Dataset dataset);

    /**
     *
     * @return
     * @param name
     * @param type
     */
    Collection<Dataset> get(String name, String type);

    /**
     *
     * @param datasetId
     * @return
     */
    Dataset get(String datasetId);

    /**
     *
     * @param dataset
     * @return
     */
    Dataset update(Dataset dataset);

    /**
     *
     * @param datasetId
     * @return
     */
    void remove(String datasetId);

    /**
     *
     * @param datasetId
     * @return
     */
    List<DataDefinition> getLinkedDataDefinitions(String datasetId);
}
