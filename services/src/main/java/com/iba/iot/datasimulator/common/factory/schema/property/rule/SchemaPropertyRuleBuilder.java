package com.iba.iot.datasimulator.common.factory.schema.property.rule;

import com.iba.iot.datasimulator.common.model.schema.property.rule.SchemaPropertyRule;

/**
 *
 */
public interface SchemaPropertyRuleBuilder {

    /**
     *
     * @return
     */
    SchemaPropertyRule buildAsIsRule();

    /**
     *
     * @return
     */
    SchemaPropertyRule buildCurrentTimeRule();

    /**
     *
     * @return
     */
    SchemaPropertyRule buildRelativeTimeRule(String relativePosition);

    /**
     *
     * @return
     */
    SchemaPropertyRule buildRandomBooleanRule();

    /**
     *
     * @return
     */
    SchemaPropertyRule buildRandomIntegerRule();

    /**
     *
     * @return
     */
    SchemaPropertyRule buildRandomLongRule();

    /**
     *
     * @return
     */
    SchemaPropertyRule buildRandomDoubleRule();

    /**
     *
     * @return
     */
    SchemaPropertyRule buildUuidRule();

    /**
     *
     * @return
     */
    SchemaPropertyRule buildUuidRule(String prefix);

    /**
     *
     * @param isDatasetProvided
     * @return
     */
    SchemaPropertyRule buildArrayCustomFunctionRule(boolean isDatasetProvided);

    /**
     *
     * @param isDatasetProvided
     * @return
     */
    SchemaPropertyRule buildObjectCustomFunctionRule(boolean isDatasetProvided);
}
