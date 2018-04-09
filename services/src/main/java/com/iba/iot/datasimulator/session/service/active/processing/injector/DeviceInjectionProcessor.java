package com.iba.iot.datasimulator.session.service.active.processing.injector;

import com.iba.iot.datasimulator.session.model.active.ActiveSessionPayload;
import io.reactivex.Observable;

import java.io.IOException;

/**
 *
 */
public interface DeviceInjectionProcessor {

    /**
     *
     * @param payload
     * @return
     */
    Observable<ActiveSessionPayload> process(ActiveSessionPayload payload) throws IOException;
}
