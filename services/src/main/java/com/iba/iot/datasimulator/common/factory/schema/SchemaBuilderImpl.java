package com.iba.iot.datasimulator.common.factory.schema;

import com.iba.iot.datasimulator.common.model.schema.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;

@Service
public class SchemaBuilderImpl implements SchemaBuilder {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(SchemaBuilderImpl.class);

    @Override
    public ArraySchemaModel buildArraySchema(SchemaRootMetadataType type) {

        logger.debug(">>> Building array schema with metadata type {}", type);

        ArraySchemaModel schema = new ArraySchemaModel(SchemaType.ARRAY, new ArrayList<>());
        SchemaRootMetadata schemaRootMetadata = new SchemaRootMetadata(type);
        schema.setMetadata(schemaRootMetadata);

        return schema;
    }

    @Override
    public ObjectSchemaModel buildObjectSchema(SchemaRootMetadataType type) {

        logger.debug(">>> Building object schema with metadata type {}", type);

        ObjectSchemaModel schema = new ObjectSchemaModel(SchemaType.OBJECT, new LinkedHashMap<>());
        SchemaRootMetadata schemaRootMetadata = new SchemaRootMetadata(type);
        schema.setMetadata(schemaRootMetadata);

        return schema;
    }
}
