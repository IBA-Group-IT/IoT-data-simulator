package com.iba.iot.datasimulator.definition.factory;

import com.iba.iot.datasimulator.common.provider.MinioConnectionPropertiesProvider;
import com.iba.iot.datasimulator.definition.model.Dataset;
import com.iba.iot.datasimulator.definition.model.DatasetType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatasetFactoryImpl implements DatasetFactory {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(DatasetFactoryImpl.class);

    @Autowired
    private MinioConnectionPropertiesProvider minioConnectionPropsProvider;

    @Override
    public Dataset buildFromUploadRequest(String fileName, DatasetType datasetType) {

        Dataset dataset = new Dataset();

        dataset.setName(fileName);
        dataset.setType(datasetType);

        dataset.setUrl(minioConnectionPropsProvider.getUrl());
        dataset.setAccessKey(minioConnectionPropsProvider.getAccessKey());
        dataset.setSecretKey(minioConnectionPropsProvider.getSecretKey());
        dataset.setBucket(minioConnectionPropsProvider.getBucket());

        return dataset;
    }
}
