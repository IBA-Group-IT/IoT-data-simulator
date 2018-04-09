package com.iba.iot.datasimulator.common.model.schema.property;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.iba.iot.datasimulator.common.model.schema.property.metadata.SchemaPropertyMetadata;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonDeserialize
public class SchemaPropertyModel implements SchemaProperty {

    @NotNull
    private SchemaPropertyType type;

    @NotNull
    @Valid
    private SchemaPropertyMetadata metadata;

}
