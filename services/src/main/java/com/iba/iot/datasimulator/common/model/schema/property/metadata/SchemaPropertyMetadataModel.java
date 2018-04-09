package com.iba.iot.datasimulator.common.model.schema.property.metadata;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.NotEmpty;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonDeserialize
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SchemaPropertyMetadataModel implements SchemaPropertyMetadata {

    @NotEmpty
    private String position;

    private String name;

    private String description;

    /**
     *
     * @param position
     */
    public SchemaPropertyMetadataModel(String position) {
        this.position = position;
    }

    public SchemaPropertyMetadataModel(String position, String name) {
        this.position = position;
        this.name = name;
    }
}
