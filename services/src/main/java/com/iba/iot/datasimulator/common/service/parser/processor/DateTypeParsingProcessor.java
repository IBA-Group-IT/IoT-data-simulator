package com.iba.iot.datasimulator.common.service.parser.processor;

import com.iba.iot.datasimulator.common.service.parser.SchemaPropertyType;
import com.iba.iot.datasimulator.common.service.parser.TypeParsingResult;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Qualifier("dateParsingProcessor")
@Order(value = 6)
public class DateTypeParsingProcessor implements TypeParsingProcessor {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(DateTypeParsingProcessor.class);

    @Autowired
    @Qualifier("date-format-regexps")
    private Map<String, String> dateFormatRegexps;

    @Override
    public TypeParsingResult parse(String value) {

        logger.debug(">>> Parsing string value {} as date.", value);

        String format = determineDateFormat(value);
        if (StringUtils.isNotEmpty(format)) {
            return new TypeParsingResult(true, SchemaPropertyType.DATE, format);
        }

        return new TypeParsingResult(false);
    }

    /**
     *
     * @param dateString
     * @return
     */
    public String determineDateFormat(String dateString) {

        for (String regexp : dateFormatRegexps.keySet()) {
            if (dateString.toLowerCase().matches(regexp)) {
                return dateFormatRegexps.get(regexp);
            }
        }

        return null;
    }
}
