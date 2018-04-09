package com.iba.iot.datasimulator.session.model.unfold;

import com.iba.iot.datasimulator.common.model.schema.property.SchemaProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
public class UnfoldedSchemaProperty {

    /** **/
    private String position;

    /** **/
    private SchemaProperty property;
}
