package com.iba.iot.datasimulator.common.service.schema.rule;

import com.iba.iot.datasimulator.session.model.unfold.UnfoldedSessionSchemaProperty;

import java.util.Collection;

/**
 *
 */
public interface SchemaPropertyRuleDependencyManager {

    /**
     *
     * @param unfoldedSessionSchemaProperties
     * @return
     */
    Collection<Collection<UnfoldedSessionSchemaProperty>> findCircularRuleDependencies(
        Collection<UnfoldedSessionSchemaProperty> unfoldedSessionSchemaProperties);


    /**
     *
     * @param unfoldedSessionSchemaProperties
     * @return
     */
    Collection<UnfoldedSessionSchemaProperty> orderByRuleDependencies(
        Collection<UnfoldedSessionSchemaProperty> unfoldedSessionSchemaProperties);
}
