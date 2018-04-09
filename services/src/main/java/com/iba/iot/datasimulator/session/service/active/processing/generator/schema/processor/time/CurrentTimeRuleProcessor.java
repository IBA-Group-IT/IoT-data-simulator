package com.iba.iot.datasimulator.session.service.active.processing.generator.schema.processor.time;

import com.iba.iot.datasimulator.common.model.schema.property.metadata.SchemaPropertyMetadataType;
import com.iba.iot.datasimulator.common.model.schema.property.metadata.TypedSchemaPropertyMetadata;
import com.iba.iot.datasimulator.common.model.schema.property.rule.SchemaPropertyRuleType;
import com.iba.iot.datasimulator.device.model.Device;
import com.iba.iot.datasimulator.session.model.active.RuleEngineContext;
import com.iba.iot.datasimulator.session.model.unfold.UnfoldedSessionSchemaProperty;
import com.iba.iot.datasimulator.session.service.active.processing.generator.schema.processor.RuleProcessor;
import com.iba.iot.datasimulator.session.service.active.processing.generator.schema.processor.time.helper.TimeRuleHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CurrentTimeRuleProcessor implements RuleProcessor {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(CurrentTimeRuleProcessor.class);

    @Autowired
    private Map<SchemaPropertyMetadataType, TimeRuleHelper> timeRuleHelpers;

    @Override
    public SchemaPropertyRuleType getType() {
        return SchemaPropertyRuleType.CURRENT_TIME;
    }

    @Override
    public void process(UnfoldedSessionSchemaProperty unfoldedSchemaProperty, Device device, String datasetEntryValue, RuleEngineContext context) throws Exception {

        logger.debug(">>> Processing current time rule for unfolded schema property: {}", unfoldedSchemaProperty);

        TypedSchemaPropertyMetadata metadata = (TypedSchemaPropertyMetadata) unfoldedSchemaProperty.getProperty().getMetadata();
        SchemaPropertyMetadataType metadataType = metadata.getType();

        timeRuleHelpers.get(metadataType).process(unfoldedSchemaProperty, datasetEntryValue, context);
    }
}