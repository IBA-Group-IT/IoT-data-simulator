package com.iba.iot.datasimulator.common.provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;

@Service
public class MinioConnectionPropertiesProviderImpl implements MinioConnectionPropertiesProvider {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(MinioConnectionPropertiesProviderImpl.class);

    /** **/
    public static final String MINIO_URL = "MINIO_URL";

    /** **/
    public static final String MINIO_ACCESS_KEY = "MINIO_ACCESS_KEY";

    /** **/
    public static final String MINIO_SECRET_KEY = "MINIO_SECRET_KEY";

    /** **/
    @Value("${minio.local.bucket.name}")
    public String defaultBucketName;

    /** **/
    private String minioUrl;

    /** **/
    private String minioAccessKey;

    /** **/
    private String minioSecretKey;

    @PostConstruct
    private void init() {

        minioUrl = System.getenv(MINIO_URL);
        minioAccessKey = System.getenv(MINIO_ACCESS_KEY);
        minioSecretKey = System.getenv(MINIO_SECRET_KEY);

        if (StringUtils.isEmpty(minioUrl) || StringUtils.isEmpty(minioAccessKey) || StringUtils.isEmpty(minioSecretKey)) {

            logger.error(">>> Minio object storage connection properties missing in environment variables.");
            throw new RuntimeException("Minio object storage connection properties are missing in environment variables.");
        }
    }

    @Override
    public String getUrl() {
        return minioUrl;
    }

    @Override
    public String getAccessKey() {
        return minioAccessKey;
    }

    @Override
    public String getSecretKey() {
        return minioSecretKey;
    }

    @Override
    public String getBucket() {
        return defaultBucketName;
    }
}
