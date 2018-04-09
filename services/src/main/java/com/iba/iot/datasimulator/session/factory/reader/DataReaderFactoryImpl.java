package com.iba.iot.datasimulator.session.factory.reader;

import com.iba.iot.datasimulator.common.model.schema.Schema;
import com.iba.iot.datasimulator.definition.model.Dataset;
import com.iba.iot.datasimulator.definition.model.DatasetType;
import com.iba.iot.datasimulator.session.factory.filter.DatasetFilterProcessorFactory;
import com.iba.iot.datasimulator.session.model.active.filter.DatasetFilter;
import com.iba.iot.datasimulator.session.service.active.processing.reader.*;
import com.iba.iot.datasimulator.session.service.active.processing.reader.filter.DatasetFilterProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DataReaderFactoryImpl implements DatasetReaderFactory {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(DataReaderFactoryImpl.class);

    @Autowired
    private BeanFactory beanFactory;

    @Autowired
    private DatasetFilterProcessorFactory datasetFilterProcessorFactory;

    @Override
    public DatasetReader<byte[]> buildByteDatasetReader(Dataset dataset) {
        return beanFactory.getBean(ByteDatasetReader.class, dataset);
    }

    @Override
    public DatasetReader<String> buildStringDatasetReader(Dataset dataset) {
        return buildStringDatasetReader(dataset, null, null);
    }

    @Override
    public DatasetReader<String> buildStringDatasetReader(Dataset dataset, DatasetFilter datasetFilter, Schema schema) {

        if (dataset == null) {

            logger.error(">>> Cannot build dataset reader for empty dataset entity.");
            throw new RuntimeException("Dataset reader building error.");
        }

        DatasetReader<byte[]> byteDatasetReader = buildByteDatasetReader(dataset);
        DatasetReader<String> stringDatasetReader = buildStringDatasetReader(dataset.getType(), byteDatasetReader);

        if (datasetFilter == null) {
            return stringDatasetReader;
        } else {
            return buildFilteringDatasetReader(stringDatasetReader, datasetFilter, schema);
        }
    }

    /**
     *
     * @param datasetType
     * @param byteDatasetReader
     * @return
     */
    private DatasetReader<String> buildStringDatasetReader(DatasetType datasetType, DatasetReader<byte[]> byteDatasetReader) {

        if (datasetType == DatasetType.JSON) {
            return beanFactory.getBean(MultilineJsonDatasetReader.class, byteDatasetReader);
        } else {
            return beanFactory.getBean(LineDatasetReader.class, byteDatasetReader);
        }
    }

    /**
     *
     * @param stringDatasetReader
     * @param datasetFilter
     * @param schema
     * @return
     */
    private DatasetReader<String> buildFilteringDatasetReader(DatasetReader<String> stringDatasetReader, DatasetFilter datasetFilter, Schema schema) {

        DatasetFilterProcessor<String> filterProcessor = datasetFilterProcessorFactory.build(datasetFilter, schema);
        return beanFactory.getBean(FilteringDatasetReader.class, stringDatasetReader, filterProcessor);
    }
}
