package com.iba.iot.datasimulator.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
public class DateTimeFormatRegex {

    /** **/
    private String regex;

    /** **/
    private String format;
}
