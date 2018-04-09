package com.iba.iot.datasimulator.common.model.schema.property;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.iba.iot.datasimulator.common.model.schema.property.rule.SchemaPropertyRule;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.Valid;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonDeserialize
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SessionArraySchemaPropertyModel extends ArraySchemaPropertyModel implements SessionArraySchemaProperty {

    @Valid
    private SchemaPropertyRule rule;
}
