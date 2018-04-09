package com.iba.iot.datasimulator.definition.service;

import com.iba.iot.datasimulator.common.model.schema.Schema;
import com.iba.iot.datasimulator.common.model.schema.property.description.SchemaPropertyDescription;
import com.iba.iot.datasimulator.definition.model.DataDefinition;
import com.iba.iot.datasimulator.definition.model.DataDefinitionCreateUpdateRequest;

import java.io.IOException;
import java.util.Collection;

public interface DataDefinitionManager {

    /**
     *
     * @param dataDefinitionCreateUpdateRequest
     * @return
     */
    DataDefinition create(DataDefinitionCreateUpdateRequest dataDefinitionCreateUpdateRequest);

    /**
     *
     * @param dataDefinition
     * @return
     */
    DataDefinition create(DataDefinition dataDefinition);

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
     * @return
     */
    Collection<SchemaPropertyDescription> getSchemaPropertiesDescription(String dataDefinitionId);

    /**
     *
     * @param dataDefinitionId
     * @return
     */
    Schema populateSchemaDefaultProcessingRules(String dataDefinitionId) throws IOException;

    /**
     *
     * @param dataDefinitionId
     */
    void remove(String dataDefinitionId);

    /**
     *
     *
     * @param dataDefinitionId@return
     */
    DataDefinition update(String dataDefinitionId, DataDefinitionCreateUpdateRequest dataDefinitionCreateUpdateRequest) throws IOException;
}
