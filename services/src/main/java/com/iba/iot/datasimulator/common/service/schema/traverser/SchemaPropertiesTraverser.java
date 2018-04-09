package com.iba.iot.datasimulator.common.service.schema.traverser;

import com.iba.iot.datasimulator.common.model.schema.Schema;
import com.iba.iot.datasimulator.common.model.schema.property.ArraySchemaProperty;
import com.iba.iot.datasimulator.common.model.schema.property.ObjectSchemaProperty;
import com.iba.iot.datasimulator.common.model.schema.property.SchemaProperty;

import java.util.function.Consumer;

/**
 *
 */
public interface SchemaPropertiesTraverser {

    /**
     * @param schema
     * @param plainPropertyConsumer
     */
    void traverseWithPlainPropertyConsumer(Schema schema, Consumer<SchemaProperty> plainPropertyConsumer);

    /**
     * @param schema
     * @param objectPropertyConsumer
     */
    void traverseWithObjectPropertyConsumer(Schema schema, Consumer<ObjectSchemaProperty> objectPropertyConsumer);

    /**
     * @param schema
     * @param arrayPropertyConsumer
     */
    void traverseWithArrayPropertyConsumer(Schema schema, Consumer<ArraySchemaProperty> arrayPropertyConsumer);

    /**
     * @param schema
     * @param plainPropertyConsumer
     * @param objectPropertyConsumer
     * @param arrayPropertyConsumer
     */
    void traverse(Schema schema, Consumer<SchemaProperty> plainPropertyConsumer,
                  Consumer<ObjectSchemaProperty> objectPropertyConsumer, Consumer<ArraySchemaProperty> arrayPropertyConsumer);

}
