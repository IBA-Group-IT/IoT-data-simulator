package com.iba.iot.datasimulator.session.service.importer;

import com.iba.iot.datasimulator.session.model.Session;

import java.io.IOException;

/**
 *
 */
public interface SessionImporter {

    /**
     *
     * @param session
     */
    void importSession(Session session) throws IOException;

}
