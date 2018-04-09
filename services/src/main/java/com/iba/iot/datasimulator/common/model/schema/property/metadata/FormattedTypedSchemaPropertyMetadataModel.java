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
public class FormattedTypedSchemaPropertyMetadataModel extends TypedSchemaPropertyMetadataModel implements FormattedTypedSchemaPropertyMetadata {

    @NotEmpty
    private String format;

    /**
     *
     * @param position
     * @param name
     * @param type
     * @param format
     */
    public FormattedTypedSchemaPropertyMetadataModel(String position, String name, SchemaPropertyMetadataType type, String format) {
        super(position, name, type);
        this.format = format;
    }
}
