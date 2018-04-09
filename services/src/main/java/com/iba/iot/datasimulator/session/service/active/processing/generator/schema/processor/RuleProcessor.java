package com.iba.iot.datasimulator.session.service.active.processing.generator.schema.processor;

import com.iba.iot.datasimulator.common.model.TypedEntity;
import com.iba.iot.datasimulator.common.model.schema.property.rule.SchemaPropertyRuleType;
import com.iba.iot.datasimulator.device.model.Device;
import com.iba.iot.datasimulator.session.model.unfold.UnfoldedSessionSchemaProperty;
import com.iba.iot.datasimulator.session.model.active.RuleEngineContext;

/**
 *
 */
public interface RuleProcessor extends TypedEntity<SchemaPropertyRuleType> {

    /**
     * @param unfoldedSchemaProperty
     * @param device
     * @param datasetEntryValue
     * @param context
     */
    void process(UnfoldedSessionSchemaProperty unfoldedSchemaProperty, Device device, String datasetEntryValue, RuleEngineContext context) throws Exception;

}
