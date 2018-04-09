package com.iba.iot.datasimulator.session.factory.timer;

import com.iba.iot.datasimulator.common.model.schema.Schema;
import com.iba.iot.datasimulator.session.model.Session;
import com.iba.iot.datasimulator.session.model.active.timer.DatasetTimer;
import com.iba.iot.datasimulator.session.model.active.timer.Timer;
import com.iba.iot.datasimulator.session.model.active.timer.TimerType;
import com.iba.iot.datasimulator.session.service.active.processing.timer.DatasetTimerProcessor;
import com.iba.iot.datasimulator.session.service.active.processing.timer.TimerProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DatasetTimerProcessorFactory implements TimerProcessorFactory {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(DatasetTimerProcessorFactory.class);

    @Autowired
    private BeanFactory beanFactory;

    @Override
    public TimerProcessor build(Session session) {

        Timer timer = session.getTimer();
        logger.debug(">>> Building dataset timer processor from timer: {}", timer);

        DatasetTimer datasetTimer = (DatasetTimer) timer;
        Schema schema = session.getDataDefinition().getSchema();

        return beanFactory.getBean(DatasetTimerProcessor.class, datasetTimer, schema);
    }

    @Override
    public TimerType getType() {
        return TimerType.DATASET_PROVIDED;
    }
}
