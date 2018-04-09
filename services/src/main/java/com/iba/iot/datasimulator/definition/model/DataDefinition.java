package com.iba.iot.datasimulator.definition.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.iba.iot.datasimulator.common.json.ObjectIdDeserializer;
import com.iba.iot.datasimulator.common.json.ObjectIdSerializer;
import com.iba.iot.datasimulator.common.model.ModelEntity;
import com.iba.iot.datasimulator.common.model.schema.Schema;
import com.iba.iot.datasimulator.common.validation.GroupMember;
import com.iba.iot.datasimulator.common.validation.NonEmptyGroup;
import com.iba.iot.datasimulator.definition.validator.DataDefinitionSchemaValid;
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

@Data
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@NonEmptyGroup
@Entity("datadefinition")
public class DataDefinition implements ModelEntity {

    @ApiModelProperty(dataType = "java.lang.String")
    @JsonSerialize(using = ObjectIdSerializer.class)
    @JsonDeserialize(using = ObjectIdDeserializer.class)
    @JsonView(DataDefinitionViews.Short.class)
    @Id
    private ObjectId id;

    @JsonView(DataDefinitionViews.Short.class)
    private long modified;

    @NotEmpty
    @JsonView(DataDefinitionViews.Short.class)
    private String name;

    @JsonView(DataDefinitionViews.Short.class)
    @GroupMember
    @Reference(idOnly = true)
    private Dataset dataset;

    @JsonView(DataDefinitionViews.Short.class)
    @GroupMember
    @Embedded
    @Valid
    @DataDefinitionSchemaValid
    private Schema schema;
}
