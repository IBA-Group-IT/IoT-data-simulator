package com.iba.iot.datasimulator.definition.dao;

import com.iba.iot.datasimulator.common.factory.minio.MinioClientFactory;
import com.iba.iot.datasimulator.definition.model.Dataset;
import com.iba.iot.datasimulator.definition.util.ObjectStorageUtil;
import io.minio.MinioClient;
import io.minio.errors.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Repository
public class DatasetObjectStorageDaoImpl implements DatasetObjectStorageDao {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(DatasetObjectStorageDaoImpl.class);

    private static final String UPLOADING_STREAM_CONTENT_TYPE = "application/octet-stream";

    @Autowired
    private MinioClientFactory minioClientFactory;

    @Override
    public void upload(Dataset dataset, String originalFileName, InputStream inputStream, long contentSize) throws IOException, InvalidKeyException,
            NoSuchAlgorithmException, InsufficientDataException, InvalidArgumentException, InternalException, NoResponseException,
            InvalidBucketNameException, XmlPullParserException, ErrorResponseException, RegionConflictException, InvalidPortException, InvalidEndpointException, InvalidObjectPrefixException {

        logger.debug(">>> Uploading dataset {} into object storage.", dataset);

        MinioClient minioClient = minioClientFactory.buildMinioClient(dataset);

        String bucketName = dataset.getBucket();
        ObjectStorageUtil.createBucketIfNotExist(minioClient, bucketName);

        String bucketKey = ObjectStorageUtil.generateBucketKey(bucketName, dataset.getName(), minioClient);
        dataset.setObjectKey(bucketKey);

        /**
         *  Overriding initial file name by generated bucket key in order to avoid inconsistency
         *  between initial name & bucket key during uploading
          */
        dataset.setName(bucketKey);

        minioClient.putObject(bucketName, bucketKey, inputStream, contentSize, UPLOADING_STREAM_CONTENT_TYPE);
    }

    @Override
    public void checkExistence(Dataset dataset) throws InvalidPortException, InvalidEndpointException, IOException, InvalidKeyException,
            NoSuchAlgorithmException, InsufficientDataException, InternalException, NoResponseException, InvalidBucketNameException,
            XmlPullParserException, ErrorResponseException {

        logger.debug(">>> Checking dataset {} for existence.", dataset);

        MinioClient minioClient = minioClientFactory.buildMinioClient(dataset);
        minioClient.statObject(dataset.getBucket(), dataset.getObjectKey());
    }
}
