package com.iba.iot.datasimulator.common.service.schema.manager;

import com.iba.iot.datasimulator.common.model.schema.Schema;
import com.iba.iot.datasimulator.common.model.schema.property.description.SchemaPropertyDescription;
import com.iba.iot.datasimulator.definition.model.DataDefinition;
import com.iba.iot.datasimulator.definition.model.Dataset;

import java.io.IOException;
import java.util.Collection;

/**
 *
 */
public interface SchemaManager {

    /**
     *
     * @param schema
     * @return
     */
    Collection<SchemaPropertyDescription> getPropertiesDescription(Schema schema);

    /**
     *
     * @param dataset
     * @return
     */
    Schema deriveSchema(Dataset dataset) throws IOException;

    /**
     *
     * @param dataDefinition
     * @return
     */
    Schema populateSchemaDefaultRules(DataDefinition dataDefinition) throws IOException;
}
