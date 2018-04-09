package com.iba.iot.datasimulator.common.model.schema.property.rule.literal;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.iba.iot.datasimulator.common.model.schema.property.rule.SchemaPropertyRuleType;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Data
@ToString
@JsonDeserialize
public class DoubleLiteralSchemaPropertyRule implements LiteralSchemaPropertyRule<Double> {

    @NotNull
    private final SchemaPropertyRuleType type = SchemaPropertyRuleType.LITERAL_DOUBLE;

    @NotNull
    private Double value;

}
