package com.iba.iot.datasimulator.common.service.parser.processor;

import com.iba.iot.datasimulator.common.service.parser.SchemaPropertyType;
import com.iba.iot.datasimulator.common.service.parser.TypeParsingResult;
import com.iba.iot.datasimulator.common.model.TimestampType;
import com.iba.iot.datasimulator.common.util.MathUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Qualifier("timestampParsingProcessor")
@Order(value = 2)
public class TimestampTypeParsingProcessor implements TypeParsingProcessor {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(TimestampTypeParsingProcessor.class);

    /** **/
    private static final int TIMESTAMP_SECONDS_FORMAT_NUMBER_LENGTH = 10;

    /** **/
    private static final int TIMESTAMP_MILLISECONDS_FORMAT_NUMBER_LENGTH = 13;

    @Override
    public TypeParsingResult parse(String value) {

        logger.debug(">>> Parsing string value {} as timestamp.", value);

        try {

            long number = Long.parseLong(value);
            int length = MathUtil.getNumberLength(number);
            if (length == TIMESTAMP_MILLISECONDS_FORMAT_NUMBER_LENGTH) {

                return new TypeParsingResult(true, SchemaPropertyType.TIMESTAMP, TimestampType.MILLISECONDS.toString());

            } else if (length == TIMESTAMP_SECONDS_FORMAT_NUMBER_LENGTH) {

                return new TypeParsingResult(true, SchemaPropertyType.TIMESTAMP, TimestampType.SECONDS.toString());
            }

        } catch (NumberFormatException exception) { }

        return new TypeParsingResult(false);
    }
}
