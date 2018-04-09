package com.iba.iot.datasimulator.common.factory.minio;

import com.iba.iot.datasimulator.definition.model.Dataset;
import com.iba.iot.datasimulator.target.model.S3TargetSystem;
import io.minio.MinioClient;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidPortException;

/**
 *
 */
public interface MinioClientFactory {

    /**
     *
     * @param dataset
     * @return
     */
    MinioClient buildMinioClient(Dataset dataset) throws InvalidPortException, InvalidEndpointException;

    /**
     *
     * @param targetSystem
     * @return
     * @throws InvalidPortException
     * @throws InvalidEndpointException
     */
    MinioClient buildMinioClient(S3TargetSystem targetSystem);

}
