package com.iba.iot.datasimulator.definition.util;

import com.iba.iot.datasimulator.definition.model.DatasetType;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

/**
 *
 */
public class DatasetUtil {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(DatasetUtil.class);

    /**
     *
     * @param datasetFileName
     * @return
     */
    public static DatasetType getDatasetType(String datasetFileName) {

        String extension = FilenameUtils.getExtension(datasetFileName);
        DatasetType datasetType = DatasetType.fromString(extension);
        if (datasetType != null) {
            return datasetType;
        }

        logger.error(">>> Unsupported dataset type detected for file {}", datasetFileName);
        throw new RuntimeException("Unsupported dataset type");
    }

}
