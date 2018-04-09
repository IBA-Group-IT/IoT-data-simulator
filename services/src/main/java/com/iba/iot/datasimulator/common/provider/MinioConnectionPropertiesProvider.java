package com.iba.iot.datasimulator.common.provider;

/**
 *
 */
public interface MinioConnectionPropertiesProvider {

    /** **/
    String getUrl();

    /** **/
    String getAccessKey();

    /** **/
    String getSecretKey();

    /** **/
    String getBucket();

}
