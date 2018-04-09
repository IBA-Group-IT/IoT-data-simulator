package com.iba.iot.datasimulator.session.model.active.timer;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.iba.iot.datasimulator.session.model.SessionViews;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotEmpty;

@Data
@ToString
@JsonDeserialize
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomFunctionTimer implements Timer {

    @JsonView(SessionViews.Short.class)
    @Getter
    private TimerType type = TimerType.CUSTOM_FUNCTION;

    @JsonView(SessionViews.Short.class)
    @NotEmpty
    private String function;
}
