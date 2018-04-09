package com.iba.iot.datasimulator.session.service.active.processing.timer;

import java.io.IOException;
import java.text.ParseException;

/**
 *
 */
public interface TimerProcessor  {

    /**
     *
     * @param previousEntry
     * @param nextEntry
     * @return
     */
    long getWaitInterval(String previousEntry, String nextEntry) throws ParseException, IOException;
}
