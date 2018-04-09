package com.iba.iot.datasimulator.definition.util;

import com.iba.iot.datasimulator.common.util.StringUtil;
import io.minio.MinioClient;
import io.minio.errors.*;
import io.minio.policy.PolicyType;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 *
 */
public class ObjectStorageUtil {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(ObjectStorageUtil.class);

    /**
     *
     * @param minioClient
     * @param bucketName
     */
    public static void createBucketIfNotExist(MinioClient minioClient, String bucketName)  {

        try {

            if (!minioClient.bucketExists(bucketName)) {
                minioClient.makeBucket(bucketName);
                minioClient.setBucketPolicy(bucketName, StringUtil.EMPTY_STRING, PolicyType.READ_WRITE);
            }

        } catch (Exception exception) {

            String message = "An error occurred during bucket existence check";
            logger.error(message, exception);
            throw new RuntimeException(message, exception);
        }
    }

    /**
     *
     * @param bucketName
     * @param initialName
     * @param minioClient
     * @return
     * @throws InsufficientDataException
     * @throws NoSuchAlgorithmException
     * @throws XmlPullParserException
     * @throws NoResponseException
     * @throws InternalException
     * @throws InvalidBucketNameException
     * @throws InvalidKeyException
     * @throws IOException
     */
    public static String generateBucketKey(String bucketName, String initialName, MinioClient minioClient) throws InsufficientDataException, NoSuchAlgorithmException,
            XmlPullParserException, NoResponseException, InternalException, InvalidBucketNameException, InvalidKeyException, IOException {

        StringBuilder generatedBucketKey = new StringBuilder(initialName);
        int postfixIndex = 1;

        while (isObjectExist(minioClient, bucketName, generatedBucketKey.toString())) {

            logger.debug(">>> Bucket {} already has object {}.",bucketName, generatedBucketKey);

            generatedBucketKey.setLength(0);

            String baseName = FilenameUtils.getBaseName(initialName);
            String extension = FilenameUtils.getExtension(initialName);

            generatedBucketKey.append(baseName)
                              .append('_')
                              .append(postfixIndex);

            if (!StringUtils.isEmpty(extension)) {

                generatedBucketKey.append('.')
                                  .append(extension);
            }

            postfixIndex++;
        }

        logger.debug(">>> Bucket key {} generated for initial file name {}", generatedBucketKey, initialName);
        return generatedBucketKey.toString();
    }

    /**
     *
     * @param minioClient
     * @param bucketName
     * @param objectName
     * @return
     */
    public static boolean isObjectExist(MinioClient minioClient, String bucketName, String objectName) throws XmlPullParserException,
            InsufficientDataException, NoSuchAlgorithmException, IOException, NoResponseException, InvalidKeyException, InternalException,
            InvalidBucketNameException {

        try {

            minioClient.statObject(bucketName, objectName);
            return true;

        } catch (ErrorResponseException exception) {
            return false;
        }
    }
}
