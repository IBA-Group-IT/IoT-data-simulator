package com.iba.iot.datasimulator.target.model;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.iba.iot.datasimulator.common.json.ObjectIdDeserializer;
import com.iba.iot.datasimulator.common.json.ObjectIdSerializer;
import com.iba.iot.datasimulator.common.model.security.Security;
import com.iba.iot.datasimulator.target.model.serialization.Serializer;
import com.iba.iot.datasimulator.target.validator.TargetSystemValid;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import org.bson.types.ObjectId;
import org.hibernate.validator.constraints.NotEmpty;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Id;

import javax.validation.Valid;
import java.util.Collection;

@Data
@ToString
@JsonDeserialize
@TargetSystemValid
public abstract class TargetSystemEntity implements TargetSystem {

    @ApiModelProperty(dataType = "java.lang.String")
    @JsonSerialize(using = ObjectIdSerializer.class)
    @JsonDeserialize(using = ObjectIdDeserializer.class)
    @JsonView(TargetSystemViews.Short.class)
    @Id
    private ObjectId id;

    @JsonView(TargetSystemViews.Short.class)
    private long modified;

    @NotEmpty
    @JsonView(TargetSystemViews.Short.class)
    private String name;

    @Embedded
    private Security security;

    @Embedded
    private Collection<Header> headers;

    @Embedded
    @Valid
    private Serializer messageSerializer;
}
