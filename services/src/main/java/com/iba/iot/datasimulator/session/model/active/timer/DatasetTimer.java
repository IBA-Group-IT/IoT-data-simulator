package com.iba.iot.datasimulator.session.model.active.timer;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.iba.iot.datasimulator.session.model.SessionViews;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;

@Data
@ToString
@JsonDeserialize
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DatasetTimer implements Timer {

    @JsonView(SessionViews.Short.class)
    @Getter
    private final TimerType type = TimerType.DATASET_PROVIDED;

    @JsonView(SessionViews.Short.class)
    @NotEmpty
    private String datePosition;

    @JsonView(SessionViews.Short.class)
    @DecimalMin("0.0000001")
    private BigDecimal replayRate = BigDecimal.valueOf(1);
}
