package com.iba.iot.datasimulator.session.factory.filter;

import com.iba.iot.datasimulator.common.model.schema.Schema;
import com.iba.iot.datasimulator.session.model.active.filter.DatasetFilter;
import com.iba.iot.datasimulator.session.model.active.filter.DatasetFilterType;
import com.iba.iot.datasimulator.session.service.active.processing.reader.filter.CustomFunctionDatasetFilterProcessor;
import com.iba.iot.datasimulator.session.service.active.processing.reader.filter.DatasetEntryPositionFilterProcessor;
import com.iba.iot.datasimulator.session.service.active.processing.reader.filter.DatasetFilterProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DatasetFilterProcessorFactoryImpl implements DatasetFilterProcessorFactory {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(DatasetFilterProcessorFactoryImpl.class);

    @Autowired
    private BeanFactory beanFactory;

    @Override
    public DatasetFilterProcessor<String> build(DatasetFilter datasetFilter, Schema schema) {

        if (datasetFilter != null) {

            DatasetFilterType datasetFilterType = datasetFilter.getType();
            if (datasetFilterType == DatasetFilterType.DATASET_ENTRY_POSITION && schema != null) {
                return beanFactory.getBean(DatasetEntryPositionFilterProcessor.class, datasetFilter, schema);
            } else if (datasetFilterType == DatasetFilterType.CUSTOM_FUNCTION) {
                return beanFactory.getBean(CustomFunctionDatasetFilterProcessor.class, datasetFilter);
            }
        }

        logger.error(">>> Cannot build dataset filter processor.");
        throw new RuntimeException("Dataset filter processor building error.");
    }
}

