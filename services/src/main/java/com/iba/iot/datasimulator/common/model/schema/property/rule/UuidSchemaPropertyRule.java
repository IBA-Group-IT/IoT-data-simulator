package com.iba.iot.datasimulator.common.model.schema.property.rule;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Data
@ToString
@JsonDeserialize
@NoArgsConstructor
public class UuidSchemaPropertyRule implements SchemaPropertyRule {

    @Getter
    @NotNull
    private SchemaPropertyRuleType type = SchemaPropertyRuleType.UUID;

    @Getter
    private String prefix;

    @Getter
    private String postfix;

    /**
     *
     * @param prefix
     */
    public UuidSchemaPropertyRule(String prefix) {
        this.prefix = prefix;
    }
}
