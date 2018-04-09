package com.iba.iot.datasimulator.session.service.active.processing.generator.schema.processor;

import com.iba.iot.datasimulator.common.model.schema.SchemaRootMetadataType;
import com.iba.iot.datasimulator.common.model.schema.property.rule.SchemaPropertyRuleType;
import com.iba.iot.datasimulator.device.model.Device;
import com.iba.iot.datasimulator.session.model.active.RuleEngineContext;
import com.iba.iot.datasimulator.session.model.unfold.UnfoldedSessionSchemaProperty;
import com.iba.iot.datasimulator.session.service.active.processing.generator.schema.processor.convertor.DatasetEntryValueConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AsIsRuleProcessor implements RuleProcessor {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(AsIsRuleProcessor.class);

    @Autowired
    private DatasetEntryValueConverter datasetEntryValueConverter;

    @Override
    public void process(UnfoldedSessionSchemaProperty unfoldedSchemaProperty, Device device, String datasetEntryValue, RuleEngineContext context) throws Exception {

        logger.debug(">>> Processing as is rule for unfolded schema property {} and dataset value {}",
                unfoldedSchemaProperty, datasetEntryValue);

        String position = unfoldedSchemaProperty.getPosition();
        Object result = getDatasetEntryValue(unfoldedSchemaProperty, datasetEntryValue, context);

        logger.debug(">>> Processing as is rule for unfolded schema property {} and dataset value {} result: {}",
                unfoldedSchemaProperty, datasetEntryValue, result);
        context.getProcessingResults().put(position, result);
    }

    /**
     *
     * @param unfoldedSchemaProperty
     * @param datasetEntryValue
     * @param context
     * @return
     */
    private Object getDatasetEntryValue(UnfoldedSessionSchemaProperty unfoldedSchemaProperty, String datasetEntryValue, RuleEngineContext context) throws IOException {

        if (context.getSchema().getMetadata().getType() == SchemaRootMetadataType.CSV) {
            // In case of CSV all dataset entry values are strings
            return datasetEntryValue;
        } else {
            return datasetEntryValueConverter.convertToOriginalType(unfoldedSchemaProperty, datasetEntryValue);
        }
    }

    @Override
    public SchemaPropertyRuleType getType() {
        return SchemaPropertyRuleType.AS_IS;
    }
}
