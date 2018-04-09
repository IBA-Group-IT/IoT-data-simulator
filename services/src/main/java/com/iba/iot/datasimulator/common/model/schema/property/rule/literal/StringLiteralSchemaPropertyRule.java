package com.iba.iot.datasimulator.common.model.schema.property.rule.literal;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.iba.iot.datasimulator.common.model.schema.property.rule.SchemaPropertyRuleType;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

@Data
@ToString
@JsonDeserialize
public class StringLiteralSchemaPropertyRule implements LiteralSchemaPropertyRule<String> {

    @NotNull
    private final SchemaPropertyRuleType type = SchemaPropertyRuleType.LITERAL_STRING;

    @NotEmpty
    private String value;
}
