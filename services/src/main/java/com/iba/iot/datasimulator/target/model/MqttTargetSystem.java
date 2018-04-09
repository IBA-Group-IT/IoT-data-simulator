package com.iba.iot.datasimulator.target.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.iba.iot.datasimulator.common.model.TargetSystemType;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.NotEmpty;
import org.mongodb.morphia.annotations.Entity;

@Data
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity (value = "targetSystem")
public class MqttTargetSystem extends TargetSystemEntity {

    /** **/
    @JsonView(TargetSystemViews.Short.class)
    private final TargetSystemType type = TargetSystemType.MQTT_BROKER;

    private String url;

    @NotEmpty
    private String topic;
}