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
public class LongRandomSchemaPropertyRule implements RandomSchemaPropertyRule<Long> {

    @NotNull
    private final SchemaPropertyRuleType type = SchemaPropertyRuleType.RANDOM_LONG;

    @NotNull
    private Long min;

    @NotNull
    private Long max;

    /**
     *
     * @param min
     * @param max
     */
    public LongRandomSchemaPropertyRule(Long min, Long max) {
        this.min = min;
        this.max = max;
    }
}
