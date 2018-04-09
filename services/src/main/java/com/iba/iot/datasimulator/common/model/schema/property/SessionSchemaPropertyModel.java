package com.iba.iot.datasimulator.common.model.schema.property;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.iba.iot.datasimulator.common.model.schema.property.rule.SchemaPropertyRule;
import lombok.Data;
import lombok.ToString;

import javax.validation.Valid;

@Data
@ToString
@JsonDeserialize
public class SessionSchemaPropertyModel extends SchemaPropertyModel implements SessionSchemaProperty {

    @Valid
    private SchemaPropertyRule rule;
}
