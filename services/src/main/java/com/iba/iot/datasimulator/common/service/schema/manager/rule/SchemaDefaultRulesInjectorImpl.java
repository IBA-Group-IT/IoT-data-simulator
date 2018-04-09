package com.iba.iot.datasimulator.common.service.schema.manager.rule;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iba.iot.datasimulator.common.factory.schema.property.rule.SchemaPropertyRuleBuilder;
import com.iba.iot.datasimulator.common.model.schema.Schema;
import com.iba.iot.datasimulator.common.model.schema.property.SessionArraySchemaProperty;
import com.iba.iot.datasimulator.common.model.schema.property.SessionObjectSchemaProperty;
import com.iba.iot.datasimulator.common.model.schema.property.SessionSchemaProperty;
import com.iba.iot.datasimulator.common.service.schema.parser.SchemaParser;
import com.iba.iot.datasimulator.common.service.schema.traverser.SchemaPropertiesTraverser;
import com.iba.iot.datasimulator.common.util.SchemaPropertyUtil;
import com.iba.iot.datasimulator.common.util.StringUtil;
import com.iba.iot.datasimulator.session.model.unfold.UnfoldedSchemaProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.stream.Collectors;

@Component
public class SchemaDefaultRulesInjectorImpl implements SchemaDefaultRulesInjector {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(SchemaDefaultRulesInjectorImpl.class);

    @Autowired
    private SchemaPropertiesTraverser schemaPropertiesTraverser;

    @Autowired
    private SchemaPropertyRuleBuilder schemaPropertyRuleBuilder;

    @Autowired
    private SchemaParser<UnfoldedSchemaProperty> schemaParser;

    @Value("${session.processing.json.path.delimiter}")
    private String propertyPathDelimiter;

    @Autowired
    private ObjectMapper mapper;

    @Override
    public Schema injectDefaultRules(Schema schema, boolean isDatasetProvided) throws IOException {

        Schema sessionSchema = cloneAsSessionSchema(schema);
        if (isDatasetProvided) {
            populatePropertyRuleWithDataset(sessionSchema);
        } else {
            populatePropertyRuleWithoutDataset(sessionSchema);
        }

        logger.debug(">>> Schema default rules injection result: {}", sessionSchema);
        return sessionSchema;
    }

    /**
     *
     * @param schema
     * @return
     * @throws IOException
     */
    private Schema cloneAsSessionSchema(Schema schema) throws IOException {

        String raw = mapper.writeValueAsString(schema);
        return mapper.readValue(raw, Schema.class);
    }

    /**
     *
     * @param schema
     */
    private void populatePropertyRuleWithDataset(Schema schema) {

        injectAsIsRules(schema);
        injectTimeRules(schema);
        injectCustomFunctionRules(schema, true);
    }

    /**
     *
     * @param schema
     */
    private void populatePropertyRuleWithoutDataset(Schema schema) {

        schemaPropertiesTraverser.traverseWithPlainPropertyConsumer(schema, schemaProperty -> {

            logger.debug(">>> Processing schema property {} without provided dataset", schemaProperty);
            SessionSchemaProperty sessionSchemaProperty = (SessionSchemaProperty) schemaProperty;
            if (SchemaPropertyUtil.isDateTimeProperty(sessionSchemaProperty)) {
                sessionSchemaProperty.setRule(schemaPropertyRuleBuilder.buildCurrentTimeRule());
            } else if(SchemaPropertyUtil.isBooleanProperty(sessionSchemaProperty)) {
                sessionSchemaProperty.setRule(schemaPropertyRuleBuilder.buildRandomBooleanRule());
            } else if (SchemaPropertyUtil.isIntegerProperty(schemaProperty)) {
                sessionSchemaProperty.setRule(schemaPropertyRuleBuilder.buildRandomIntegerRule());
            } else if (SchemaPropertyUtil.isLongProperty(schemaProperty)) {
                sessionSchemaProperty.setRule(schemaPropertyRuleBuilder.buildRandomLongRule());
            } else if (SchemaPropertyUtil.isDoubleProperty(schemaProperty)) {
                sessionSchemaProperty.setRule(schemaPropertyRuleBuilder.buildRandomDoubleRule());
            } else {

                // We suppose property is string prop
                String prefix = schemaProperty.getMetadata().getName() + StringUtil.UNDERSCORE;
                sessionSchemaProperty.setRule(schemaPropertyRuleBuilder.buildUuidRule(prefix));
            }
        });

        injectCustomFunctionRules(schema, false);
    }

    /**
     *
     * @param schema
     */
    private void injectAsIsRules(Schema schema) {

        schemaPropertiesTraverser.traverseWithPlainPropertyConsumer(schema, schemaProperty -> {

            logger.debug(">>> Processing schema property {} with provided dataset", schemaProperty);
            SessionSchemaProperty sessionSchemaProperty = (SessionSchemaProperty) schemaProperty;
            if (!SchemaPropertyUtil.isDateTimeProperty(sessionSchemaProperty)) {
                sessionSchemaProperty.setRule(schemaPropertyRuleBuilder.buildAsIsRule());
            }
        });
    }

    /**
     *
     * @param schema
     */
    private void injectTimeRules(Schema schema) {

        Collection<UnfoldedSchemaProperty> unfoldedProperties = schemaParser.parse(schema);

        LinkedList<UnfoldedSchemaProperty> dateTimeProperties = unfoldedProperties.stream()
                .filter(unfoldedProperty -> SchemaPropertyUtil.isDateTimeProperty(unfoldedProperty.getProperty()))
                .sorted(sortPropertiesByPositionDepth())
                .collect(Collectors.toCollection(LinkedList::new));

        if (!dateTimeProperties.isEmpty()) {

            // Inject current time rule into first date time property
            UnfoldedSchemaProperty unfoldedFirstDateTimeProperty = dateTimeProperties.pollFirst();
            SessionSchemaProperty firstDateTimePropertyProperty = (SessionSchemaProperty) unfoldedFirstDateTimeProperty.getProperty();
            firstDateTimePropertyProperty.setRule(schemaPropertyRuleBuilder.buildCurrentTimeRule());
            String firstDateTimePropertyPosition = unfoldedFirstDateTimeProperty.getPosition();

            // Inject relative time rule into rest of the date time properties
            dateTimeProperties.forEach(unfoldedProperty -> {
                SessionSchemaProperty dateTimeProperty = (SessionSchemaProperty) unfoldedProperty.getProperty();
                dateTimeProperty.setRule(schemaPropertyRuleBuilder.buildRelativeTimeRule(firstDateTimePropertyPosition));
            });
        }
    }

    /**
     *
     * @param schema
     * @param isDatasetProvided
     */
    private void injectCustomFunctionRules(Schema schema, boolean isDatasetProvided) {

        schemaPropertiesTraverser.traverseWithObjectPropertyConsumer(schema, objectSchemaProperty -> {

            if (objectSchemaProperty.getProperties() == null || objectSchemaProperty.getProperties().isEmpty()) {

                logger.debug(">>> Injecting default CUSTOM_RULE property into property: {}", objectSchemaProperty);
                ((SessionObjectSchemaProperty) objectSchemaProperty).setRule(schemaPropertyRuleBuilder.buildObjectCustomFunctionRule(isDatasetProvided));
            }
        });

        schemaPropertiesTraverser.traverseWithArrayPropertyConsumer(schema, arraySchemaProperty -> {

            if (arraySchemaProperty.getItems() == null || arraySchemaProperty.getItems().isEmpty()) {

                logger.debug(">>> Injecting default CUSTOM_RULE property into property: {}", arraySchemaProperty);
                ((SessionArraySchemaProperty) arraySchemaProperty).setRule(schemaPropertyRuleBuilder.buildArrayCustomFunctionRule(isDatasetProvided));
            }
        });
    }

    /**
     *
     * @return
     */
    private Comparator<UnfoldedSchemaProperty> sortPropertiesByPositionDepth() {

        return (first, second) -> {

            String firstPosition = first.getPosition();
            int firstPositionSegmentsNumber = firstPosition.split(propertyPathDelimiter).length;

            String secondPosition = second.getPosition();
            int secondPositionSegmentsNumber = secondPosition.split(propertyPathDelimiter).length;

            if (firstPositionSegmentsNumber == secondPositionSegmentsNumber) {
                return firstPosition.compareTo(secondPosition);
            } else {
                return Integer.compare(firstPositionSegmentsNumber, secondPositionSegmentsNumber);
            }
        };
    }
}
