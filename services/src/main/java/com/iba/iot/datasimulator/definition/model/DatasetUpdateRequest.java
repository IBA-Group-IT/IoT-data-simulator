package com.iba.iot.datasimulator.definition.model;

import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

@Data
@ToString
public class DatasetUpdateRequest {

    @NotEmpty
    private String name;

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
