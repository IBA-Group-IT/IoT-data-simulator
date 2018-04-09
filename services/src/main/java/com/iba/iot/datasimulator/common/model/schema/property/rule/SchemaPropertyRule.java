package com.iba.iot.datasimulator.common.model.schema.property.rule;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.iba.iot.datasimulator.common.json.SchemaPropertyRuleDeserializer;
import com.iba.iot.datasimulator.common.model.TypedEntity;

/**
 *
 */
@JsonDeserialize(using = SchemaPropertyRuleDeserializer.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public interface SchemaPropertyRule extends TypedEntity<SchemaPropertyRuleType> {


}
