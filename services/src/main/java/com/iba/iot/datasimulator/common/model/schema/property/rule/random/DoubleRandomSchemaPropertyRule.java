package com.iba.iot.datasimulator.common.model.schema.property.rule.random;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.iba.iot.datasimulator.common.model.schema.property.rule.SchemaPropertyRuleType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Data
@ToString
@JsonDeserialize
@NoArgsConstructor
public class DoubleRandomSchemaPropertyRule implements RandomSchemaPropertyRule<Double> {

    @NotNull
    private final SchemaPropertyRuleType type = SchemaPropertyRuleType.RANDOM_DOUBLE;

    @NotNull
    private Double min;

    @NotNull
    private Double max;

    /**
     *
     * @param min
     * @param max
     */
    public DoubleRandomSchemaPropertyRule(Double min, Double max) {
        this.min = min;
        this.max = max;
    }
}
