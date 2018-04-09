package com.iba.iot.datasimulator.session.model;

import com.iba.iot.datasimulator.definition.model.DataDefinitionViews;
import com.iba.iot.datasimulator.device.model.DeviceViews;
import com.iba.iot.datasimulator.target.model.TargetSystemViews;

/**
 *
 */
public interface SessionViews {

    /**
     *
     */
    interface Short extends DataDefinitionViews.Short, TargetSystemViews.Short, DeviceViews.Short {}
}
