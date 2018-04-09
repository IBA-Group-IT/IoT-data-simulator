package com.iba.iot.datasimulator.target.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.iba.iot.datasimulator.common.model.TargetSystemType;
import com.iba.iot.datasimulator.target.model.serialization.Serializer;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.NotEmpty;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;

import javax.validation.Valid;

@Data
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity(value = "targetSystem")
public class KafkaTargetSystem extends TargetSystemEntity {

    @JsonView(TargetSystemViews.Short.class)
    private final TargetSystemType type = TargetSystemType.KAFKA_BROKER;

    @NotEmpty
    private String url;

    @NotEmpty
    private String topic;

    /** **/
    private String keyFunction;

    @Embedded
    @Valid
    private Serializer keySerializer;
}
