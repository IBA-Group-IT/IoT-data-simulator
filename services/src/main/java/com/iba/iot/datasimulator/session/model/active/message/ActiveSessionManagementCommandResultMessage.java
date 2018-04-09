package com.iba.iot.datasimulator.session.model.active.message;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.iba.iot.datasimulator.session.model.active.command.CommandResult;
import lombok.*;
import org.hibernate.validator.constraints.NotEmpty;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ActiveSessionManagementCommandResultMessage implements CommandResultMessage {

    /** **/
    private final MessageType type = MessageType.SESSION_MANAGEMENT_COMMAND_RESULT;

    /**
     *
     * @param sessionId
     * @param result
     */
    public ActiveSessionManagementCommandResultMessage(String sessionId, CommandResult result) {
        this.sessionId = sessionId;
        this.result = result;
    }

    @NotEmpty
    private String sessionId;

    @NotEmpty
    private CommandResult result;

    /** **/
    private String errorMessage;
}
