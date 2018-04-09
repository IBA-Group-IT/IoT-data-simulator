package com.iba.iot.datasimulator.session.factory.session;


import com.iba.iot.datasimulator.session.model.Session;
import com.iba.iot.datasimulator.session.model.SessionCreateUpdateRequest;

public interface SessionFactory {

    Session buildFromCreateUpdateRequest(SessionCreateUpdateRequest sessionCreateUpdateRequest, String sessionId);
}
