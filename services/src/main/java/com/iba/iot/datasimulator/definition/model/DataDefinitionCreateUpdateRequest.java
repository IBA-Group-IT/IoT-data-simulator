package com.iba.iot.datasimulator.definition.model;

import com.iba.iot.datasimulator.common.model.schema.Schema;
import com.iba.iot.datasimulator.common.validation.GroupMember;
import com.iba.iot.datasimulator.common.validation.NonEmptyGroup;
import com.iba.iot.datasimulator.definition.validator.DataDefinitionSchemaValid;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;

@Data
@ToString
@NonEmptyGroup
public class DataDefinitionCreateUpdateRequest {

    @NotEmpty
    private String name;

    @GroupMember
    private String datasetId;

    @Valid
    @GroupMember
    @DataDefinitionSchemaValid
    private Schema schema;
}
