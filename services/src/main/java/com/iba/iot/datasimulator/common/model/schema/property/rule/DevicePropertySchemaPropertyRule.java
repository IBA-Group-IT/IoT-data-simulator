package com.iba.iot.datasimulator.common.model.schema.property.rule;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

@Data
@ToString
@JsonDeserialize
public class DevicePropertySchemaPropertyRule implements SchemaPropertyRule {

    @Getter
    @NotNull
    private SchemaPropertyRuleType type = SchemaPropertyRuleType.DEVICE_PROPERTY;

    @NotEmpty
    private String propertyName;
}
