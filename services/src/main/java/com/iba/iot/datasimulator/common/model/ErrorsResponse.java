package com.iba.iot.datasimulator.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Collection;

@Data
@AllArgsConstructor
public class ErrorsResponse {

    /** **/
    private Collection<String> messages;
}
