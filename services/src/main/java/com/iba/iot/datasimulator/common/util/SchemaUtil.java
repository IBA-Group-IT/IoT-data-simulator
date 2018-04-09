package com.iba.iot.datasimulator.common.util;

import com.iba.iot.datasimulator.common.model.schema.ArraySchema;
import com.iba.iot.datasimulator.common.model.schema.ObjectSchema;
import com.iba.iot.datasimulator.common.model.schema.Schema;

import java.util.function.Consumer;

/**
 *
 */
public class SchemaUtil {

    /**
     *
     * @param schema
     * @param objectSchemaConsumer
     * @param arraySchemaConsumer
     */
    public static void processSchema(Schema schema,
                                     Consumer<ObjectSchema> objectSchemaConsumer,
                                     Consumer<ArraySchema> arraySchemaConsumer) {

        if (schema instanceof ObjectSchema) {
            objectSchemaConsumer.accept((ObjectSchema) schema);
        } else {
            arraySchemaConsumer.accept((ArraySchema) schema);
        }
    }
}
