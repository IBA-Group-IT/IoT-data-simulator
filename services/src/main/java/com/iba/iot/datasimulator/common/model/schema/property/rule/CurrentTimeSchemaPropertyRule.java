package com.iba.iot.datasimulator.common.model.schema.property.rule;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.iba.iot.datasimulator.common.model.IntervalMetric;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Data
@ToString
@JsonDeserialize
public class CurrentTimeSchemaPropertyRule implements SchemaPropertyRule {

    @Getter
    @NotNull
    private SchemaPropertyRuleType type = SchemaPropertyRuleType.CURRENT_TIME;

    private IntervalMetric metric;

    private Integer shift;
}
