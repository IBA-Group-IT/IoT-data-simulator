package com.iba.iot.datasimulator.common.model.schema.property;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Collection;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonDeserialize
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArraySchemaPropertyModel extends SchemaPropertyModel implements ArraySchemaProperty {

    /** **/
    private Collection<SchemaProperty> items;

    /**
     *
     * @param type
     */
    public ArraySchemaPropertyModel(SchemaPropertyType type) {
        this.setType(type);
    }
}
