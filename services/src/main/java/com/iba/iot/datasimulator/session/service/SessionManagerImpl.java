package com.iba.iot.datasimulator.session.service;

import com.iba.iot.datasimulator.common.model.schema.Schema;
import com.iba.iot.datasimulator.common.model.schema.property.SchemaProperty;
import com.iba.iot.datasimulator.common.model.schema.property.SessionSchemaProperty;
import com.iba.iot.datasimulator.common.model.schema.property.rule.SchemaPropertyRule;
import com.iba.iot.datasimulator.common.model.schema.property.rule.SchemaPropertyRuleType;
import com.iba.iot.datasimulator.common.service.schema.lookup.SchemaPropertyLookupService;
import com.iba.iot.datasimulator.common.service.schema.manager.SchemaManager;
import com.iba.iot.datasimulator.common.service.schema.parser.SchemaParser;
import com.iba.iot.datasimulator.common.util.ModelEntityUtil;
import com.iba.iot.datasimulator.common.util.SchemaPropertyUtil;
import com.iba.iot.datasimulator.definition.model.DataDefinition;
import com.iba.iot.datasimulator.session.dao.SessionDao;
import com.iba.iot.datasimulator.session.factory.session.SessionFactory;
import com.iba.iot.datasimulator.session.model.Session;
import com.iba.iot.datasimulator.session.model.SessionCreateUpdateRequest;
import com.iba.iot.datasimulator.session.model.active.generator.Generator;
import com.iba.iot.datasimulator.session.model.active.generator.GeneratorType;
import com.iba.iot.datasimulator.session.model.active.generator.SchemaBasedGenerator;
import com.iba.iot.datasimulator.session.model.unfold.UnfoldedSchemaProperty;
import com.iba.iot.datasimulator.session.service.importer.SessionImporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collection;

@Service
public class SessionManagerImpl implements SessionManager {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(SessionManagerImpl.class);

    @Autowired
    private SessionDao sessionDao;

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private SchemaManager schemaManager;

    @Autowired
    private SchemaPropertyLookupService schemaPropertyLookupService;

    @Autowired
    private SessionImporter sessionImporter;

    @Autowired
    private SchemaParser<UnfoldedSchemaProperty> schemaParser;

    @Override
    public Session create(SessionCreateUpdateRequest sessionCreateUpdateRequest) {

        logger.debug(">>> Creating new session {}", sessionCreateUpdateRequest);

        Session session = sessionFactory.buildFromCreateUpdateRequest(sessionCreateUpdateRequest, null);
        return sessionDao.save(session);
    }

    @Override
    public void create(Session session) {
        sessionDao.save(session);
    }

    @Override
    public Session update(String sessionId, SessionCreateUpdateRequest sessionCreateUpdateRequest) {

        logger.debug(">>> Updating session {}", sessionCreateUpdateRequest);

        Session session = sessionFactory.buildFromCreateUpdateRequest(sessionCreateUpdateRequest, sessionId);
        return sessionDao.update(session);
    }

    @Override
    public Collection<Session> get() {

        logger.debug(">>> Get sessions");
        return ModelEntityUtil.sortByModified(sessionDao.get());
    }

    @Override
    public Session get(String sessionId) {

        logger.debug(">>> Get session by id {}", sessionId);
        return sessionDao.get(sessionId);

    }

    @Override
    public void remove(String sessionId) {

        logger.debug(">>> Removing session by id: {}", sessionId);
        sessionDao.remove(sessionId);
    }

    @Override
    public void updateSessionsSchema(DataDefinition dataDefinition) throws IOException {

        logger.debug(">>> Updating sessions schema due to data definition update: {}", dataDefinition);

        Collection<Session> referencedSessions = sessionDao.getByDataDefinitionId(dataDefinition.getId().toString());
        if (!referencedSessions.isEmpty()) {

            Schema newSchema = schemaManager.populateSchemaDefaultRules(dataDefinition);
            referencedSessions.forEach(session -> {

                Generator generator = session.getGenerator();
                if (generator.getType() == GeneratorType.SCHEMA_BASED) {

                    SchemaBasedGenerator schemaBasedGenerator = (SchemaBasedGenerator) generator;
                    Schema oldSchema = schemaBasedGenerator.getSchema();
                    updateSessionSchema(newSchema, oldSchema);
                    schemaBasedGenerator.setSchema(newSchema);
                    sessionDao.save(session);
                }
            });
        }
    }

    /**
     *
     * @param newSchema
     * @param oldSchema
     */
    private void updateSessionSchema(Schema newSchema, Schema oldSchema) {

        schemaParser.parse(newSchema).forEach(newSchemaUnfoldedProperty -> {

            String newSchemaPropertyPosition = newSchemaUnfoldedProperty.getPosition();
            SessionSchemaProperty newSchemaProperty = (SessionSchemaProperty) newSchemaUnfoldedProperty.getProperty();
            SessionSchemaProperty oldSchemaProperty = (SessionSchemaProperty) schemaPropertyLookupService.findProperty(newSchemaPropertyPosition, oldSchema);

            if (oldSchemaProperty != null &&
                oldSchemaProperty.getRule() != null && newSchemaProperty.getRule() != null &&
                (SchemaPropertyUtil.isSamePropertyType(newSchemaProperty, oldSchemaProperty) ||
                // In case of CUSTOM_FUNCTION rule should remains
                oldSchemaProperty.getRule().getType() == SchemaPropertyRuleType.CUSTOM_FUNCTION)) {

                    // copy previous schema rule
                    SchemaPropertyRule oldPropertyRule = oldSchemaProperty.getRule();
                    newSchemaProperty.setRule(oldPropertyRule);
            }
        });
    }

    @Override
    public void importSession(Session session) throws IOException {
        sessionImporter.importSession(session);
    }
}
