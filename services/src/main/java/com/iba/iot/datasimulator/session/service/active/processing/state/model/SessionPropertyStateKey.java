package com.iba.iot.datasimulator.session.service.active.processing.state.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class SessionPropertyStateKey {

    /** **/
    private String sessionId;

    /** **/
    private String propertyPath;

    /** **/
    private String deviceName;
}
