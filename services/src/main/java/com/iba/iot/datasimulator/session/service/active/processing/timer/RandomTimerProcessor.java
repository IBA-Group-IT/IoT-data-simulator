package com.iba.iot.datasimulator.session.service.active.processing.timer;

import com.iba.iot.datasimulator.common.util.MathUtil;
import com.iba.iot.datasimulator.common.util.TimeUtil;
import com.iba.iot.datasimulator.session.model.active.timer.RandomTimer;
import com.iba.iot.datasimulator.session.model.active.timer.Timer;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.ParseException;

/**
 *
 */
public class RandomTimerProcessor implements TimerProcessor {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(RandomTimerProcessor.class);

    /** **/
    private RandomTimer timer;

    /**
     *
     * @param timer
     */
    public RandomTimerProcessor(Timer timer) {

        if (!(timer instanceof RandomTimer)) {

            logger.error(">>> Cannot build random timer processor from not random timer entity.");
            throw new RuntimeException("Wrong constructor parameter provided.");
        }

        this.timer = (RandomTimer) timer;
    }

    @Override
    public long getWaitInterval(String previousEntry, String nextEntry) throws ParseException, IOException {

        if (StringUtils.isEmpty(previousEntry) && StringUtils.isNotEmpty(nextEntry)) {
            return 0;
        }

        long randomValue = MathUtil.randomLong(timer.getMin(), timer.getMax());
        long interval = TimeUtil.getIntervalMilliseconds(randomValue, timer.getMetric());

        logger.debug(">>> Random timer processor calculated interval: {}", interval);
        return interval;
    }
}
