package com.iba.iot.datasimulator.session.factory.timer;

import com.iba.iot.datasimulator.session.model.Session;
import com.iba.iot.datasimulator.session.model.active.timer.RandomTimer;
import com.iba.iot.datasimulator.session.model.active.timer.Timer;
import com.iba.iot.datasimulator.session.model.active.timer.TimerType;
import com.iba.iot.datasimulator.session.service.active.processing.timer.RandomTimerProcessor;
import com.iba.iot.datasimulator.session.service.active.processing.timer.TimerProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class RandomTimerProcessorFactory implements TimerProcessorFactory {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(RandomTimerProcessorFactory.class);

    @Override
    public TimerProcessor build(Session session) {

        Timer timer = session.getTimer();
        logger.debug(">>> Building random timer processor from timer: {}", timer);

        RandomTimer randomTimer = (RandomTimer) timer;
        return new RandomTimerProcessor(randomTimer);
    }

    @Override
    public TimerType getType() {
        return TimerType.RANDOM;
    }
}
