package com.iba.iot.datasimulator.common.model.schema.property.description;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SchemaPropertyDescription {

    private String path;

    private String type;

}
