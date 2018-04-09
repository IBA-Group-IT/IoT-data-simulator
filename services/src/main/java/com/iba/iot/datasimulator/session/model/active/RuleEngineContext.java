package com.iba.iot.datasimulator.session.model.active;

import com.iba.iot.datasimulator.common.model.schema.Schema;
import com.iba.iot.datasimulator.session.service.active.processing.state.SessionStateManager;
import lombok.Getter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@ToString
public class RuleEngineContext {

    /** **/
    @Getter
    private String sessionId;

    /** **/
    @Getter
    private boolean isDatasetProvided;

    @Getter
    private Schema schema;

    @Getter
    private String datasetEntry;

    @Getter
    private Map<String, Object> processingResults = new HashMap<>();

    @Getter
    private Map<String, Long> datasetEntryTimestamps = new HashMap<>();

    @Getter
    private SessionStateManager sessionStateManager;

    /**
     *
     * @param sessionId
     * @param payload
     * @param schema
     * @param sessionStateManager
     */
    public RuleEngineContext(String sessionId, ActiveSessionPayload payload, Schema schema, SessionStateManager sessionStateManager) {

        this.sessionId = sessionId;
        this.isDatasetProvided = payload.isDatasetProvided();
        this.schema = schema;
        this.datasetEntry = payload.getDatasetEntry();
        this.sessionStateManager = sessionStateManager;
    }
}
