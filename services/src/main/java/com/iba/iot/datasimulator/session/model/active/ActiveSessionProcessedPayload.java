package com.iba.iot.datasimulator.session.model.active;

import com.iba.iot.datasimulator.target.model.TargetSystem;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
public class ActiveSessionProcessedPayload {

    /** **/
    private String sessionId;

    /** **/
    private TargetSystem target;

    /** **/
    private String payload;

    /** **/
    private ActiveSessionState state;
}
