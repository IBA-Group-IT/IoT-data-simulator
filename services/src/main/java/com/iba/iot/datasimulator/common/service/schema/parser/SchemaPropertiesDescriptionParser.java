package com.iba.iot.datasimulator.common.service.schema.parser;

import com.iba.iot.datasimulator.common.model.schema.property.ArraySchemaProperty;
import com.iba.iot.datasimulator.common.model.schema.property.ObjectSchemaProperty;
import com.iba.iot.datasimulator.common.model.schema.property.SchemaProperty;
import com.iba.iot.datasimulator.common.model.schema.property.description.SchemaPropertyDescription;
import com.iba.iot.datasimulator.common.model.schema.property.metadata.SchemaPropertyMetadata;
import com.iba.iot.datasimulator.common.model.schema.property.metadata.TypedSchemaPropertyMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class SchemaPropertiesDescriptionParser extends CommonSchemaParser<SchemaPropertyDescription> {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(SchemaPropertiesDescriptionParser.class);

    @Override
    protected void processFlatPropertyValue(SchemaProperty schemaProperty, Collection<SchemaPropertyDescription> result, String position) {
        processProperty(schemaProperty, result, position);
    }

    @Override
    protected void processEmptyObjectProperty(ObjectSchemaProperty objectSchemaProperty, Collection<SchemaPropertyDescription> result, String position) {
        processProperty(objectSchemaProperty, result, position);
    }

    @Override
    protected void processEmptyArrayProperty(ArraySchemaProperty arraySchemaProperty, Collection<SchemaPropertyDescription> result, String position) {
        processProperty(arraySchemaProperty, result, position);
    }

    /**
     *
     * @param schemaProperty
     * @param result
     * @param position
     */
    private void processProperty(SchemaProperty schemaProperty, Collection<SchemaPropertyDescription> result, String position) {

        logger.debug(">>> Processing schema property {} for position {}", schemaProperty, position);
        String type = derivePropertyType(schemaProperty);
        result.add(new SchemaPropertyDescription(position, type));
    }

    /**
     *
     * @param schemaProperty
     * @return
     */
    private String derivePropertyType(SchemaProperty schemaProperty) {

        SchemaPropertyMetadata metadata = schemaProperty.getMetadata();
        if (metadata instanceof TypedSchemaPropertyMetadata) {

            TypedSchemaPropertyMetadata typedMetadata = (TypedSchemaPropertyMetadata) metadata;
            return typedMetadata.getType().toString();
        }

        return schemaProperty.getType().toString();
    }
}
