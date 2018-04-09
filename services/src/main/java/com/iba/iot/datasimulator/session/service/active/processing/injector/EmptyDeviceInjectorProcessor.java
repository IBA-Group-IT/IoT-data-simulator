package com.iba.iot.datasimulator.session.service.active.processing.injector;

import com.iba.iot.datasimulator.session.model.active.ActiveSessionPayload;
import io.reactivex.Observable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class EmptyDeviceInjectorProcessor implements DeviceInjectionProcessor {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(EmptyDeviceInjectorProcessor.class);

    @Override
    public Observable<ActiveSessionPayload> process(ActiveSessionPayload payload) {

        logger.debug(">>> Empty device injection processor: processing payload {}", payload);
        return Observable.just(payload);
    }
}
