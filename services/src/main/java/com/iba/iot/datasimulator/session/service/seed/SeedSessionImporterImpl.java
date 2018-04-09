package com.iba.iot.datasimulator.session.service.seed;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iba.iot.datasimulator.common.util.FileUtil;
import com.iba.iot.datasimulator.session.model.Session;
import com.iba.iot.datasimulator.session.service.importer.SessionImporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Service
public class SeedSessionImporterImpl implements SeedSessionImporter {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(SeedSessionImporterImpl.class);

    @Value("#{systemEnvironment['IMPORT_DEMO_SESSION'] ?: true}")
    private boolean isNeedToImportSession;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private SessionImporter sessionImporter;

    @Override
    @PostConstruct
    public void process() throws IOException {

        if (isNeedToImportSession) {

            logger.info(">>> Importing demo session.");
            String rawSession = FileUtil.readFile("seed/demo-session.json");
            Session session = mapper.readValue(rawSession, Session.class);

            try {
                sessionImporter.importSession(session);
            } catch (IllegalArgumentException exception) {
                logger.info(">>> Skipping demo session import as it is already presented.");
            }

        } else {
            logger.debug(">>> Skipping demo session import.");
        }
    }
}
