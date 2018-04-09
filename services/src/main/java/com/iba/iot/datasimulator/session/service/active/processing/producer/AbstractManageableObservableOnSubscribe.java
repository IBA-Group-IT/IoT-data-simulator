package com.iba.iot.datasimulator.session.service.active.processing.producer;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractManageableObservableOnSubscribe<T> implements ManageableObservableOnSubscribe<T> {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(AbstractManageableObservableOnSubscribe.class);

    /** **/
    private static final long PAUSE_SLEEP_INTERVAL = 1000L;

    /** **/
    private static final long WAIT_SLEEP_INTERVAL = 200L;

    /** **/
    private ObservableState state = ObservableState.RUNNING;

    /**
     *
     * @throws InterruptedException
     */
    protected void processPause() throws InterruptedException {

        while (state == ObservableState.PAUSED) {

            logger.debug(">>> Observable is on pause. Thread going to sleep for {} ms.", PAUSE_SLEEP_INTERVAL);
            Thread.sleep(PAUSE_SLEEP_INTERVAL);
        }
    }

    /**
     *
     * @param waitInterval
     * @return
     */
    protected boolean processWaiting(long waitInterval) throws InterruptedException {

        while (waitInterval > 0) {

            if (waitInterval >= WAIT_SLEEP_INTERVAL) {

                waitInterval -= WAIT_SLEEP_INTERVAL;
                Thread.sleep(WAIT_SLEEP_INTERVAL);

            } else {

                Thread.sleep(waitInterval);
                waitInterval = 0;
            }

            if (state == ObservableState.STOPPED) {
                return false;
            }
        }

        return true;
    }

    @Override
    public ObservableState getState() {
        return state;
    }

    /**
     *
     */
    public void pause(){

        if (state == ObservableState.RUNNING) {

            logger.debug(">>> Pausing observable");
            state = ObservableState.PAUSED;
        }
    }

    /**
     *
     */
    public void resume(){

        if (state == ObservableState.PAUSED) {

            logger.debug(">>> Resuming observable");
            state = ObservableState.RUNNING;
        }
    }

    /**
     *
     */
    public void stop() {

        if (state != ObservableState.STOPPED) {

            logger.debug(">>> Stopping observable");
            state = ObservableState.STOPPED;
        }
    }

}
