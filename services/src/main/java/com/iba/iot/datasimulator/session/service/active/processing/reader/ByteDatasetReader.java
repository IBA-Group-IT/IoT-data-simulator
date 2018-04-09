package com.iba.iot.datasimulator.session.service.active.processing.reader;

import com.iba.iot.datasimulator.common.factory.minio.MinioClientFactory;
import com.iba.iot.datasimulator.definition.model.Dataset;
import io.minio.MinioClient;
import io.minio.errors.*;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.xmlpull.v1.XmlPullParserException;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ByteDatasetReader implements DatasetReader<byte[]> {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(ByteDatasetReader.class);

    /** **/
    private MinioClient minioClient;

    /** **/
    private String datasetName;

    /** **/
    private long datasetBinaryLength;

    /** **/
    private String bucketName;

    /** **/
    private Dataset dataset;

    /** **/
    private long offset = 0;

    /** **/
    @Value("${dataset.reading.buffer}")
    private long chunkSize;

    @Autowired
    private MinioClientFactory minioClientFactory;

    /**
     *
     * @param dataset
     */
    public ByteDatasetReader(Dataset dataset) {

        logger.debug(">>> Creating instance of ByteDatasetReader");

        this.dataset = dataset;
        datasetName = dataset.getObjectKey();
        bucketName = dataset.getBucket();
    }

    @PostConstruct
    private void init() throws InvalidPortException, InvalidEndpointException, IOException, InvalidKeyException, NoSuchAlgorithmException,
            InsufficientDataException, InternalException, NoResponseException, InvalidBucketNameException, XmlPullParserException,
            ErrorResponseException {

        minioClient = minioClientFactory.buildMinioClient(dataset);
        datasetBinaryLength = minioClient.statObject(bucketName, datasetName).length();
    }

    @Override
    public boolean hasNext() {
        return offset < datasetBinaryLength;
    }

    @Override
    public byte[] next() {

        try {

            InputStream inputStream;
            byte[] bytes = new byte[(int)chunkSize];

            if (offset > 0) {

                inputStream = minioClient.getObject(bucketName, datasetName, offset, chunkSize);
                bytes = IOUtils.toByteArray(inputStream);

            } else {

                /**
                 * There is an issue in current minio client version:
                 * in case of offset = 0 it returns input stream to full dataset content.
                 * So, as workaround we are reading first chunk manually
                 */
                inputStream = minioClient.getObject(bucketName, datasetName);
                IOUtils.read(inputStream, bytes, 0, (int) chunkSize);
            }

            byte[] trimmedBytes = new String(bytes).trim().getBytes();

            offset += chunkSize;

            // Close the input stream.
            inputStream.close();

            return trimmedBytes;

        } catch (Exception exception) {

            logger.error(">>> An error occurred during dataset value reading.", exception);
            throw new RuntimeException("Dataset reading error.");
        }
    }
}
