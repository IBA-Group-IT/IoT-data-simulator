package com.iba.iot.datasimulator.definition.service;

import com.iba.iot.datasimulator.common.model.schema.Schema;
import com.iba.iot.datasimulator.definition.model.Dataset;
import io.minio.errors.*;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;

public interface DatasetManager {

    /**
     *
     *
     * @param originalFileName
     * @param inputStream
     * @param contentSize
     * @return
     * @throws IOException
     * @throws XmlPullParserException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws InvalidArgumentException
     * @throws ErrorResponseException
     * @throws NoResponseException
     * @throws InvalidBucketNameException
     * @throws InsufficientDataException
     * @throws InternalException
     * @throws RegionConflictException
     * @throws InvalidPortException
     * @throws InvalidEndpointException
     */
    Dataset upload(String originalFileName, InputStream inputStream, long contentSize) throws IOException, XmlPullParserException, NoSuchAlgorithmException,
            InvalidKeyException, InvalidArgumentException, ErrorResponseException, NoResponseException,
            InvalidBucketNameException, InsufficientDataException, InternalException, RegionConflictException,
            InvalidPortException, InvalidEndpointException, InvalidObjectPrefixException;

    /**
     *
     * @param dataset
     * @return
     * @throws IOException
     * @throws XmlPullParserException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws InvalidPortException
     * @throws ErrorResponseException
     * @throws NoResponseException
     * @throws InvalidBucketNameException
     * @throws InsufficientDataException
     * @throws InvalidEndpointException
     * @throws InternalException
     */
    Dataset create(Dataset dataset) throws IOException, XmlPullParserException, NoSuchAlgorithmException,
            InvalidKeyException, InvalidPortException, ErrorResponseException, NoResponseException, InvalidBucketNameException,
            InsufficientDataException, InvalidEndpointException, InternalException;

    /**
     *
     * @return
     * @param name
     * @param type
     */
    Collection<Dataset> get(String name, String type);

    /**
     *
     * @param datasetId
     * @return
     */
    Dataset get(String datasetId);

    /**
     *
     * @param datasetId
     * @return
     */
    Schema deriveSchema(String datasetId) throws IOException;

    /**
     *
     *
     */
    Dataset update(String datasetId, Dataset dataset) throws IOException, XmlPullParserException,
            NoSuchAlgorithmException, InvalidKeyException, InvalidPortException, ErrorResponseException, NoResponseException,
            InvalidBucketNameException, InsufficientDataException, InvalidEndpointException, InternalException;

    /**
     *
     * @param datasetId
     */
    void remove(String datasetId);

}
