package com.iba.iot.datasimulator.common.model.schema;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SchemaRootMetadata {

    @NotNull
    @JsonView(SchemaViews.Short.class)
    private SchemaRootMetadataType type;
}
