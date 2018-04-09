package com.iba.iot.datasimulator.session.model.active.command;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Data
@ToString
public class ActiveSessionManagementCommand {

    @NotNull
    private SessionManagementCommand command;
}
