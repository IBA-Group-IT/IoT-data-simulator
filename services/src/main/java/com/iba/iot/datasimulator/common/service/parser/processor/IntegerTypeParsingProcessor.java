package com.iba.iot.datasimulator.common.service.parser.processor;

import com.iba.iot.datasimulator.common.service.parser.SchemaPropertyType;
import com.iba.iot.datasimulator.common.service.parser.TypeParsingResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(value = 3)
public class IntegerTypeParsingProcessor implements TypeParsingProcessor {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(IntegerTypeParsingProcessor.class);

    @Override
    public TypeParsingResult parse(String value) {

        logger.debug(">>> Parsing string value {} as integer.", value);

        try {

            Integer.parseInt(value);
            return new TypeParsingResult(true, SchemaPropertyType.INTEGER);

        } catch (NumberFormatException exception) {
            return new TypeParsingResult(false);
        }
    }
}
