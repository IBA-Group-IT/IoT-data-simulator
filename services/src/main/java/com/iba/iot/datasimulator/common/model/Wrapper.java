package com.iba.iot.datasimulator.common.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Wrapper<T> {

    /** **/
    T value;
}
