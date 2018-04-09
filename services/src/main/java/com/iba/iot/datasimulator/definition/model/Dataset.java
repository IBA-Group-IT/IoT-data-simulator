package com.iba.iot.datasimulator.definition.model;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.iba.iot.datasimulator.common.json.ObjectIdDeserializer;
import com.iba.iot.datasimulator.common.json.ObjectIdSerializer;
import com.iba.iot.datasimulator.common.model.ModelEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import org.bson.types.ObjectId;
import org.hibernate.validator.constraints.NotEmpty;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import javax.validation.constraints.NotNull;

@Data
@ToString
@Entity("dataset")
public class Dataset implements ModelEntity {

    @ApiModelProperty(dataType = "java.lang.String")
    @JsonSerialize(using = ObjectIdSerializer.class)
    @JsonDeserialize(using = ObjectIdDeserializer.class)
    @JsonView(DatasetViews.Short.class)
    @Id
    private ObjectId id;

    @JsonView(DatasetViews.Short.class)
    private long modified;

    @JsonView(DatasetViews.Short.class)
    @NotEmpty
    private String name;

    @JsonView(DatasetViews.Short.class)
    @NotNull
    private DatasetType type;

    @NotEmpty
    private String url;

    @NotEmpty
    private String bucket;

    @NotEmpty
    private String objectKey;

    @NotEmpty
    private String accessKey;

    @NotEmpty
    private String secretKey;
}
