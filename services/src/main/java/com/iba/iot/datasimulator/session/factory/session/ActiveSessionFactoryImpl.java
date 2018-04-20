package com.iba.iot.datasimulator.session.factory.session;

import com.iba.iot.datasimulator.definition.model.DataDefinition;
import com.iba.iot.datasimulator.definition.model.Dataset;
import com.iba.iot.datasimulator.session.dao.SessionDao;
import com.iba.iot.datasimulator.session.factory.injector.DeviceInjectionProcessorFactory;
import com.iba.iot.datasimulator.session.factory.generator.SessionPayloadGeneratorFactory;
import com.iba.iot.datasimulator.session.factory.timer.TimerProcessorFactory;
import com.iba.iot.datasimulator.session.factory.reader.DatasetReaderFactory;
import com.iba.iot.datasimulator.session.model.Session;
import com.iba.iot.datasimulator.session.model.active.ActiveSessionPayload;
import com.iba.iot.datasimulator.session.model.active.timer.Timer;
import com.iba.iot.datasimulator.session.model.active.timer.TimerType;
import com.iba.iot.datasimulator.session.service.active.entity.ActiveSession;
import com.iba.iot.datasimulator.session.service.active.entity.ActiveSessionEntity;
import com.iba.iot.datasimulator.session.service.active.processing.generator.SessionPayloadGenerator;
import com.iba.iot.datasimulator.session.service.active.processing.injector.DeviceInjectionProcessor;
import com.iba.iot.datasimulator.session.service.active.processing.producer.DataProducer;
import com.iba.iot.datasimulator.session.service.active.processing.reader.DatasetReader;
import com.iba.iot.datasimulator.session.service.active.processing.timer.TimerProcessor;
import com.iba.iot.datasimulator.session.util.SessionUtil;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ActiveSessionFactoryImpl implements ActiveSessionFactory {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(ActiveSessionFactoryImpl.class);

    @Autowired
    private BeanFactory beanFactory;

    @Autowired
    private SessionDao sessionDao;

    @Autowired
    private Map<TimerType, TimerProcessorFactory> timerProcessorFactories;

    @Autowired
    private DatasetReaderFactory datasetReaderFactory;

    @Autowired
    private DeviceInjectionProcessorFactory deviceInjectionProcessorFactory;

    @Autowired
    private SessionPayloadGeneratorFactory sessionPayloadGeneratorFactory;

    @Override
    public ActiveSession build(String sessionId) {

        Session session = fetchSession(sessionId);

        DatasetReader<String> datasetReader = buildDatasetReader(session);
        TimerProcessor timerProcessor = buildTimerProcessor(session);

        DataProducer dataProducer = beanFactory.getBean(DataProducer.class, datasetReader, timerProcessor, session.getTicksNumber());
        DeviceInjectionProcessor deviceInjectorProcessor = buildDeviceInjectionProcessor(session);
        SessionPayloadGenerator sessionPayloadGenerator = sessionPayloadGeneratorFactory.build(session);

        Observable<ActiveSessionPayload> processingPipeline = buildProcessingPipeline(dataProducer, deviceInjectorProcessor, sessionPayloadGenerator);

        logger.debug(">>> Building new active session with id {}", sessionId);
        return beanFactory.getBean(ActiveSessionEntity.class, session, dataProducer, processingPipeline);
    }

    /**
     *
     * @param sessionId
     * @return
     */
    private Session fetchSession(String sessionId) {

        Session session = sessionDao.get(sessionId);
        if (session == null) {

            logger.error(">>> Couldn't find session by provided id: {}", sessionId);
            throw new RuntimeException("Session " + sessionId + " isn't existed.");
        }

        logger.debug(">>> Fetched session {} content: {}", sessionId, session);
        return session;
    }

    /**
     *
     * @param session
     * @return
     */
    private DatasetReader<String> buildDatasetReader(Session session) {

        DataDefinition dataDefinition = session.getDataDefinition();
        if (dataDefinition != null) {

            Dataset dataset = dataDefinition.getDataset();
            if (dataset != null) {
                return datasetReaderFactory.buildStringDatasetReader(dataset, session.getDatasetFilter(), SessionUtil.getSessionSchema(session));
            }
        }

        return null;
    }

    /**
     *
     * @param session
     * @return
     */
    private TimerProcessor buildTimerProcessor(Session session) {

        Timer timer = session.getTimer();
        return timerProcessorFactories.get(timer.getType()).build(session);
    }

    /**
     *
     * @param session
     * @return
     */
    private DeviceInjectionProcessor buildDeviceInjectionProcessor(Session session) {
        return deviceInjectionProcessorFactory.build(session);
    }

    /**
     *  @param dataProducer
     * @param deviceInjectionProcessor
     * @param sessionPayloadGenerator
     */
    private Observable<ActiveSessionPayload> buildProcessingPipeline(DataProducer dataProducer,
                                                                     DeviceInjectionProcessor deviceInjectionProcessor,
                                                                     SessionPayloadGenerator sessionPayloadGenerator) {

        return Observable.create(dataProducer)
                         .flatMap(deviceInjectionProcessor::process)
                         .map(sessionPayloadGenerator::process)
                         .subscribeOn(Schedulers.newThread());
    }
}
