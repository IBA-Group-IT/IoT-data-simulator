package com.iba.iot.datasimulator.session.model.active.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ActiveSessionAnalyticsMessage implements ActiveSessionMessage {

    /** **/
    private final MessageType type = MessageType.SESSION_ANALYTICS;

    @NotNull
    private ActiveSessionAnalyticTag tag;

    @NotEmpty
    private String sessionId;

    @NotEmpty
    private String message;

    @NotEmpty
    private long timestamp;

}
