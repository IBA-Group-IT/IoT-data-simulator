package com.iba.iot.datasimulator.session.factory.generator;

import com.iba.iot.datasimulator.session.model.Session;
import com.iba.iot.datasimulator.session.model.active.generator.Generator;
import com.iba.iot.datasimulator.session.model.active.generator.JsFunctionGenerator;
import com.iba.iot.datasimulator.session.model.active.generator.SchemaBasedGenerator;
import com.iba.iot.datasimulator.session.service.active.processing.generator.SessionPayloadGenerator;
import com.iba.iot.datasimulator.session.service.active.processing.generator.function.SessionFunctionPayloadGenerator;
import com.iba.iot.datasimulator.session.service.active.processing.generator.schema.SessionSchemaPayloadGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SessionPayloadGeneratorFactoryImpl implements SessionPayloadGeneratorFactory {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(SessionPayloadGeneratorFactoryImpl.class);

    @Autowired
    private BeanFactory beanFactory;

    @Override
    public SessionPayloadGenerator build(Session session) {

        String sessionId = session.getId().toString();
        logger.debug(">>> Building session rule engine factory for session {}", sessionId);

        Generator generator = session.getGenerator();
        switch (generator.getType()) {

            case SCHEMA_BASED:
                SchemaBasedGenerator schemaBasedGenerator = (SchemaBasedGenerator) generator;
                return beanFactory.getBean(SessionSchemaPayloadGenerator.class, sessionId, schemaBasedGenerator.getSchema());

            case JS_FUNCTION:
                JsFunctionGenerator jsFunctionGenerator = (JsFunctionGenerator) generator;
                return beanFactory.getBean(SessionFunctionPayloadGenerator.class, sessionId, jsFunctionGenerator.getJsFunction());
        }

        logger.error(">>> Cannot build session payload generator due to missing schema and function properties.");
        throw new RuntimeException("Session payload generator building error.");
    }
}
