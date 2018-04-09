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
@Entity(value = "targetSystem")
public class AmqpTargetSystem extends TargetSystemEntity {

    /** **/
    @JsonView(TargetSystemViews.Short.class)
    private final TargetSystemType type = TargetSystemType.AMQP_BROKER;

    @NotEmpty
    private String url;

    @NotEmpty
    private String queue;
}
