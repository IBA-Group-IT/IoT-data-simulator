package com.iba.iot.datasimulator.common.factory.schema.property.metadata;

import com.iba.iot.datasimulator.common.model.schema.property.metadata.FormattedTypedSchemaPropertyMetadataModel;
import com.iba.iot.datasimulator.common.model.schema.property.metadata.SchemaPropertyMetadataModel;
import com.iba.iot.datasimulator.common.model.schema.property.metadata.SchemaPropertyMetadataType;
import com.iba.iot.datasimulator.common.model.schema.property.metadata.TypedSchemaPropertyMetadataModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SchemaPropertyMetadataBuilderImpl implements SchemaPropertyMetadataBuilder {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(SchemaPropertyMetadataBuilderImpl.class);

    @Value("${dataset.json.schema.property.postfix}")
    private String schemaPropertyNamePostfix;

    @Override
    public SchemaPropertyMetadataModel buildMetadata(String position) {

        logger.debug(">>> Building metadata with position {}", position);
        return new SchemaPropertyMetadataModel(position, position + schemaPropertyNamePostfix);
    }

    @Override
    public SchemaPropertyMetadataModel buildArrayMetadata(String position) {

        logger.debug(">>> Building array metadata with position {}", position);
        return new SchemaPropertyMetadataModel(position);
    }

    @Override
    public SchemaPropertyMetadataModel buildObjectMetadata(String position) {

        logger.debug(">>> Building object metadata with position {}", position);
        return new SchemaPropertyMetadataModel(position);
    }

    @Override
    public TypedSchemaPropertyMetadataModel buildTypedMetadata(String position, SchemaPropertyMetadataType type) {

        logger.debug(">>> Building typed metadata with type {} and position {}", type, position);
        return new TypedSchemaPropertyMetadataModel(position, position + schemaPropertyNamePostfix, type);
    }

    @Override
    public FormattedTypedSchemaPropertyMetadataModel buildTypedFormattedMetadata(String position, SchemaPropertyMetadataType type, String format) {

        logger.debug(">>> Building typed metadata with type {} and position {}", type, position);
        return new FormattedTypedSchemaPropertyMetadataModel(position, position + schemaPropertyNamePostfix, type, format);
    }
}
