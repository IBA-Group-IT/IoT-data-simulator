package com.iba.iot.datasimulator.common.service.schema.rule;

import com.iba.iot.datasimulator.session.model.unfold.UnfoldedSessionSchemaProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Collection;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PropertyRuleDependencySequence {

    /** **/
    private Collection<UnfoldedSessionSchemaProperty> sequence;

    /** **/
    private boolean isCyclic;
}
