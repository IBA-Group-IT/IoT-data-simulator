package com.iba.iot.datasimulator.common.factory.minio;

import com.iba.iot.datasimulator.common.model.security.AccessKeysSecurity;
import com.iba.iot.datasimulator.definition.model.Dataset;
import com.iba.iot.datasimulator.target.model.S3TargetSystem;
import io.minio.MinioClient;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidPortException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MinioClientFactoryImpl implements MinioClientFactory {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(MinioClientFactoryImpl.class);

    /** **/
    private static final String ERROR_MESSAGE = "An error occurred during minio client creation";

    @Value("${minio.client.connect.timeout}")
    private long connectionTimeoutSeconds;

    /** **/
    private long connectionTimeout = connectionTimeoutSeconds * 1000;

    @Value("${minio.client.write.timeout}")
    private long writeTimeoutSeconds;

    /** **/
    private long writeTimeout = writeTimeoutSeconds * 1000;

    @Value("${minio.client.read.timeout}")
    private long readTimeoutSeconds;

    /** **/
    private long readTimeout = readTimeoutSeconds * 1000;

    @Override
    public MinioClient buildMinioClient(Dataset dataset) throws InvalidPortException, InvalidEndpointException {

        logger.debug(">>> Building minio client for dataset: {}", dataset);
        return buildMinioClient(dataset.getUrl(), dataset.getAccessKey(), dataset.getSecretKey());
    }

    @Override
    public MinioClient buildMinioClient(S3TargetSystem targetSystem) {

        logger.debug(">>> Building minio client for S3 target system: {}", targetSystem);
        AccessKeysSecurity security = (AccessKeysSecurity) targetSystem.getSecurity();

        return buildMinioClient(targetSystem.getUrl(), security.getAccessKey(), security.getSecretKey());
    }

    /**
     *
     * @param url
     * @param accessKey
     * @param secretKey
     * @return
     */
    private MinioClient buildMinioClient(String url, String accessKey, String secretKey) {

        try {

            MinioClient minioClient = new MinioClient(url, accessKey, secretKey);
            minioClient.setTimeout(connectionTimeout, writeTimeout, readTimeout);
            return minioClient;

        } catch (Exception exception) {

            logger.error(ERROR_MESSAGE, exception);
            throw new RuntimeException(ERROR_MESSAGE, exception);
        }
    }
}
