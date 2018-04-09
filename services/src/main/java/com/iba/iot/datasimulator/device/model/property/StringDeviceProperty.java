package com.iba.iot.datasimulator.device.model.property;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotEmpty;

@ToString
@JsonDeserialize
public class StringDeviceProperty implements DeviceProperty<String> {

    @NotEmpty
    @Getter
    @Setter
    private String name;

    @NotEmpty
    @Getter
    @Setter
    private String value;

}
