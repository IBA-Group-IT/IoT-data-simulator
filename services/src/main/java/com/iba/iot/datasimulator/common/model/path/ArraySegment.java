package com.iba.iot.datasimulator.common.model.path;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Collection;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ArraySegment {

    /** **/
    private String arrayName;

    /** **/
    private Collection<Integer> intermediateArrayIndexes;

    /** **/
    private int lastArrayIndex;

}
