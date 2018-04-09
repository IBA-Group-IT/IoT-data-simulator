package com.iba.iot.datasimulator.common.service.parser.processor;

import com.iba.iot.datasimulator.common.service.parser.SchemaPropertyType;
import com.iba.iot.datasimulator.common.service.parser.TypeParsingResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(value = 1)
public class BooleanTypeParsingProcessor implements TypeParsingProcessor {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(BooleanTypeParsingProcessor.class);

    /** **/
    private static final String TRUE_LITERAL = "true";

    /** **/
    private static final String FALSE_LITERAL = "false";

    @Override
    public TypeParsingResult parse(String value) {

        logger.debug(">>> Parsing string value {} as boolean.", value);

        if (TRUE_LITERAL.equalsIgnoreCase(value) || FALSE_LITERAL.equalsIgnoreCase(value)) {

            return new TypeParsingResult(true, SchemaPropertyType.BOOLEAN);
        }

        return new TypeParsingResult(false);
    }
}
