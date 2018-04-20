package com.iba.iot.datasimulator.session.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.iba.iot.datasimulator.common.json.ObjectIdDeserializer;
import com.iba.iot.datasimulator.common.json.ObjectIdSerializer;
import com.iba.iot.datasimulator.common.model.ModelEntity;
import com.iba.iot.datasimulator.common.validation.NonEmptyGroup;
import com.iba.iot.datasimulator.definition.model.DataDefinition;
import com.iba.iot.datasimulator.device.model.Device;
import com.iba.iot.datasimulator.session.model.active.filter.DatasetFilter;
import com.iba.iot.datasimulator.session.model.active.generator.Generator;
import com.iba.iot.datasimulator.session.model.active.injector.DeviceInjector;
import com.iba.iot.datasimulator.session.model.active.timer.Timer;
import com.iba.iot.datasimulator.target.model.TargetSystem;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import org.bson.types.ObjectId;
import org.hibernate.validator.constraints.NotEmpty;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Reference;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Collection;

@NonEmptyGroup
@Data
@ToString
@Entity("session")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Session implements ModelEntity {

    @ApiModelProperty(dataType = "java.lang.String")
    @JsonView(SessionViews.Short.class)
    @JsonSerialize(using = ObjectIdSerializer.class)
    @JsonDeserialize(using = ObjectIdDeserializer.class)
    @Id
    private ObjectId id;

    @JsonView(SessionViews.Short.class)
    private long modified;

    @JsonView(SessionViews.Short.class)
    @NotEmpty
    private String name;

    @Reference(idOnly = true)
    @JsonView(SessionViews.Short.class)
    @Valid
    private DataDefinition dataDefinition;

    @JsonView(SessionViews.Short.class)
    @Embedded
    @NotNull
    @Valid
    private Timer timer;

    @JsonView(SessionViews.Short.class)
    private int ticksNumber;

    @JsonView(SessionViews.Short.class)
    private boolean isReplayLooped;

    @JsonView(SessionViews.Short.class)
    @Embedded
    @Valid
    private DatasetFilter datasetFilter;

    @JsonView(SessionViews.Short.class)
    @Reference(idOnly = true)
    @Valid
    private Collection<Device> devices;

    @JsonView(SessionViews.Short.class)
    @Embedded
    private DeviceInjector deviceInjector;

    @JsonView(SessionViews.Short.class)
    @NotNull
    @Valid
    @Embedded
    private Generator generator;

    @JsonView(SessionViews.Short.class)
    @Reference(idOnly = true)
    @Valid
    private TargetSystem targetSystem;
}

