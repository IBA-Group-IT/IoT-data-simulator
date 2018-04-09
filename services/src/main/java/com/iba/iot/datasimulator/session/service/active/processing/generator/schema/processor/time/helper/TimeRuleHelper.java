package com.iba.iot.datasimulator.session.service.active.processing.generator.schema.processor.time.helper;

import com.iba.iot.datasimulator.common.model.TypedEntity;
import com.iba.iot.datasimulator.common.model.schema.property.metadata.SchemaPropertyMetadataType;
import com.iba.iot.datasimulator.session.model.unfold.UnfoldedSessionSchemaProperty;
import com.iba.iot.datasimulator.session.model.active.RuleEngineContext;

import java.text.ParseException;

/**
 *
 */
public interface TimeRuleHelper extends TypedEntity<SchemaPropertyMetadataType> {

    /**
     *
     * @param unfoldedSchemaProperty
     * @param datasetEntryValue
     * @param context
     * @throws ParseException
     */
    void process(UnfoldedSessionSchemaProperty unfoldedSchemaProperty, String datasetEntryValue, RuleEngineContext context) throws ParseException;
}
