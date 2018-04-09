package com.iba.iot.datasimulator.session.service.active.processing.generator.schema;

import com.iba.iot.datasimulator.common.model.schema.Schema;
import com.iba.iot.datasimulator.common.model.schema.SchemaRootMetadataType;
import com.iba.iot.datasimulator.common.model.schema.property.rule.SchemaPropertyRuleType;
import com.iba.iot.datasimulator.common.service.schema.parser.SchemaParser;
import com.iba.iot.datasimulator.common.service.schema.rule.SchemaPropertyRuleDependencyManager;
import com.iba.iot.datasimulator.device.model.Device;
import com.iba.iot.datasimulator.session.model.active.ActiveSessionPayload;
import com.iba.iot.datasimulator.session.model.active.RuleEngineContext;
import com.iba.iot.datasimulator.session.model.unfold.UnfoldedSessionSchemaProperty;
import com.iba.iot.datasimulator.session.service.active.processing.generator.SessionPayloadGenerator;
import com.iba.iot.datasimulator.session.service.active.processing.generator.schema.databind.DatasetEntryDeserializer;
import com.iba.iot.datasimulator.session.service.active.processing.generator.schema.databind.DatasetEntrySerializer;
import com.iba.iot.datasimulator.session.service.active.processing.generator.schema.processor.RuleProcessor;
import com.iba.iot.datasimulator.session.service.active.processing.state.SessionStateManager;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class SessionSchemaPayloadGenerator implements SessionPayloadGenerator {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(SessionSchemaPayloadGenerator.class);

    @Value("#{systemEnvironment['SKIP_RULE_PROCESSING_FOR_EMPTY_DATASET_PROPERTY'] ?: true}")
    private boolean skipEmptyPropertyRuleProcessing;

    @Value("#{systemEnvironment['SKIP_EMPTY_GENERATED_VALUES'] ?: true}")
    private boolean skipEmptyGeneratedValues;

    /** **/
    private String sessionId;

    /** **/
    private Schema schema;

    /** **/
    private Collection<UnfoldedSessionSchemaProperty> schemaProperties;

    @Autowired
    private SchemaParser<UnfoldedSessionSchemaProperty> sessionSchemaParser;

    @Autowired
    private SchemaPropertyRuleDependencyManager ruleDependencyManager;

    @Autowired
    private Map<SchemaRootMetadataType, DatasetEntryDeserializer> datasetEntryDeserializers;

    @Autowired
    private Map<SchemaRootMetadataType, DatasetEntrySerializer> datasetEntrySerializers;

    @Autowired
    private Map<SchemaPropertyRuleType, RuleProcessor> ruleProcessors;

    @Autowired
    private SessionStateManager sessionPropertyStateManager;

    /**
     *
     * @param schema
     */
    public SessionSchemaPayloadGenerator(String sessionId, Schema schema) {

        this.sessionId = sessionId;
        this.schema = schema;
    }

    @PostConstruct
    private void init() {

        logger.debug(">>> Unfolding session schema properties for schema: {}", schema);
        Collection<UnfoldedSessionSchemaProperty> unorderedSchemaProperties = sessionSchemaParser.parse(schema);
        schemaProperties = ruleDependencyManager.orderByRuleDependencies(unorderedSchemaProperties);

        logger.info(">>> Session {} rule engine skip empty property processing property: {}", sessionId, skipEmptyPropertyRuleProcessing);
        logger.info(">>> Session {} rule engine skip empty generated value property: {}", sessionId, skipEmptyGeneratedValues);
    }

    @Override
    public ActiveSessionPayload process(ActiveSessionPayload payload) throws Exception {

        logger.debug(">>> Processing session schema payload generator for session: {} and payload: {}", sessionId, payload);

        RuleEngineContext ruleEngineContext = new RuleEngineContext(sessionId, payload, schema, sessionPropertyStateManager);
        SchemaRootMetadataType schemaMetadataType = schema.getMetadata().getType();

        Map<String, String> datasetProperties = deserializeDatasetEntry(payload, schemaMetadataType);
        processRules(payload, datasetProperties, ruleEngineContext);
        filterGeneratedValues(ruleEngineContext);
        String generatedPayload = serializeDatasetEntry(schemaMetadataType, ruleEngineContext);

        payload.setGeneratedPayload(generatedPayload);
        logger.debug(">>> Session rule engine: payload processing has completed: {}", payload);

        return payload;
    }

    /**
     *
     * @param payload
     * @param datasetProperties
     * @param ruleEngineContext
     */
    private void processRules(ActiveSessionPayload payload, Map<String, String> datasetProperties, RuleEngineContext ruleEngineContext) throws Exception {

        for (UnfoldedSessionSchemaProperty schemaProperty : schemaProperties) {

            SchemaPropertyRuleType ruleType = schemaProperty.getProperty().getRule().getType();
            String datasetEntryValue = datasetProperties.get(schemaProperty.getPosition());
            Device device = payload.getDevice();

            if (!payload.isDatasetProvided() || // in this case all dataset entry values are empty
                !skipEmptyPropertyRuleProcessing || (payload.isDatasetProvided() && StringUtils.isNotEmpty(datasetEntryValue))) {

                if (ruleProcessors.containsKey(ruleType)) {
                    ruleProcessors.get(ruleType).process(schemaProperty, device, datasetEntryValue, ruleEngineContext);
                } else {

                    logger.error(">>> Cannot proceed with unsupported rule type {}", ruleType);
                    throw new RuntimeException("Unsupported rule type " + ruleType + " error.");
                }
            }
        }
    }

    /**
     *
     * @param ruleEngineContext
     */
    private void filterGeneratedValues(RuleEngineContext ruleEngineContext) {

        if (skipEmptyGeneratedValues &&
            // We cannot skip empty values for CSV dataset, because it will break result CSV row structure
            schema.getMetadata().getType() != SchemaRootMetadataType.CSV) {

            Map<String, Object> processingResults = ruleEngineContext.getProcessingResults();

            Map<String, Object> filteredResults =
                processingResults.entrySet()
                                 .stream()
                                 .filter(entry -> Objects.nonNull(entry.getValue()))
                                 .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

            processingResults.clear();
            processingResults.putAll(filteredResults);
        }
    }

    /**
     *
     * @param payload
     * @param schemaType
     * @return
     * @throws IOException
     */
    private  Map<String, String> deserializeDatasetEntry(ActiveSessionPayload payload, SchemaRootMetadataType schemaType) throws IOException {
        return datasetEntryDeserializers.get(schemaType).deserialize(payload.getDatasetEntry(), payload.isDatasetProvided());
    }

    /**
     *
     * @param schemaMetadataType
     * @param ruleEngineContext
     * @return
     */
    private String serializeDatasetEntry(SchemaRootMetadataType schemaMetadataType, RuleEngineContext ruleEngineContext) {
        return datasetEntrySerializers.get(schemaMetadataType).serialize(ruleEngineContext.getProcessingResults(), schema.getType());
    }
}
