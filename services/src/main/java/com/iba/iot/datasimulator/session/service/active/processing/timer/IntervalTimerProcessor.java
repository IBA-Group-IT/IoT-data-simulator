package com.iba.iot.datasimulator.session.service.active.processing.timer;

import com.iba.iot.datasimulator.common.util.TimeUtil;
import com.iba.iot.datasimulator.session.model.active.timer.IntervalTimer;
import com.iba.iot.datasimulator.session.model.active.timer.Timer;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class IntervalTimerProcessor implements TimerProcessor {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(IntervalTimerProcessor.class);

    /** **/
    private IntervalTimer timer;

    /**
     *
     * @param timer
     */
    public IntervalTimerProcessor(Timer timer) {

        if (!(timer instanceof IntervalTimer)) {

            logger.error(">>> Cannot build interval timer processor from not interval timer entity.");
            throw new RuntimeException("Wrong constructor parameter provided.");
        }

        this.timer = (IntervalTimer) timer;
    }

    @Override
    public long getWaitInterval(String previousEntry, String nextEntry) {

        if (StringUtils.isEmpty(previousEntry) && StringUtils.isNotEmpty(nextEntry)) {
            return 500;
        }

        Long value = this.timer.getValue();
        Long interval = TimeUtil.getIntervalMilliseconds(value, timer.getMetric());

        logger.debug(">>> Interval timer processor calculated interval: {}", interval);
        return interval;
    }
}
