package com.iba.iot.datasimulator.session.model.active.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ActiveSessionManagementCommand {

    @NotNull
    private SessionManagementCommand command;
}
