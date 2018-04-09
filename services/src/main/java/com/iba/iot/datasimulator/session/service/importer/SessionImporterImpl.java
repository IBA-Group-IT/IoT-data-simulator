package com.iba.iot.datasimulator.session.service.importer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iba.iot.datasimulator.common.model.schema.Schema;
import com.iba.iot.datasimulator.definition.model.DataDefinition;
import com.iba.iot.datasimulator.definition.model.Dataset;
import com.iba.iot.datasimulator.definition.service.DataDefinitionManager;
import com.iba.iot.datasimulator.definition.service.DatasetManager;
import com.iba.iot.datasimulator.device.model.Device;
import com.iba.iot.datasimulator.device.service.DeviceManager;
import com.iba.iot.datasimulator.session.model.Session;
import com.iba.iot.datasimulator.session.service.SessionManager;
import com.iba.iot.datasimulator.target.model.TargetSystem;
import com.iba.iot.datasimulator.target.model.TargetSystemEntity;
import com.iba.iot.datasimulator.target.service.TargetSystemManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collection;

@Service
public class SessionImporterImpl implements SessionImporter {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(SessionImporterImpl.class);

    @Autowired
    private DataDefinitionManager dataDefinitionManager;

    @Autowired
    private DatasetManager datasetManager;

    @Autowired
    private SessionManager sessionManager;

    @Autowired
    private DeviceManager deviceManager;

    @Autowired
    @Qualifier("dataDefinitionMapper")
    private ObjectMapper mapper;

    @Autowired
    private TargetSystemManager targetSystemManager;

    @Override
    public void importSession(Session session) throws IOException {

        String sessionId = session.getId().toString();
        logger.debug(">>> Importing session {}", sessionId);

        checkSessionExistance(sessionId);
        processDataDefinitionImport(session);
        processDevices(session);
        processTargetSystem(session);
        processSession(session);
    }

    /**
     *
     * @param sessionId
     */
    private void checkSessionExistance(String sessionId) {

        Session existingSession = sessionManager.get(sessionId);
        if (existingSession != null) {

            logger.warn(">>> Cannot import already existing session {}", sessionId);
            throw new IllegalArgumentException("Cannot import already existing session");
        }
    }

    /**
     *
     * @param session
     */
    private void processDataDefinitionImport(Session session) throws IOException {

        logger.debug(">>> Processing data definition import...");

        DataDefinition dataDefinition = session.getDataDefinition();
        if (dataDefinition != null) {

            String dataDefinitionId = dataDefinition.getId().toString();
            DataDefinition existingDataDefinition = dataDefinitionManager.get(dataDefinitionId);
            if (existingDataDefinition == null) {

                processDatasetImport(dataDefinition);

                /**
                 * Data definition properties are deserialized as SessionSchemaProperty.
                 * Converting it to proper SchemaProperty type
                 */
                Schema schema = dataDefinition.getSchema();
                if (schema != null) {
                    String raw = mapper.writeValueAsString(schema);
                    Schema convertedSchema = mapper.readValue(raw, Schema.class);
                    dataDefinition.setSchema(convertedSchema);
                }

                dataDefinitionManager.create(dataDefinition);

            } else {
                logger.debug(">>> Skipping data definition {} import as it is already exist", dataDefinitionId);
            }
        }
    }

    /**
     *
     * @param dataDefinition
     */
    private void processDatasetImport(DataDefinition dataDefinition) {

        Dataset dataset = dataDefinition.getDataset();
        if (dataset != null) {

            String datasetId = dataset.getId().toString();
            Dataset existingDataset = datasetManager.get(datasetId);
            if (existingDataset == null) {

                /**
                 * Data definition has reference to non existing dataset.
                 * Trying to find dataset by the same name and type and use it instead of original one.
                 */
                Collection<Dataset> replacementDatasets = datasetManager.get(dataset.getName(), dataset.getType().toString());
                if (replacementDatasets != null && !replacementDatasets.isEmpty()) {

                    Dataset replacementDataset = replacementDatasets.iterator().next();
                    dataDefinition.setDataset(replacementDataset);

                } else {

                    if (dataDefinition.getSchema() != null) {

                        //There is no datasets with the same name and type. But at least schema provided in data definition.
                        // So, without dataset data definition is still valid. Removing dataset reference from data definition.
                        dataDefinition.setDataset(null);

                    } else {

                        logger.error("Cannot proceed with data definition import due to empty schema and non existing dataset.");
                        throw new RuntimeException("Session import error. Please import dataset first.");
                    }
                }
            }

        } else {
            logger.debug(">>> Skipping dataset {} import as it is already exist", dataset);
        }
    }

    /**
     *
     * @param session
     */
    private void processDevices(Session session) {

        logger.debug(">>> Processing devices import...");

        Collection<Device> devices = session.getDevices();
        if (devices != null) {

            for (Device device : devices) {

                String deviceId = device.getId().toString();
                Device existingDevice = deviceManager.get(deviceId);
                if (existingDevice == null) {
                    deviceManager.create(device);
                } else {
                    logger.debug(">>> Skipping device {} import as it is already exist", deviceId);
                }
            }
        }
    }

    /**
     *
     * @param session
     */
    private void processTargetSystem(Session session) {

        logger.debug(">>> Processing target system import...");
        TargetSystemEntity targetSystem = (TargetSystemEntity) session.getTargetSystem();

        String targetSystemId = targetSystem.getId().toString();
        TargetSystem existingTargetSystem = targetSystemManager.get(targetSystemId);
        if (existingTargetSystem == null) {
            targetSystemManager.create(targetSystem);
        } else {
            logger.debug(">>> Skipping target system {} import as it is already exist", targetSystemId);
        }
    }

    /**
     *
     * @param session
     */
    private void processSession(Session session) {

        logger.debug(">>> Processing Whole session import...");
        sessionManager.create(session);
    }
}
