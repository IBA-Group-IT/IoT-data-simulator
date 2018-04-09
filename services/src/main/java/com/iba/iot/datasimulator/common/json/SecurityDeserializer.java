package com.iba.iot.datasimulator.common.json;

import com.iba.iot.datasimulator.common.model.security.*;

/**
 *
 */
public class SecurityDeserializer extends TypedPolymorphicDeserializer<Security, SecurityType> {

    @Override
    protected SecurityType parseType(String rawType) {
        return SecurityType.fromString(rawType);
    }

    @Override
    protected Class<? extends Security> determineConcreteType(SecurityType deviceSecurityType) {

        switch (deviceSecurityType) {

            case CREDENTIALS:
                return CredentialsSecurity.class;

            case CERTIFICATES:
                return CertificateSecurity.class;

            case ACCESS_TOKEN:
                return AccessTokenSecurity.class;

            case ACCESS_KEYS:
                return AccessKeysSecurity.class;

            default:
                return null;
        }
    }
}
