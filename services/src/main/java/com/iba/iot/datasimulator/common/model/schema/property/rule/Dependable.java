package com.iba.iot.datasimulator.common.model.schema.property.rule;

/**
 *
 */
public interface Dependable {

    /**
     *
     * @return
     */
    String getDependsOn();

    /**
     *
     * @param propertyPath
     */
    void setDependsOn(String propertyPath);
}
