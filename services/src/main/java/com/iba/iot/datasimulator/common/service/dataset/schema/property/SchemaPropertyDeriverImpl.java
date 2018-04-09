package com.iba.iot.datasimulator.common.service.dataset.schema.property;

import com.fasterxml.jackson.databind.JsonNode;
import com.iba.iot.datasimulator.common.factory.schema.property.SchemaPropertyBuilder;
import com.iba.iot.datasimulator.common.model.schema.property.SchemaProperty;
import com.iba.iot.datasimulator.common.service.parser.TypeParser;
import com.iba.iot.datasimulator.common.service.parser.TypeParsingResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SchemaPropertyDeriverImpl implements SchemaPropertyDeriver {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(SchemaPropertyDeriverImpl.class);

    /** **/
    private static final long LONG_PROPERTY_THRESHOLD = 10000000;

    @Autowired
    private SchemaPropertyBuilder propertyBuilder;

    @Autowired
    private TypeParser typeParser;

    @Override
    public SchemaProperty derive(String datasetValue, String position) {

        logger.debug(">>> Building schema property for dataset value {}", datasetValue);

        TypeParsingResult parsingResult = typeParser.parse(datasetValue);
        SchemaProperty schemaProperty = buildSchemaProperty(position, parsingResult);

        logger.debug(">>> Dataset value {} building schema property result: {}", datasetValue, schemaProperty);
        return schemaProperty;
    }

    @Override
    public SchemaProperty derive(JsonNode jsonNode, String position) {

        logger.debug(">>> Building schema property for json node value {}", jsonNode.asText());
        SchemaProperty schemaProperty = buildSchemaProperty(jsonNode, position);

        logger.debug(">>> Json node {} building schema property result: {}", jsonNode.asText(), schemaProperty);
        return schemaProperty;
    }

    /**
     *
     * @param position
     * @param parsingResult
     * @return
     */
    private SchemaProperty buildSchemaProperty(String position, TypeParsingResult parsingResult) {

        switch (parsingResult.getType()) {

            case BOOLEAN:
                return propertyBuilder.buildBooleanProperty(position);

            case INTEGER:
                return propertyBuilder.buildIntegerProperty(position);

            case LONG:
                return propertyBuilder.buildLongProperty(position);

            case DOUBLE:
                return propertyBuilder.buildDoubleProperty(position);

            case TIMESTAMP:
                return propertyBuilder.buildTimestampProperty(position, parsingResult.getFormat());

            case DATE:
                return propertyBuilder.buildDateProperty(position, parsingResult.getFormat());

            default:
                return propertyBuilder.buildStringProperty(position);
        }
    }

    /**
     *
     * @param jsonNode
     * @param position
     * @return
     */
    private SchemaProperty buildSchemaProperty(JsonNode jsonNode, String position) {

        if (jsonNode.isNull()) {

            // for all null json values we assume that this is string by default
            return propertyBuilder.buildStringProperty(position);

        } else if (jsonNode.isBoolean()) {

            return propertyBuilder.buildBooleanProperty(position);

        } else if (isLongProperty(jsonNode)) {

            return processLongJsonNode(jsonNode, position);

        } else if (jsonNode.isInt()) {

            return propertyBuilder.buildIntegerProperty(position);

        } else if (jsonNode.isDouble()) {

            return propertyBuilder.buildDoubleProperty(position);

        }

        // By default parsing as string
        return processStringJsonNode(jsonNode, position);
    }

    /**
     *
     * @param jsonNode
     * @return
     */
    private boolean isLongProperty(JsonNode jsonNode) {

        if (jsonNode.isLong() || jsonNode.isInt()) {
            long value = jsonNode.asLong();
            if (value >= LONG_PROPERTY_THRESHOLD) {
                return true;
            }
        }

        return false;
    }

    /**
     *
     * @param jsonNode
     * @param position
     * @return
     */
    private SchemaProperty processLongJsonNode(JsonNode jsonNode, String position) {

        long value = jsonNode.asLong();
        TypeParsingResult typeParsingResult = typeParser.parseLongAsTimestamp(value);
        if (typeParsingResult.isSucceed()) {
            return propertyBuilder.buildTimestampProperty(position, typeParsingResult.getFormat());
        } else {
            return propertyBuilder.buildLongProperty(position);
        }
    }

    /**
     *
     * @param jsonNode
     * @param position
     * @return
     */
    private SchemaProperty processStringJsonNode(JsonNode jsonNode, String position) {

        String value = jsonNode.asText();
        TypeParsingResult typeParsingResult = typeParser.parseStringAsDate(value);
        if (typeParsingResult.isSucceed()) {
            return propertyBuilder.buildDateProperty(position, typeParsingResult.getFormat());
        } else {
            return propertyBuilder.buildStringProperty(position);
        }
    }
}
