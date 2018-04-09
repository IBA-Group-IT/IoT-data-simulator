package com.iba.iot.datasimulator.session.factory.timer;

import com.iba.iot.datasimulator.common.model.TypedEntity;
import com.iba.iot.datasimulator.session.factory.SessionHandlerFactory;
import com.iba.iot.datasimulator.session.model.Session;
import com.iba.iot.datasimulator.session.model.active.timer.TimerType;
import com.iba.iot.datasimulator.session.service.active.processing.timer.TimerProcessor;

/**
 *
 */
public interface TimerProcessorFactory extends SessionHandlerFactory<TimerProcessor>, TypedEntity<TimerType> {

    /**
     *
     * @param session
     * @return
     */
    TimerProcessor build(Session session);
}
