package com.iba.iot.datasimulator.session.model.unfold;

import com.iba.iot.datasimulator.common.model.schema.property.SessionSchemaProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
public class UnfoldedSessionSchemaProperty {

    private String position;

    private SessionSchemaProperty property;
}
