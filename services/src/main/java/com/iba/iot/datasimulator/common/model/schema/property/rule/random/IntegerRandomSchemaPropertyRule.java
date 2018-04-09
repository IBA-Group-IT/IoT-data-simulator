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
public class IntegerRandomSchemaPropertyRule implements RandomSchemaPropertyRule<Integer> {

    @NotNull
    private final SchemaPropertyRuleType type = SchemaPropertyRuleType.RANDOM_INTEGER;

    @NotNull
    private Integer min;

    @NotNull
    private Integer max;

    /**
     *
     * @param min
     * @param max
     */
    public IntegerRandomSchemaPropertyRule(Integer min, Integer max) {
        this.min = min;
        this.max = max;
    }
}
