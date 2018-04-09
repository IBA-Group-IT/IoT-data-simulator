package com.iba.iot.datasimulator.session.model.active;

import com.iba.iot.datasimulator.device.model.Device;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ActiveSessionPayload {

    /** **/
    private boolean isDatasetProvided;

    /** **/
    private ActiveSessionState state;

    /** **/
    private String datasetEntry;

    /** **/
    private String generatedPayload;

    /** **/
    private Device device;

    /**
     *
     * @param isDatasetProvided
     * @param datasetEntry
     */
    public ActiveSessionPayload(boolean isDatasetProvided, String datasetEntry) {

        this.isDatasetProvided = isDatasetProvided;
        this.datasetEntry = datasetEntry;
        this.state = ActiveSessionState.RUNNING;
    }

    /**
     *
     * @param state
     */
    public ActiveSessionPayload(ActiveSessionState state) {
        this.state = state;
    }
}
