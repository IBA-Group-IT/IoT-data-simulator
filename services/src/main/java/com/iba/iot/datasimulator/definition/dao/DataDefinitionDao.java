package com.iba.iot.datasimulator.definition.dao;

import com.iba.iot.datasimulator.definition.model.DataDefinition;
import com.iba.iot.datasimulator.session.model.Session;

import java.util.Collection;
import java.util.List;

/**
 *
 */
public interface DataDefinitionDao {

    /**
     *
     * @param dataDefinition
     * @return
     */
    DataDefinition save(DataDefinition dataDefinition);

    /**
     *
     * @return
     */
    Collection<DataDefinition> get();

    /**
     *
     * @param dataDefinitionId
     * @return
     */
    DataDefinition get(String dataDefinitionId);

    /**
     *
     * @param dataDefinitionId
     */
    void remove(String dataDefinitionId);

    /**
     *
     * @param datasetId
     * @return
     */
    int getDatasetReferencesCounter(String datasetId);


    /**
     *
     * @param dataDefinition
     * @return
     */
    DataDefinition update(DataDefinition dataDefinition);

    /**
     *
     * @param dataDefinitionId
     * @return
     */
    List<Session> getLinkedSessions(String dataDefinitionId);
}
