package com.iba.iot.datasimulator.session.service.active.processing.generator.schema.processor.time.helper;

import com.iba.iot.datasimulator.common.model.IntervalMetric;
import com.iba.iot.datasimulator.common.model.TimestampType;
import com.iba.iot.datasimulator.common.model.schema.property.metadata.FormattedTypedSchemaPropertyMetadata;
import com.iba.iot.datasimulator.common.model.schema.property.metadata.SchemaPropertyMetadataType;
import com.iba.iot.datasimulator.common.model.schema.property.rule.CurrentTimeSchemaPropertyRule;
import com.iba.iot.datasimulator.session.model.active.RuleEngineContext;
import com.iba.iot.datasimulator.session.model.unfold.UnfoldedSessionSchemaProperty;
import com.iba.iot.datasimulator.common.util.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Map;

@Component
public class TimestampRuleHelper implements TimeRuleHelper {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(TimestampRuleHelper.class);

    @Override
    public SchemaPropertyMetadataType getType() {
        return SchemaPropertyMetadataType.TIMESTAMP;
    }

    @Override
    public void process(UnfoldedSessionSchemaProperty unfoldedSchemaProperty, String datasetEntryValue,
                        RuleEngineContext context) throws ParseException {

        generateCurrentTime(unfoldedSchemaProperty, context);
        processDatasetEntryValue(unfoldedSchemaProperty, context, datasetEntryValue);
    }

    /**
     *
     * @param unfoldedSchemaProperty
     * @param context
     */
    private void generateCurrentTime(UnfoldedSessionSchemaProperty unfoldedSchemaProperty, RuleEngineContext context) {

        CurrentTimeSchemaPropertyRule timeRule = (CurrentTimeSchemaPropertyRule) unfoldedSchemaProperty.getProperty().getRule();
        FormattedTypedSchemaPropertyMetadata metadata = (FormattedTypedSchemaPropertyMetadata) unfoldedSchemaProperty.getProperty().getMetadata();

        long timestamp = System.currentTimeMillis();

        IntervalMetric metric = timeRule.getMetric();
        Integer shift = timeRule.getShift();

        timestamp = TimeUtil.getTimestamp(timestamp, metric, shift);
        if (TimestampType.fromString(metadata.getFormat()) == TimestampType.SECONDS) {
            timestamp = timestamp / 1000;
        }

        logger.debug(">>> Generated current time value {} for property {}", timestamp, unfoldedSchemaProperty);
        context.getProcessingResults().put(unfoldedSchemaProperty.getPosition(), timestamp);
    }

    /**
     *
     * @param unfoldedSchemaProperty
     * @param context
     * @param rawDatasetEntryValue
     */
    private void processDatasetEntryValue(UnfoldedSessionSchemaProperty unfoldedSchemaProperty, RuleEngineContext context,
                                          String rawDatasetEntryValue) {

        String position = unfoldedSchemaProperty.getPosition();
        Map<String, Long> datasetEntryTimestamps = context.getDatasetEntryTimestamps();
        
        if (context.isDatasetProvided() && !datasetEntryTimestamps.containsKey(position)) {

            FormattedTypedSchemaPropertyMetadata metadata = (FormattedTypedSchemaPropertyMetadata) unfoldedSchemaProperty.getProperty().getMetadata();

            long datasetEntryValue = Long.valueOf(rawDatasetEntryValue);
            if (TimestampType.fromString(metadata.getFormat()) == TimestampType.SECONDS) {
                datasetEntryValue *= 1000;
            }

            logger.debug(">>> Calculated dataset entry original time value {} for property {}", datasetEntryValue, unfoldedSchemaProperty);
            datasetEntryTimestamps.put(position, datasetEntryValue);
        }
    }
}
