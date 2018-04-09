package com.iba.iot.datasimulator.device.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.iba.iot.datasimulator.common.json.ObjectIdDeserializer;
import com.iba.iot.datasimulator.common.json.ObjectIdSerializer;
import com.iba.iot.datasimulator.common.model.ModelEntity;
import com.iba.iot.datasimulator.device.model.property.DeviceProperty;
import com.iba.iot.datasimulator.target.model.TargetSystem;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import org.bson.types.ObjectId;
import org.hibernate.validator.constraints.NotEmpty;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.util.Collection;

@Data
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity("device")
public class Device implements ModelEntity {

    @ApiModelProperty(dataType = "java.lang.String")
    @JsonSerialize(using = ObjectIdSerializer.class)
    @JsonDeserialize(using = ObjectIdDeserializer.class)
    @JsonView(DeviceViews.Short.class)
    @Id
    private ObjectId id;

    @JsonView(DeviceViews.Short.class)
    private long modified;

    @JsonView(DeviceViews.Short.class)
    @NotEmpty
    private String name;

    @Embedded
    private Collection<TargetSystem> targetSystems;

    @Embedded
    private Collection<DeviceProperty> properties;
}
