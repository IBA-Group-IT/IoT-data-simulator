package com.iba.iot.datasimulator.common.service.schema.parser;

import com.iba.iot.datasimulator.common.model.schema.ArraySchema;
import com.iba.iot.datasimulator.common.model.schema.ObjectSchema;
import com.iba.iot.datasimulator.common.model.schema.Schema;
import com.iba.iot.datasimulator.common.model.schema.property.ArraySchemaProperty;
import com.iba.iot.datasimulator.common.model.schema.property.ObjectSchemaProperty;
import com.iba.iot.datasimulator.common.model.schema.property.SchemaProperty;
import com.iba.iot.datasimulator.common.model.schema.property.SchemaPropertyType;
import com.iba.iot.datasimulator.common.model.schema.property.metadata.SchemaPropertyMetadata;
import com.iba.iot.datasimulator.common.util.ParseUtil;
import com.iba.iot.datasimulator.common.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 *
 * @param <T>
 */
public abstract class CommonSchemaParser<T> implements SchemaParser<T> {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(CommonSchemaParser.class);

    @Value("${session.processing.json.path.delimiter}")
    private String jsonPropertyDelimiter;

    /**
     *
     * @param schemaProperty
     * @param result
     * @param position
     */
    protected abstract void processFlatPropertyValue(SchemaProperty schemaProperty, Collection<T> result, String position);

    /**
     *
     * @param objectSchemaProperty
     * @param result
     * @param position
     */
    protected void processEmptyObjectProperty(ObjectSchemaProperty objectSchemaProperty, Collection<T> result, String position) {}

    /**
     *
     * @param arraySchemaProperty
     * @param result
     * @param position
     */
    protected void processEmptyArrayProperty(ArraySchemaProperty arraySchemaProperty, Collection<T> result, String position) {}

    @Override
    public Collection<T> parse(Schema schema) {

        logger.debug(">>> JsonSessionSchemaParser: parsing json schema: {}", schema);

        List<T> result = new ArrayList<>();
        if (schema instanceof ObjectSchema) {

            ObjectSchema objectSchema = (ObjectSchema) schema;
            Map<String, SchemaProperty> properties = new HashMap<>(objectSchema.getProperties());
            parseProperties(properties, result, StringUtil.EMPTY_STRING);

        } else {

            ArraySchema arraySchema = (ArraySchema) schema;
            Collection<SchemaProperty> items = new ArrayList<>(arraySchema.getItems());
            parseProperties(items, result, StringUtil.EMPTY_STRING);
        }

        return result;
    }

    /**
     *
     * @param properties
     * @param result
     */
    private void parseProperties(Map<String, SchemaProperty> properties, Collection<T> result, String parentPosition) {

        for (Map.Entry<String, SchemaProperty> schemaPropertyEntry : properties.entrySet()) {

            SchemaProperty schemaProperty = schemaPropertyEntry.getValue();
            String position = getCurrentPosition(parentPosition, schemaProperty);

            SchemaPropertyType schemaPropertyType = schemaProperty.getType();
            switch(schemaPropertyType) {

                case OBJECT:
                    processObjectProperty((ObjectSchemaProperty) schemaProperty, result, position);
                    break;

                case ARRAY: {
                    processArrayProperty((ArraySchemaProperty) schemaProperty, result, position);
                    break;
                }

                default:
                    processFlatProperty(schemaProperty, result, position);
            }
        }
    }

    /**
     *
     * @param properties
     * @param result
     * @param parentPosition
     */
    private void parseProperties(Collection<SchemaProperty> properties, Collection<T> result, String parentPosition) {

        for (SchemaProperty property : properties) {

            SchemaPropertyType propertyType = property.getType();
            String entryPosition = property.getMetadata().getPosition();
            String position = ParseUtil.buildArrayPath(parentPosition, entryPosition);

            switch (propertyType) {

                case OBJECT: {
                    processObjectProperty((ObjectSchemaProperty) property, result, position);
                    break;
                }

                case ARRAY: {
                    processArrayProperty((ArraySchemaProperty) property, result, position);
                    break;
                }

                default:
                    processFlatProperty(property, result, position);
            }
        }
    }

    /**
     *
     * @param parentPosition
     * @param schemaProperty
     * @return
     */
    private String getCurrentPosition(String parentPosition, SchemaProperty schemaProperty) {

        SchemaPropertyMetadata metadata = schemaProperty.getMetadata();
        String position = metadata.getPosition();

        if (StringUtils.isEmpty(parentPosition)) {
            return position;
        } else {
            return parentPosition + jsonPropertyDelimiter + position;
        }
    }

    /**
     * @param schemaProperty
     * @param result
     * @param position
     */
    private void processFlatProperty(SchemaProperty schemaProperty, Collection<T> result, String position) {
        processFlatPropertyValue(schemaProperty, result, position);
    }

    /**
     * @param objectSchemaProperty
     * @param result
     * @param position
     */
    private void processObjectProperty(ObjectSchemaProperty objectSchemaProperty, Collection<T> result, String position) {

        Map<String, SchemaProperty> properties = objectSchemaProperty.getProperties();
        if (properties != null && !properties.isEmpty()) {
            parseProperties(properties, result, position);
        } else {
            processEmptyObjectProperty(objectSchemaProperty, result, position);
        }
    }

    /**
     *
     * @param arraySchemaProperty
     * @param result
     * @param position
     */
    private void processArrayProperty(ArraySchemaProperty arraySchemaProperty, Collection<T> result, String position) {

        Collection<SchemaProperty> values = arraySchemaProperty.getItems();
        if (values != null && !values.isEmpty()) {
            parseProperties(values, result, position);
        } else {
            processEmptyArrayProperty(arraySchemaProperty, result, position);
        }
    }
}
