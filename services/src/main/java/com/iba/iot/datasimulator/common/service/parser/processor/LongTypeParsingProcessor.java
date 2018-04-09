package com.iba.iot.datasimulator.common.service.parser.processor;

import com.iba.iot.datasimulator.common.service.parser.SchemaPropertyType;
import com.iba.iot.datasimulator.common.service.parser.TypeParsingResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(value = 4)
public class LongTypeParsingProcessor implements TypeParsingProcessor {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(LongTypeParsingProcessor.class);

    @Override
    public TypeParsingResult parse(String value) {

        logger.debug(">>> Parsing string value {} as long.", value);

        try {

            Long.parseLong(value);
            return new TypeParsingResult(true, SchemaPropertyType.LONG);

        } catch (NumberFormatException exception) {
            return new TypeParsingResult(false);
        }
    }
}
