package com.iba.iot.datasimulator.common.model.security;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.NotEmpty;

@Data
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonDeserialize
@AllArgsConstructor
@NoArgsConstructor
public class AccessKeysSecurity implements Security {

    /** **/
    @NotEmpty
    private String accessKey;

    /** **/
    @NotEmpty
    private String secretKey;

    @Override
    public SecurityType getType() {
        return SecurityType.ACCESS_KEYS;
    }

}
