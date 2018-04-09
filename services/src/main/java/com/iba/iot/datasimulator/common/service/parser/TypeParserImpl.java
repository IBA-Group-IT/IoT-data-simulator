
package com.iba.iot.datasimulator.common.service.parser;

import com.iba.iot.datasimulator.common.service.parser.processor.TypeParsingProcessor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TypeParserImpl implements TypeParser {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(TypeParserImpl.class);

    @Autowired
    private List<TypeParsingProcessor> processors;

    @Autowired
    @Qualifier("timestampParsingProcessor")
    private TypeParsingProcessor timestampParsingProcessor;

    @Autowired
    @Qualifier("dateParsingProcessor")
    private TypeParsingProcessor dateParsingProcessor;

    @Override
    public TypeParsingResult parse(String value) {

        logger.debug(">>> Parsing type for raw value {}", value);

        TypeParsingResult result = getDefaultResult();

        final String trimmedValue = value.trim();
        if (StringUtils.isNotEmpty(trimmedValue)) {

            result =  processors.stream()
                                .map(processor -> processor.parse(value))
                                .filter(TypeParsingResult::isSucceed)
                                .findFirst()
                                .orElse(getDefaultResult());
        }

        logger.debug(" Raw string value {} parsing result: {}", result);
        return result;
    }

    @Override
    public TypeParsingResult parseLongAsTimestamp(long value) {
        return timestampParsingProcessor.parse(Long.toString(value));
    }

    @Override
    public TypeParsingResult parseStringAsDate(String value) {
        return dateParsingProcessor.parse(value);
    }

    /**
     *
     * @return
     */
    private TypeParsingResult getDefaultResult() {
        return new TypeParsingResult(true, SchemaPropertyType.STRING);
    }
}
