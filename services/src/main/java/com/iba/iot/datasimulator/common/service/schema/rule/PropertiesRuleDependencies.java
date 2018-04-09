package com.iba.iot.datasimulator.common.service.schema.rule;

import com.iba.iot.datasimulator.session.model.unfold.UnfoldedSessionSchemaProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collection;

@Data
public class PropertiesRuleDependencies {

    /** **/
    private Collection<UnfoldedSessionSchemaProperty> independentProperties = new ArrayList<>();

    /** **/
    private Collection<Collection<UnfoldedSessionSchemaProperty>> acyclicDependencies = new ArrayList<>();

    /** **/
    private Collection<Collection<UnfoldedSessionSchemaProperty>> cyclicDependencies = new ArrayList<>();
}
