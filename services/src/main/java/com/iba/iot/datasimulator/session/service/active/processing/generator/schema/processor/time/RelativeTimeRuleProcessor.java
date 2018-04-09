package com.iba.iot.datasimulator.session.service.active.processing.generator.schema.processor.time;

import com.iba.iot.datasimulator.common.model.IntervalMetric;
import com.iba.iot.datasimulator.common.model.schema.SchemaRootMetadataType;
import com.iba.iot.datasimulator.common.model.schema.property.metadata.FormattedTypedSchemaPropertyMetadata;
import com.iba.iot.datasimulator.common.model.schema.property.metadata.SchemaPropertyMetadataType;
import com.iba.iot.datasimulator.common.model.schema.property.rule.RelativeTimeSchemaPropertyRule;
import com.iba.iot.datasimulator.common.model.schema.property.rule.SchemaPropertyRuleType;
import com.iba.iot.datasimulator.common.service.dataset.parser.entry.DatasetEntryParser;
import com.iba.iot.datasimulator.common.util.TimeUtil;
import com.iba.iot.datasimulator.device.model.Device;
import com.iba.iot.datasimulator.session.model.active.RuleEngineContext;
import com.iba.iot.datasimulator.session.model.unfold.UnfoldedSessionSchemaProperty;
import com.iba.iot.datasimulator.session.service.active.processing.generator.schema.processor.RuleProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.ParseException;
import java.util.Map;

@Component
public class RelativeTimeRuleProcessor implements RuleProcessor {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(RelativeTimeRuleProcessor.class);

    @Autowired
    private DatasetEntryParser datasetEntryParser;

    @Override
    public SchemaPropertyRuleType getType() {
        return SchemaPropertyRuleType.RELATIVE_TIME;
    }

    @Override
    public void process(UnfoldedSessionSchemaProperty unfoldedSchemaProperty, Device device, String datasetEntryValue, RuleEngineContext context) throws Exception {

        logger.debug(">>> Processing relative time rule for unfolded schema property: {}", unfoldedSchemaProperty);
        validate(context);

        String position = unfoldedSchemaProperty.getPosition();
        FormattedTypedSchemaPropertyMetadata metadata = (FormattedTypedSchemaPropertyMetadata) unfoldedSchemaProperty.getProperty().getMetadata();
        RelativeTimeSchemaPropertyRule timeRule = (RelativeTimeSchemaPropertyRule) unfoldedSchemaProperty.getProperty().getRule();

        long propertyTime = calculateTargetPropertyTime(datasetEntryValue, context, metadata, timeRule);
        propertyTime = processTimeShift(timeRule, propertyTime);

        if (metadata.getType() == SchemaPropertyMetadataType.DATE) {

            String processedDate = TimeUtil.formatDateTime(metadata, propertyTime);
            context.getProcessingResults().put(position, processedDate);

        } else {

            propertyTime = TimeUtil.formatTimestamp(metadata, propertyTime);
            context.getProcessingResults().put(position, propertyTime);
        }
    }

    /**
     *
     * @param context
     */
    private void validate(RuleEngineContext context) {

        if (!context.isDatasetProvided()) {

            logger.error(">>> Cannot process relative time for session {} without dataset.", context.getSessionId());
            throw new RuntimeException("Session relative time rule processing error.");
        }
    }

    /**
     *
     * @param datasetEntryValue
     * @param context
     * @param metadata
     * @param timeRule
     * @return
     * @throws ParseException
     */
    private long calculateTargetPropertyTime(String datasetEntryValue, RuleEngineContext context, FormattedTypedSchemaPropertyMetadata metadata, RelativeTimeSchemaPropertyRule timeRule) throws ParseException, IOException {

        long relativeDatasetEntryTimestamp = getRelativeDateEntryTimestamp(context, timeRule, metadata);
        long targetDatasetEntryTimestamp = TimeUtil.parseDate(datasetEntryValue, metadata);

        long delta = targetDatasetEntryTimestamp - relativeDatasetEntryTimestamp;
        long currentTime = System.currentTimeMillis();

        return currentTime + delta;
    }

    /**
     *
     * @param context
     * @param timeRule
     * @param metadata
     * @return
     * @throws IOException
     */
    private long getRelativeDateEntryTimestamp(RuleEngineContext context, RelativeTimeSchemaPropertyRule timeRule,
                                               FormattedTypedSchemaPropertyMetadata metadata) throws IOException, ParseException {

        String relativePosition = timeRule.getRelativePosition();
        Map<String, Long> datasetEntryTimestamps = context.getDatasetEntryTimestamps();
        if (datasetEntryTimestamps.containsKey(relativePosition)) {
            return datasetEntryTimestamps.get(relativePosition);
        } else {

            // Relative timestamp property timestamp isn't calculated yet.
            // So, we need to parse it and calculate timestamp
            return parseRelativeDateEntryTimestamp(context, metadata, relativePosition);
        }
    }

    /**
     *
     * @param context
     * @param metadata
     * @param relativePosition
     * @return
     * @throws IOException
     * @throws ParseException
     */
    private long parseRelativeDateEntryTimestamp(RuleEngineContext context, FormattedTypedSchemaPropertyMetadata metadata,
                                                 String relativePosition) throws IOException, ParseException {

        SchemaRootMetadataType schemaType = context.getSchema().getMetadata().getType();
        String rawValue = datasetEntryParser.getValue(context.getDatasetEntry(), relativePosition, schemaType);
        long timestamp = TimeUtil.parseDate(rawValue, metadata);
        context.getDatasetEntryTimestamps().put(relativePosition, timestamp);

        return timestamp;
    }

    /**
     *
     * @param timeRule
     * @param propertyTime
     * @return
     */
    private long processTimeShift(RelativeTimeSchemaPropertyRule timeRule, long propertyTime) {

        IntervalMetric metric = timeRule.getMetric();
        Integer shift = timeRule.getShift();
        return TimeUtil.getTimestamp(propertyTime, metric, shift);
    }
}