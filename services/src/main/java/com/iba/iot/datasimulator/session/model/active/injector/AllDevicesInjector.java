package com.iba.iot.datasimulator.session.model.active.injector;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.iba.iot.datasimulator.session.model.SessionViews;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@ToString
@JsonDeserialize
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AllDevicesInjector implements DeviceInjector {

    @JsonView(SessionViews.Short.class)
    @Getter
    @NotNull
    private final DeviceInjectionRule rule = DeviceInjectionRule.ALL;

    /** **/
    @Min(0L)
    private long delay;
}
