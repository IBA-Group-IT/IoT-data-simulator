package com.iba.iot.datasimulator.definition.factory;

import com.iba.iot.datasimulator.definition.model.DataDefinition;
import com.iba.iot.datasimulator.definition.model.DataDefinitionCreateUpdateRequest;

/**
 *
 */
public interface DataDefinitionFactory {

    /**
     *
     * @param dataDefinitionCreateUpdateRequest
     * @param dataDefinitionId
     * @return
     */
    DataDefinition buildFromCreateUpdateRequest(DataDefinitionCreateUpdateRequest dataDefinitionCreateUpdateRequest, String dataDefinitionId);
}
