package com.iba.iot.datasimulator.session.model.active.generator;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.iba.iot.datasimulator.common.model.schema.Schema;
import com.iba.iot.datasimulator.session.model.SessionViews;
import com.iba.iot.datasimulator.session.validator.SessionSchemaValid;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
@ToString
@JsonDeserialize
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SchemaBasedGenerator implements Generator {

    @JsonView(SessionViews.Short.class)
    @Getter
    private GeneratorType type = GeneratorType.SCHEMA_BASED;

    @NotNull
    @SessionSchemaValid
    @Valid
    private Schema schema;
}
