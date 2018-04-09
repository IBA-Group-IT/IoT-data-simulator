package com.iba.iot.datasimulator.session.service.active.processing.producer;

/**
 *
 * @param <T>
 */
public interface ManageableObservableOnSubscribe<T> extends io.reactivex.ObservableOnSubscribe<T> {

    /**
     *
     */
    void pause();

    /**
     *
     */
    void resume();

    /**
     *
     */
    void stop();

    /**
     *
     * @return
     */
    ObservableState getState();
}
