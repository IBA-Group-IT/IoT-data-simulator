package com.iba.iot.datasimulator.common.service.parser.processor;

import com.iba.iot.datasimulator.common.service.parser.SchemaPropertyType;
import com.iba.iot.datasimulator.common.service.parser.TypeParsingResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(value = 5)
public class DoubleTypeParsingProcessor implements TypeParsingProcessor {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(DoubleTypeParsingProcessor.class);

    @Override
    public TypeParsingResult parse(String value) {

        logger.debug(">>> Parsing string value {} as double.", value);

        try {

            Double.parseDouble(value);
            return new TypeParsingResult(true, SchemaPropertyType.DOUBLE);

        } catch (NumberFormatException exception) {
            return new TypeParsingResult(false);
        }
    }
}
