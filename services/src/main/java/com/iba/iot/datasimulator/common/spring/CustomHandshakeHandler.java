package com.iba.iot.datasimulator.common.spring;

import com.iba.iot.datasimulator.common.model.auth.AnonymousPrincipal;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;
import java.util.UUID;

/**
 * Provides possibility to have access to principals even without enabled security.
 */
public class CustomHandshakeHandler extends DefaultHandshakeHandler {

    @Override
    protected Principal determineUser(ServerHttpRequest request,
                                      WebSocketHandler wsHandler,
                                      Map<String, Object> attributes) {

        Principal principal = request.getPrincipal();

        if (principal == null) {
            principal = new AnonymousPrincipal();

            String uniqueName = UUID.randomUUID().toString();

            ((AnonymousPrincipal) principal).setName(uniqueName);
        }

        return principal;
    }

}
