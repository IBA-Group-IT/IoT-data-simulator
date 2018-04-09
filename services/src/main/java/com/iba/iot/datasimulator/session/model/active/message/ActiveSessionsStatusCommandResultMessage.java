package com.iba.iot.datasimulator.session.model.active.message;

import com.iba.iot.datasimulator.session.model.active.ActiveSessionStatus;
import com.iba.iot.datasimulator.session.model.active.command.CommandResult;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.Collection;

@Data
@ToString
@NoArgsConstructor
public class ActiveSessionsStatusCommandResultMessage implements CommandResultMessage {

    /** **/
    private final MessageType type = MessageType.SESSIONS_STATUS_COMMAND_RESULT;

    @NotNull
    private Collection<ActiveSessionStatus> statuses;

    /** **/
    private CommandResult result;

    /** **/
    private String errorMessage;

    /**
     *
     * @param statuses
     * @param result
     */
    public ActiveSessionsStatusCommandResultMessage(Collection<ActiveSessionStatus> statuses, CommandResult result) {
        this.statuses = statuses;
        this.result = result;
    }

    /**
     *
     * @param statuses
     * @param result
     * @param errorMessage
     */
    public ActiveSessionsStatusCommandResultMessage(Collection<ActiveSessionStatus> statuses, CommandResult result, String errorMessage) {
        this.statuses = statuses;
        this.result = result;
        this.errorMessage = errorMessage;
    }
}
