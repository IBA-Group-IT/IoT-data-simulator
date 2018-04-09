package com.iba.iot.datasimulator.session.model.active.timer;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.iba.iot.datasimulator.common.model.IntervalMetric;
import com.iba.iot.datasimulator.session.model.SessionViews;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Data
@ToString
@JsonDeserialize
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IntervalTimer implements Timer {

    @JsonView(SessionViews.Short.class)
    @Getter
    private TimerType type = TimerType.INTERVAL;

    @JsonView(SessionViews.Short.class)
    private IntervalMetric metric;

    @JsonView(SessionViews.Short.class)
    @NotNull
    private Long value;
}
