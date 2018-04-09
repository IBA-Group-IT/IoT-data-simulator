package com.iba.iot.datasimulator.common.service.schema.manager.rule;

import com.iba.iot.datasimulator.common.model.schema.Schema;

import java.io.IOException;

/**
 *
 */
public interface SchemaDefaultRulesInjector {

    /**
     *
     * @param schema
     * @param isDatasetProvided
     * @return
     */
    Schema injectDefaultRules(Schema schema, boolean isDatasetProvided) throws IOException;

}
