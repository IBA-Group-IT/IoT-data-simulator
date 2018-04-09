package com.iba.iot.datasimulator.session.service.active.processing.reader;

import com.iba.iot.datasimulator.common.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class LineDatasetReader implements DatasetReader<String> {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(LineDatasetReader.class);

    /** **/
    private static final String EOL_REGEX = "\\r?\\n";

    /** **/
    private LinkedList<String> linesBuffer = new LinkedList<>();

    /** **/
    private String previousLastLine;

    /** **/
    private Iterator<byte[]> byteDatasetReader;

    /** **/
    public LineDatasetReader(Iterator<byte[]> byteDatasetReader) {

        logger.debug(">>> Creating instance of LineDatasetReader");

        this.byteDatasetReader = byteDatasetReader;
    }

    @Override
    public boolean hasNext() {
        return !linesBuffer.isEmpty() || byteDatasetReader.hasNext();
    }

    @Override
    public String next() {

        if (linesBuffer.isEmpty()) {
            populateBuffer();
        }

        return linesBuffer.pollFirst();
    }

    /**
     *
     */
    private void populateBuffer() {

        logger.debug(">>> Line buffer populating");

        byte[] bytes = byteDatasetReader.next();
        LinkedList<String> datasetChunkLines = readNextChunkLines(bytes);

        /**
         * In case of presented value in previousLastLine
         * we need to concatenate it with first line from new chunk
         */
        if (!StringUtils.isEmpty(previousLastLine)) {

            String firstLine = datasetChunkLines.pollFirst();
            String completeFirstLine = previousLastLine + firstLine;
            datasetChunkLines.addFirst(completeFirstLine);
        }

        /**
         * Save last chunk line for further processing
         * in case of more data presented in dataset
         */
        if (byteDatasetReader.hasNext()) {
            previousLastLine = datasetChunkLines.pollLast();
        }

        linesBuffer = datasetChunkLines;
        if (linesBuffer.isEmpty()) {

            logger.debug(">>> Line buffer still empty, trying to read new dataset chunk.");
            if (byteDatasetReader.hasNext()) {

                populateBuffer();

            } else {

                logger.debug(">>> No available byte arrays, adding previous line ending to line buffer");
                linesBuffer.add(previousLastLine);
            }
        }
    }

    /**
     *
     * @param bytes
     * @return
     */
    private LinkedList<String> readNextChunkLines(byte[] bytes) {

        String datasetChunk = StringUtil.buildString(bytes);
        String[] dataLines = datasetChunk.split(EOL_REGEX);
        return new LinkedList<>(Arrays.asList(dataLines));
    }
}
