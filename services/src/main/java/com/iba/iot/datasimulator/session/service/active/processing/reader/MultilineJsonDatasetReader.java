package com.iba.iot.datasimulator.session.service.active.processing.reader;

import com.iba.iot.datasimulator.common.util.StringUtil;
import com.iba.iot.datasimulator.session.service.active.processing.reader.parser.JsonTokenParser;
import com.iba.iot.datasimulator.session.service.active.processing.reader.parser.JsonTokenParserImpl;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Iterator;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class MultilineJsonDatasetReader implements DatasetReader<String> {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(MultilineJsonDatasetReader.class);

    /** **/
    private StringBuilder datasetChunk = new StringBuilder();

    /** **/
    private Iterator<byte[]> byteDatasetReader;

    /** **/
    public MultilineJsonDatasetReader(Iterator<byte[]> byteDatasetReader) {
        this.byteDatasetReader = byteDatasetReader;
    }

    @Override
    public boolean hasNext() {
       return datasetChunk.length() > 0 || checkNextChunkAvailability();
    }

    @Override
    public String next() {

        logger.debug(">>> Getting next json entity.");

        String jsonEntity = readJsonEntity();
        while (jsonEntity == null && byteDatasetReader.hasNext()) {

            readNextDatasetChunk();
            jsonEntity = readJsonEntity();
        }


        if (StringUtils.isNotBlank(jsonEntity)) {
            return jsonEntity;
        }

        logger.error(">>> Cannot parse json entry from the following dataset chunk: {}", datasetChunk.toString());
        throw new RuntimeException("Dataset entry json parsing error.");
    }

    /**
     *
     * @return
     * @throws IOException
     */
    private String readJsonEntity() {

        JsonTokenParser jsonTokenParser = new JsonTokenParserImpl(datasetChunk);
        int jsonDepthLevel = 0;

        do {

            String token = jsonTokenParser.nextToken();

            if (token == null) {
                return null;
            }

            if (token.equalsIgnoreCase(StringUtil.OPENING_CURLY_BRACKET) ||
                token.equalsIgnoreCase(StringUtil.OPENING_SQUARE_BRACKET)) {
                jsonDepthLevel++;
            }

            if (token.equalsIgnoreCase(StringUtil.CLOSING_CURLY_BRACKET) ||
                token.equalsIgnoreCase(StringUtil.CLOSING_SQUARE_BRACKET)) {
                jsonDepthLevel--;
            }

        } while (jsonDepthLevel != 0);

        int jsonEntityEndPosition = jsonTokenParser.getCurrentOffset();
        String jsonEntity = datasetChunk.substring(0, jsonEntityEndPosition + 1);
        datasetChunk.delete(0, jsonEntityEndPosition + 1);

        return jsonEntity;
    }

    /**
     *
     * @return
     */
    private boolean checkNextChunkAvailability() {

        while (datasetChunk.length() == 0 && byteDatasetReader.hasNext()) {
            readNextDatasetChunk();
        }

        return datasetChunk.length() > 0;
    }

    /**
     *
     */
    private void readNextDatasetChunk() {

        logger.debug(">>> Reading next dataset chunk.");
        String nextDatasetChunk = StringUtil.buildString(byteDatasetReader.next()).trim();
        datasetChunk.append(nextDatasetChunk);
    }
}
