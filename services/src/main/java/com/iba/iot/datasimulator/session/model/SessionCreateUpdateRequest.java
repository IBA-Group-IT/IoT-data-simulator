package com.iba.iot.datasimulator.session.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.iba.iot.datasimulator.session.model.active.filter.DatasetFilter;
import com.iba.iot.datasimulator.session.model.active.generator.Generator;
import com.iba.iot.datasimulator.session.model.active.injector.DeviceInjector;
import com.iba.iot.datasimulator.session.model.active.timer.Timer;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Collection;

@Data
@ToString
public class SessionCreateUpdateRequest {

    @NotEmpty
    private String name;

    /** **/
    private String dataDefinitionId;

    @NotNull
    @Valid
    private Timer timer;

    /** **/
    private int ticksNumber;

    @JsonProperty(value="isReplayLooped")
    private boolean isReplayLooped;

    @Valid
    private DatasetFilter datasetFilter;

    /** **/
    private Collection<String> devices;

    @Valid
    private DeviceInjector deviceInjector;

    @NotNull
    @Valid
    private Generator generator;

    @NotEmpty
    private String targetSystemId;
}
