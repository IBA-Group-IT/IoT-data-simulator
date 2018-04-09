package com.iba.iot.datasimulator.common.factory.schema.property;

import com.iba.iot.datasimulator.common.factory.schema.property.metadata.SchemaPropertyMetadataBuilder;
import com.iba.iot.datasimulator.common.model.TimestampType;
import com.iba.iot.datasimulator.common.model.schema.property.ArraySchemaPropertyModel;
import com.iba.iot.datasimulator.common.model.schema.property.ObjectSchemaPropertyModel;
import com.iba.iot.datasimulator.common.model.schema.property.SchemaPropertyModel;
import com.iba.iot.datasimulator.common.model.schema.property.SchemaPropertyType;
import com.iba.iot.datasimulator.common.model.schema.property.metadata.FormattedTypedSchemaPropertyMetadataModel;
import com.iba.iot.datasimulator.common.model.schema.property.metadata.SchemaPropertyMetadataModel;
import com.iba.iot.datasimulator.common.model.schema.property.metadata.SchemaPropertyMetadataType;
import com.iba.iot.datasimulator.common.model.schema.property.metadata.TypedSchemaPropertyMetadataModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;

@Service
public class SchemaPropertyBuilderImpl implements SchemaPropertyBuilder {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(SchemaPropertyBuilderImpl.class);

    @Autowired
    private SchemaPropertyMetadataBuilder metadataBuilder;

    @Override
    public ArraySchemaPropertyModel buildArrayProperty(String position) {

        logger.debug(">>> Building array property for position {}", position);

        ArraySchemaPropertyModel property = new ArraySchemaPropertyModel(SchemaPropertyType.ARRAY);
        SchemaPropertyMetadataModel metadata = metadataBuilder.buildArrayMetadata(position);
        property.setMetadata(metadata);
        property.setItems(new ArrayList<>());

        return property;
    }

    @Override
    public ObjectSchemaPropertyModel buildObjectProperty(String position) {

        logger.debug(">>> Building object property for position {}", position);

        ObjectSchemaPropertyModel property = new ObjectSchemaPropertyModel(SchemaPropertyType.OBJECT);
        SchemaPropertyMetadataModel metadata = metadataBuilder.buildObjectMetadata(position);
        property.setMetadata(metadata);
        property.setProperties(new LinkedHashMap<>());

        return property;
    }

    @Override
    public SchemaPropertyModel buildStringProperty(String position) {

        logger.debug(">>> Building string property for position {}", position);

        SchemaPropertyMetadataModel metadata = metadataBuilder.buildMetadata(position);
        return new SchemaPropertyModel(SchemaPropertyType.STRING, metadata);
    }

    @Override
    public SchemaPropertyModel buildBooleanProperty(String position) {

        logger.debug(">>> Building boolean property for position {}", position);

        SchemaPropertyMetadataModel metadata = metadataBuilder.buildMetadata(position);
        return new SchemaPropertyModel(SchemaPropertyType.BOOLEAN, metadata);
    }

    @Override
    public SchemaPropertyModel buildIntegerProperty(String position) {

        logger.debug(">>> Building integer property for position {}", position);
        return buildNumberProperty(position, SchemaPropertyMetadataType.INTEGER);
    }

    @Override
    public SchemaPropertyModel buildLongProperty(String position) {

        logger.debug(">>> Building long property for position {}", position);
        return buildNumberProperty(position, SchemaPropertyMetadataType.LONG);
    }

    @Override
    public SchemaPropertyModel buildDoubleProperty(String position) {

        logger.debug(">>> Building double property for position {}", position);
        return buildNumberProperty(position, SchemaPropertyMetadataType.DOUBLE);
    }

    @Override
    public SchemaPropertyModel buildDateProperty(String position, String format) {

        logger.debug(">>> Building date property for format {} and position {}", format, position);

        FormattedTypedSchemaPropertyMetadataModel metadata =
                metadataBuilder.buildTypedFormattedMetadata(position, SchemaPropertyMetadataType.DATE, format);
        return new SchemaPropertyModel(SchemaPropertyType.STRING, metadata);
    }

    @Override
    public SchemaPropertyModel buildTimestampProperty(String position, String format) {

        logger.debug(">>> Building timestamp property for format {} and position {}", format, position);


        if (TimestampType.fromString(format) == null) {

            logger.error(">>> Couldn't build timestamp property for wrong provided format {}", format);
            throw new RuntimeException("Timestamp property building error due to wrong format value.");
        }

        FormattedTypedSchemaPropertyMetadataModel metadata =
                metadataBuilder.buildTypedFormattedMetadata(position, SchemaPropertyMetadataType.TIMESTAMP, format);
        return new SchemaPropertyModel(SchemaPropertyType.NUMBER, metadata);
    }

    /**
     *
     * @param position
     * @param type
     * @return
     */
    private SchemaPropertyModel buildNumberProperty(String position, SchemaPropertyMetadataType type) {

        TypedSchemaPropertyMetadataModel metadata = metadataBuilder.buildTypedMetadata(position, type);
        return new SchemaPropertyModel(SchemaPropertyType.NUMBER, metadata);
    }
}
