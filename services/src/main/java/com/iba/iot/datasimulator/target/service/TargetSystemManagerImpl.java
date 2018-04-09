package com.iba.iot.datasimulator.target.service;

import com.iba.iot.datasimulator.common.factory.minio.MinioClientFactory;
import com.iba.iot.datasimulator.common.model.TargetSystemType;
import com.iba.iot.datasimulator.common.model.security.AccessKeysSecurity;
import com.iba.iot.datasimulator.common.provider.MinioConnectionPropertiesProvider;
import com.iba.iot.datasimulator.common.util.ModelEntityUtil;
import com.iba.iot.datasimulator.common.util.RemovalValidationUtil;
import com.iba.iot.datasimulator.definition.util.ObjectStorageUtil;
import com.iba.iot.datasimulator.session.model.Session;
import com.iba.iot.datasimulator.target.dao.TargetSystemDao;
import com.iba.iot.datasimulator.target.model.S3TargetSystem;
import com.iba.iot.datasimulator.target.model.TargetSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class TargetSystemManagerImpl implements TargetSystemManager {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(TargetSystemManagerImpl.class);

    @Autowired
    private MinioConnectionPropertiesProvider minioConnectionPropsProvider;

    @Autowired
    private MinioClientFactory minioClientFactory;

    @Autowired
    private TargetSystemDao targetSystemDao;

    @Override
    public TargetSystem create(TargetSystem targetSystem) {

        logger.debug(">>> Creating new target system {}", targetSystem);
        if (targetSystem.getType() == TargetSystemType.S3) {
            populateDefaultLocalS3Properties((S3TargetSystem) targetSystem);
        }

        return targetSystemDao.save(targetSystem);
    }

    @Override
    public TargetSystem update(String targetSystemId, TargetSystem targetSystem) {

        logger.debug(">>> Updating existed target system {}", targetSystem);
        ModelEntityUtil.setId(targetSystem, targetSystemId);
        if (targetSystem.getType() == TargetSystemType.S3) {
            populateDefaultLocalS3Properties((S3TargetSystem) targetSystem);
        }

        return targetSystemDao.update(targetSystem);
    }

    @Override
    public Collection<TargetSystem> get() {

        logger.debug(">>> Getting all target systems");
        return ModelEntityUtil.sortByModified(targetSystemDao.get());
    }

    @Override
    public TargetSystem get(String targetSystemId) {

        logger.debug(">>> Getting target system by id: {}", targetSystemId);
        return targetSystemDao.get(targetSystemId);
    }

    @Override
    public void remove(String targetSystemId) {

        RemovalValidationUtil.checkReferences(targetSystemId, targetSystemDao::getLinkedSessions, Session::getName);

        logger.debug(">>> Removing target system by id: {}", targetSystemId);
        targetSystemDao.remove(targetSystemId);
    }

    /**
     *
     * @param targetSystem
     */
    private void populateDefaultLocalS3Properties(S3TargetSystem targetSystem) {

        targetSystem.setUrl(minioConnectionPropsProvider.getUrl());
        targetSystem.setBucket(minioConnectionPropsProvider.getBucket());
        targetSystem.setRemote(false);

        AccessKeysSecurity accessKeysSecurity = new AccessKeysSecurity(minioConnectionPropsProvider.getAccessKey(),
                minioConnectionPropsProvider.getSecretKey());

        targetSystem.setSecurity(accessKeysSecurity);

        ObjectStorageUtil.createBucketIfNotExist(minioClientFactory.buildMinioClient(targetSystem),
                minioConnectionPropsProvider.getBucket());
    }
}
