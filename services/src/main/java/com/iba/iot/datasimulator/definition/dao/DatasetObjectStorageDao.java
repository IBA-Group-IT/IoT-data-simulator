package com.iba.iot.datasimulator.definition.dao;

import com.iba.iot.datasimulator.definition.model.Dataset;
import io.minio.errors.*;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface DatasetObjectStorageDao {

    /**
     *  @param dataset
     * @param originalFileName
     * @param inputStream @throws IOException
     * @param contentSize
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     * @throws InsufficientDataException
     * @throws InvalidArgumentException
     * @throws InternalException
     * @throws NoResponseException
     * @throws InvalidBucketNameException
     * @throws XmlPullParserException
     * @throws ErrorResponseException
     * @throws RegionConflictException
     * @throws InvalidPortException
     * @throws InvalidEndpointException
     */
    void upload(Dataset dataset, String originalFileName, InputStream inputStream, long contentSize) throws IOException, InvalidKeyException, NoSuchAlgorithmException,
            InsufficientDataException, InvalidArgumentException, InternalException, NoResponseException, InvalidBucketNameException,
            XmlPullParserException, ErrorResponseException, RegionConflictException, InvalidPortException, InvalidEndpointException, InvalidObjectPrefixException;

    /**
     *
     * @param dataset
     */
    void checkExistence(Dataset dataset) throws InvalidPortException, InvalidEndpointException, IOException, InvalidKeyException,
            NoSuchAlgorithmException, InsufficientDataException, InternalException, NoResponseException, InvalidBucketNameException,
            XmlPullParserException, ErrorResponseException;

}
