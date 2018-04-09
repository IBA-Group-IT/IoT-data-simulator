package com.iba.iot.datasimulator.common.config;

import com.iba.iot.datasimulator.common.model.DateTimeFormatRegex;
import com.iba.iot.datasimulator.common.util.FileUtil;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
public class DateFormatsConfig {

    /** **/
    private static final String FILE_NAME = "regexp/date-formats-regexps.txt";

    /** **/
    private static final String VALUES_DELIMITER = "===";

    /** **/
    private static final String EOL_REGEX = "\\r?\\n";

    @Bean
    @Qualifier("date-format-regexps")
    public Map<String, String> getDateFormatRegexps () throws IOException, URISyntaxException {

        return Stream.of(FileUtil.readFile(FILE_NAME).split(EOL_REGEX))
                     .map(line -> {
                              String[] values = line.split(VALUES_DELIMITER);
                              return new DateTimeFormatRegex(values[0].trim(), values[1].trim());
                     })
                     .collect(Collectors.toMap(DateTimeFormatRegex::getRegex,
                                               DateTimeFormatRegex::getFormat));
    }
}
