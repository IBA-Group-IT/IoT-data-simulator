package com.iba.iot.datasimulator.session.service.active.processing.generator.schema.processor.time.helper;

import com.iba.iot.datasimulator.common.model.IntervalMetric;
import com.iba.iot.datasimulator.common.model.schema.property.metadata.FormattedTypedSchemaPropertyMetadata;
import com.iba.iot.datasimulator.common.model.schema.property.metadata.SchemaPropertyMetadataType;
import com.iba.iot.datasimulator.common.model.schema.property.rule.CurrentTimeSchemaPropertyRule;
import com.iba.iot.datasimulator.common.util.TimeUtil;
import com.iba.iot.datasimulator.session.model.active.RuleEngineContext;
import com.iba.iot.datasimulator.session.model.unfold.UnfoldedSessionSchemaProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Component
public class DateRuleHelper implements TimeRuleHelper {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(DateRuleHelper.class);

    @Override
    public SchemaPropertyMetadataType getType() {
        return SchemaPropertyMetadataType.DATE;
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

        String position = unfoldedSchemaProperty.getPosition();
        CurrentTimeSchemaPropertyRule timeRule = (CurrentTimeSchemaPropertyRule) unfoldedSchemaProperty.getProperty().getRule();
        FormattedTypedSchemaPropertyMetadata metadata = (FormattedTypedSchemaPropertyMetadata) unfoldedSchemaProperty.getProperty().getMetadata();

        String format = metadata.getFormat();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);

        long timestamp = System.currentTimeMillis();

        IntervalMetric metric = timeRule.getMetric();
        Integer shift = timeRule.getShift();

        timestamp = TimeUtil.getTimestamp(timestamp, metric, shift);
        String processedDate = simpleDateFormat.format(new Date(timestamp));

        logger.debug(">>> Generated current date value {} for property {}", processedDate, unfoldedSchemaProperty);
        context.getProcessingResults().put(position, processedDate);
    }

    /**
     *
     * @param unfoldedSchemaProperty
     * @param context
     * @param rawDatasetEntryValue
     */
    private void processDatasetEntryValue(UnfoldedSessionSchemaProperty unfoldedSchemaProperty, RuleEngineContext context, String rawDatasetEntryValue) throws ParseException {

        String position = unfoldedSchemaProperty.getPosition();
        Map<String, Long> datasetEntryTimestamps = context.getDatasetEntryTimestamps();

        if (context.isDatasetProvided() && !datasetEntryTimestamps.containsKey(position)) {

            FormattedTypedSchemaPropertyMetadata metadata = (FormattedTypedSchemaPropertyMetadata) unfoldedSchemaProperty.getProperty().getMetadata();
            String format = metadata.getFormat();

            Date datasetEntryDate = new SimpleDateFormat(format).parse(rawDatasetEntryValue);
            long datasetEntryValue = datasetEntryDate.getTime();

            logger.debug(">>> Calculated dataset entry original time value {} for property {}", datasetEntryValue, unfoldedSchemaProperty);
            datasetEntryTimestamps.put(position, datasetEntryValue);
        }
    }
}
