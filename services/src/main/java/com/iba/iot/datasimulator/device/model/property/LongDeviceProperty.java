package com.iba.iot.datasimulator.device.model.property;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

@ToString
@JsonDeserialize
public class LongDeviceProperty implements DeviceProperty<Long> {

    @NotEmpty
    @Getter
    @Setter
    private String name;

    @NotNull
    @Getter
    @Setter
    private Long value;
}
