package com.iba.iot.datasimulator.common.service.schema.parser;

import com.iba.iot.datasimulator.common.model.schema.Schema;

import java.util.Collection;

/**
 *
 */
public interface SchemaParser<T> {

    /**
     *
     *
     * @param schema@return
     */
    Collection<T> parse(Schema schema);

}
