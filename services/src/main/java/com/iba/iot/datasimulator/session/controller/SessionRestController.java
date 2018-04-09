package com.iba.iot.datasimulator.session.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iba.iot.datasimulator.common.model.ErrorResponse;
import com.iba.iot.datasimulator.common.util.CollectionUtil;
import com.iba.iot.datasimulator.session.model.Session;
import com.iba.iot.datasimulator.session.model.SessionCreateUpdateRequest;
import com.iba.iot.datasimulator.session.model.SessionViews;
import com.iba.iot.datasimulator.session.service.SessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.Collection;

@RestController
@RequestMapping(value = "/v1/sessions")
public class SessionRestController {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(SessionRestController.class);

    @Autowired
    private SessionManager sessionManager;

    @Autowired
    private ObjectMapper mapper;

    @RequestMapping(method = RequestMethod.POST)
    public Session create(@RequestBody @Valid @NotNull SessionCreateUpdateRequest sessionCreateUpdateRequest) {

        return sessionManager.create(sessionCreateUpdateRequest);
    }

    @RequestMapping(method = RequestMethod.GET)
    @JsonView(SessionViews.Short.class)
    public Collection<Session> get() {

        return sessionManager.get();
    }

    @RequestMapping(value = "/{sessionId}", method = RequestMethod.GET)
    public Session get(@PathVariable("sessionId") @NotNull String sessionId) {

        return sessionManager.get(sessionId);
    }

    @RequestMapping(value = "/{sessionId}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("sessionId") @NotNull String sessionId) {
        sessionManager.remove(sessionId);
    }

    @RequestMapping(value = "/{sessionId}", method = RequestMethod.PUT)
    public Session update(@PathVariable("sessionId") @NotNull String sessionId,
                          @RequestBody @Valid @NotNull SessionCreateUpdateRequest sessionCreateUpdateRequest) {

       return sessionManager.update(sessionId, sessionCreateUpdateRequest);
    }

    @RequestMapping(value = "/{sessionId}/export", method = RequestMethod.GET)
    public void export(@PathVariable("sessionId") @NotNull String sessionId,
                       HttpServletResponse response) throws IOException {


        logger.debug(">>> Exporting session: {}", sessionId);
        Session session = sessionManager.get(sessionId);
        if (session != null) {
            processSessionExport(response, session);
        } else {

            logger.error(">>> Cannot export session by id {}", sessionId);
            throw new IllegalArgumentException("Non existing session id error.");
        }
    }

    @RequestMapping(value = "/import", method = RequestMethod.POST)
    public void importSession(@RequestBody @Valid @NotNull Session session) throws IOException {

        sessionManager.importSession(session);
    }


    @ExceptionHandler({IllegalArgumentException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorResponse handleException(Exception exception) {
        logger.warn(">>> Wrong request params are provided: {}", exception.getMessage());
        return new ErrorResponse(exception.getMessage());
    }

    /**
     *
     * @param response
     * @param session
     * @throws IOException
     */
    private void processSessionExport(HttpServletResponse response, Session session) throws IOException {

        String sessionJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(session);
        response.setContentType("application/json");
        String fileName = session.getName().toLowerCase() + ".json";
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

        response.getOutputStream().print(sessionJson);
        response.flushBuffer();
    }
}
