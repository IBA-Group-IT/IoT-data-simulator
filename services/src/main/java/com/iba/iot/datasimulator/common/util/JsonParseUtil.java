package com.iba.iot.datasimulator.common.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 *
 */
public class JsonParseUtil {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(JsonParseUtil.class);

    /** **/
    private static ObjectMapper mapper = new ObjectMapper();

    /**
     *
     * @param datasetEntry
     * @return
     * @throws IOException
     */
    public static JsonNode parseDatasetEntryJson(String datasetEntry) throws IOException {

        try {

            return mapper.readTree(datasetEntry);

        } catch (Exception exception) {

            logger.error(">>> Dataset entry {} parsing as json object failed with error: {}", datasetEntry, ExceptionUtil.getErrorMessage(exception));
            throw new RuntimeException("Dataset entry json parsing error.");
        }
    }
}
