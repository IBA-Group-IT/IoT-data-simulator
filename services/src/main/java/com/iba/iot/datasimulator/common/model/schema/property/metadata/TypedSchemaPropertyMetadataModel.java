package com.iba.iot.datasimulator.common.model.schema.property.metadata;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonDeserialize
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TypedSchemaPropertyMetadataModel extends SchemaPropertyMetadataModel implements TypedSchemaPropertyMetadata {

    @NotNull
    private SchemaPropertyMetadataType type;

    /**
     *
     * @param position
     * @param name
     * @param type
     */
    public TypedSchemaPropertyMetadataModel(String position, String name, SchemaPropertyMetadataType type) {
        super(position, name);
        this.type = type;
    }
}
