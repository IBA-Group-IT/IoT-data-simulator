package com.iba.iot.datasimulator.common.model.schema.property;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Map;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonDeserialize
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ObjectSchemaPropertyModel extends SchemaPropertyModel implements ObjectSchemaProperty {

    /** **/
    private Map<String, SchemaProperty> properties;

    /**
     *
     * @param type
     */
    public ObjectSchemaPropertyModel(SchemaPropertyType type) {
        setType(type);
    }
}
