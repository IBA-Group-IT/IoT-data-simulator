package com.iba.iot.datasimulator.session.service.active.processing.reader;

import com.iba.iot.datasimulator.session.service.active.processing.reader.filter.DatasetFilterProcessor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class FilteringDatasetReader implements DatasetReader<String> {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(FilteringDatasetReader.class);

    /** **/
    private DatasetReader<String> datasetReader;

    /** **/
    private DatasetFilterProcessor<String> filterProcessor;

    /** **/
    private String nextDatasetEntry;

    /** **/
    private boolean isNextDatasetEntryRead = false;

    /**
     *
     * @param datasetReader
     */
    public FilteringDatasetReader(DatasetReader<String> datasetReader, DatasetFilterProcessor<String> filterProcessor) {
        this.datasetReader = datasetReader;
        this.filterProcessor = filterProcessor;
    }

    @Override
    public boolean hasNext() {

        logger.debug(">>> Checking next dataset entry availability");
        isNextDatasetEntryRead = true;
        readNextDatasetEntry();

        return StringUtils.isNotBlank(nextDatasetEntry);
    }

    @Override
    public String next() {

        logger.debug(">>> Providing next dataset entry: {}", nextDatasetEntry);
        if (isNextDatasetEntryRead) {
            isNextDatasetEntryRead = false;
            return nextDatasetEntry;
        }

        readNextDatasetEntry();
        return nextDatasetEntry;
    }

    /**
     *
     * @return
     */
    private void readNextDatasetEntry() {

        boolean isNextDatasetEntryFound = false;
        while(datasetReader.hasNext() && !isNextDatasetEntryFound) {

            nextDatasetEntry = datasetReader.next();
            isNextDatasetEntryFound = filterProcessor.filter(nextDatasetEntry);
        }

        if (!isNextDatasetEntryFound) {
            nextDatasetEntry = null;
        }
    }
}
