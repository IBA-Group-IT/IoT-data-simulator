package com.iba.iot.datasimulator.common.model.schema.property.rule.random;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.iba.iot.datasimulator.common.model.schema.property.rule.SchemaPropertyRule;
import com.iba.iot.datasimulator.common.model.schema.property.rule.SchemaPropertyRuleType;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Data
@ToString
@JsonDeserialize
@NoArgsConstructor
public class BooleanRandomSchemaPropertyRule implements SchemaPropertyRule {

    @Getter
    @NotNull
    private SchemaPropertyRuleType type = SchemaPropertyRuleType.RANDOM_BOOLEAN;

    @NotNull
    private double successProbability;

    /**
     *
     * @param successProbability
     */
    public BooleanRandomSchemaPropertyRule(double successProbability) {
        this.successProbability = successProbability;
    }
}
