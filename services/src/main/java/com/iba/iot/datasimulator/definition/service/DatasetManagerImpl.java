package com.iba.iot.datasimulator.definition.service;

import com.iba.iot.datasimulator.common.model.schema.Schema;
import com.iba.iot.datasimulator.common.service.schema.manager.SchemaManager;
import com.iba.iot.datasimulator.common.util.ModelEntityUtil;
import com.iba.iot.datasimulator.common.util.RemovalValidationUtil;
import com.iba.iot.datasimulator.definition.dao.DatasetDatabaseDao;
import com.iba.iot.datasimulator.definition.dao.DatasetObjectStorageDao;
import com.iba.iot.datasimulator.definition.factory.DatasetFactory;
import com.iba.iot.datasimulator.definition.model.DataDefinition;
import com.iba.iot.datasimulator.definition.model.Dataset;
import com.iba.iot.datasimulator.definition.model.DatasetType;
import com.iba.iot.datasimulator.definition.util.DatasetUtil;
import io.minio.errors.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;

@Service
public class DatasetManagerImpl implements DatasetManager {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(DatasetManagerImpl.class);

    @Autowired
    private DatasetFactory datasetFactory;

    @Autowired
    private DatasetObjectStorageDao datasetObjectStorageDao;

    @Autowired
    private DatasetDatabaseDao datasetDatabaseDao;

    @Autowired
    private SchemaManager schemaManager;

    @Override
    public Dataset upload(String originalFileName, InputStream inputStream, long contentSize) throws IOException, XmlPullParserException,
            NoSuchAlgorithmException, InvalidKeyException, InvalidArgumentException, ErrorResponseException, NoResponseException,
            InvalidBucketNameException, InsufficientDataException, InternalException, RegionConflictException, InvalidPortException,
            InvalidEndpointException, InvalidObjectPrefixException {

        logger.debug(">>> Uploading new dataset {}", originalFileName);

        DatasetType datasetType = DatasetUtil.getDatasetType(originalFileName);
        Dataset localDataset = datasetFactory.buildFromUploadRequest(originalFileName, datasetType);

        datasetObjectStorageDao.upload(localDataset, originalFileName, inputStream, contentSize);
        datasetDatabaseDao.save(localDataset);

        return localDataset;
    }

    @Override
    public Dataset create(Dataset dataset) throws IOException, XmlPullParserException, NoSuchAlgorithmException, InvalidKeyException,
            InvalidPortException, ErrorResponseException, NoResponseException, InvalidBucketNameException, InsufficientDataException,
            InvalidEndpointException, InternalException {

        logger.debug(">>> Creating new existing dataset {}", dataset);
        datasetObjectStorageDao.checkExistence(dataset);
        return datasetDatabaseDao.save(dataset);
    }

    @Override
    public Collection<Dataset> get(String name, String type) {

        logger.debug(">>> Getting all datasets.");
        return ModelEntityUtil.sortByModified(datasetDatabaseDao.get(name, type));
    }

    @Override
    public Dataset get(String datasetId) {

        logger.debug(">>> Getting dataset by id {}.", datasetId);
        return datasetDatabaseDao.get(datasetId);
    }

    @Override
    public Schema deriveSchema(String datasetId) throws IOException {

        logger.debug(">>> Deriving schema for dataset by id {}.", datasetId);

        Dataset dataset = datasetDatabaseDao.get(datasetId);
        if (dataset != null) {
            return schemaManager.deriveSchema(dataset);
        }

        logger.error(">>> Cannot fetch dataset by provided id {}", datasetId);
        throw new IllegalArgumentException("Wrong dataset id provided");
    }

    @Override
    public Dataset update(String datasetId, Dataset dataset) throws IOException, XmlPullParserException, NoSuchAlgorithmException,
            InvalidKeyException, InvalidPortException, ErrorResponseException, NoResponseException, InvalidBucketNameException, InsufficientDataException,
            InvalidEndpointException, InternalException {

        logger.debug(">>> Updating dataset {} by value: {}.", datasetId, dataset);
        ModelEntityUtil.setId(dataset, datasetId);
        datasetObjectStorageDao.checkExistence(dataset);
        return datasetDatabaseDao.update(dataset);
    }

    @Override
    public void remove(String datasetId) {

        logger.debug(">>> Removing dataset {}.", datasetId);
        RemovalValidationUtil.checkReferences(datasetId, datasetDatabaseDao::getLinkedDataDefinitions, DataDefinition::getName);

        datasetDatabaseDao.remove(datasetId);
    }
}