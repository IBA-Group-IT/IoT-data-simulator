package com.iba.iot.datasimulator.session.service.active.processing.generator.schema.processor.convertor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iba.iot.datasimulator.common.model.schema.property.SchemaPropertyType;
import com.iba.iot.datasimulator.common.model.schema.property.SessionSchemaProperty;
import com.iba.iot.datasimulator.common.model.schema.property.metadata.SchemaPropertyMetadataType;
import com.iba.iot.datasimulator.common.model.schema.property.rule.SchemaPropertyRuleType;
import com.iba.iot.datasimulator.common.service.json.interop.JavascriptJSONWrapper;
import com.iba.iot.datasimulator.common.util.SchemaPropertyUtil;
import com.iba.iot.datasimulator.session.model.unfold.UnfoldedSessionSchemaProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;

@Component
public class DatasetEntryValueConverterImpl implements DatasetEntryValueConverter {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(DatasetEntryValueConverterImpl.class);

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private JavascriptJSONWrapper jsonWrapper;

    @Override
    public Object convertToOriginalType(UnfoldedSessionSchemaProperty unfoldedSchemaProperty, String datasetEntryValue) throws IOException {

        logger.debug(">>> Converting dataset entry value: {} to original type for schema property: {}", datasetEntryValue, datasetEntryValue);

        if (StringUtils.isEmpty(datasetEntryValue)) {
            return null;
        }

        SessionSchemaProperty property = unfoldedSchemaProperty.getProperty();
        SchemaPropertyType propertyType = property.getType();
        SchemaPropertyRuleType ruleType = property.getRule().getType();

        if (propertyType == SchemaPropertyType.STRING) {
            return datasetEntryValue;
        } else if (propertyType == SchemaPropertyType.ARRAY || propertyType == SchemaPropertyType.OBJECT) {

            if (ruleType == SchemaPropertyRuleType.CUSTOM_FUNCTION) {
                return jsonWrapper.parse(datasetEntryValue);
            } else {
                return mapper.readValue(datasetEntryValue, JsonNode.class);
            }

        } else if (propertyType == SchemaPropertyType.BOOLEAN) {
            return Boolean.parseBoolean(datasetEntryValue);
        } else {

            SchemaPropertyMetadataType propertyMetadataType = SchemaPropertyUtil.getPropertyMetadataType(property.getMetadata());
            if (propertyMetadataType == SchemaPropertyMetadataType.INTEGER) {
                return Integer.parseInt(datasetEntryValue);
            } else if (propertyMetadataType == SchemaPropertyMetadataType.DOUBLE) {
                return Double.parseDouble(datasetEntryValue);
            } else {
                return Long.parseLong(datasetEntryValue);
            }
        }
    }
}
