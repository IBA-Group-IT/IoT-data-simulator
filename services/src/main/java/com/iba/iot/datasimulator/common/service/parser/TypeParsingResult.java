package com.iba.iot.datasimulator.common.service.parser;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
public class TypeParsingResult {

    /** **/
    private boolean isSucceed;

    /** **/
    private SchemaPropertyType type;

    /** **/
    private String format;

    /**
     *
     * @param isSucceed
     */
    public TypeParsingResult(boolean isSucceed) {
        this.isSucceed = isSucceed;
    }

    /**
     *
     * @param isSucceed
     * @param type
     */
    public TypeParsingResult(boolean isSucceed, SchemaPropertyType type) {
        this.isSucceed = isSucceed;
        this.type = type;
    }
}
