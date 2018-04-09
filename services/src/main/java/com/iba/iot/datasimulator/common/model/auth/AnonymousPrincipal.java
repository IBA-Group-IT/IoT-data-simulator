package com.iba.iot.datasimulator.common.model.auth;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.security.Principal;

@Data
@EqualsAndHashCode
public class AnonymousPrincipal implements Principal {

    private String name;

}
