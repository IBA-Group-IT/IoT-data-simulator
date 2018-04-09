package com.iba.iot.datasimulator.common.model.path;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.util.Collection;

@Data
@ToString
@AllArgsConstructor
public class JsonPathSegments {

    /** **/
    private Collection<String> intermediateSegments;

    /** **/
    private String terminalSegment;
}
