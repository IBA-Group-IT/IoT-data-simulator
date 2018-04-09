package com.iba.iot.datasimulator.session.model.active.message;

import com.iba.iot.datasimulator.session.model.active.ActiveSessionStatus;
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
public class ActiveSessionStatusMessage implements Message {

    /** **/
    private final MessageType type = MessageType.SESSION_STATUS;

    @NotNull
    private ActiveSessionStatus status;

    @NotEmpty
    private long timestamp;
}
