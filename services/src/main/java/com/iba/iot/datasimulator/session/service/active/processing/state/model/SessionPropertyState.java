package com.iba.iot.datasimulator.session.service.active.processing.state.model;

import jdk.nashorn.api.scripting.JSObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
public class SessionPropertyState {

    /** **/
    private JSObject function;

    /** **/
    private JSObject state;
}
