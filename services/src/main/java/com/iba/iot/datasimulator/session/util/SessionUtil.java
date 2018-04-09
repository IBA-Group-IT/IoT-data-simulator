package com.iba.iot.datasimulator.session.util;

import com.iba.iot.datasimulator.common.model.schema.Schema;
import com.iba.iot.datasimulator.common.model.schema.SchemaRootMetadataType;
import com.iba.iot.datasimulator.session.model.Session;
import com.iba.iot.datasimulator.session.model.active.generator.Generator;
import com.iba.iot.datasimulator.session.model.active.generator.GeneratorType;
import com.iba.iot.datasimulator.session.model.active.generator.SchemaBasedGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class SessionUtil {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(SessionUtil.class);

    /**
     *
     * @param session
     * @return
     */
    public static String getId(Session session) {

        if (session != null) {
            return session.getId().toString();
        }

        logger.error(">>> Cannot get id from null session.");
        throw new RuntimeException("Null session procession error.");
    }

    /**
     *
     * @param session
     * @return
     */
    public static SchemaRootMetadataType getSchemaType(Session session) {

        if (session != null) {

            Schema schema = getSessionSchema(session);
            if (schema != null) {
                return schema.getMetadata().getType();
            }
        }

        logger.error(">>> Cannot get schema type from null session.");
        throw new RuntimeException("Session schema type detecting error.");
    }

    /**
     *
     * @param session
     * @return
     */
    public static Schema getSessionSchema(Session session) {

        if (session != null) {

            Generator generator = session.getGenerator();
            if (generator.getType() == GeneratorType.SCHEMA_BASED) {

                SchemaBasedGenerator schemaBasedGenerator = (SchemaBasedGenerator) generator;
                return schemaBasedGenerator.getSchema();
            }
        }

        return null;
    }
}
