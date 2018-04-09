package com.iba.iot.datasimulator.session.factory.timer;

import com.iba.iot.datasimulator.session.model.Session;
import com.iba.iot.datasimulator.session.model.active.timer.IntervalTimer;
import com.iba.iot.datasimulator.session.model.active.timer.Timer;
import com.iba.iot.datasimulator.session.model.active.timer.TimerType;
import com.iba.iot.datasimulator.session.service.active.processing.timer.IntervalTimerProcessor;
import com.iba.iot.datasimulator.session.service.active.processing.timer.TimerProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class IntervalTimerProcessorFactory implements TimerProcessorFactory {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(IntervalTimerProcessorFactory.class);

    @Override
    public TimerProcessor build(Session session) {

        Timer timer = session.getTimer();
        logger.debug(">>> Building interval timer processor from timer: {}", timer);

        IntervalTimer intervalTimer = (IntervalTimer) timer;
        return new IntervalTimerProcessor(intervalTimer);
    }

    @Override
    public TimerType getType() {
        return TimerType.INTERVAL;
    }
}
