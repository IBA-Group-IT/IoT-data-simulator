package com.iba.iot.datasimulator.common.model.security;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.NotEmpty;

@Data
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonDeserialize
public class CertificateSecurity implements Security {

    private String ca;

    @NotEmpty
    private String deviceCertificate;

    @NotEmpty
    private String privateKey;

    @Override
    public SecurityType getType() {
        return SecurityType.CERTIFICATES;
    }
}
