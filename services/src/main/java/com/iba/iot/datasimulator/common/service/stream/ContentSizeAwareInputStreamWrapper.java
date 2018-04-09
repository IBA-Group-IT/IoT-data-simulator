package com.iba.iot.datasimulator.common.service.stream;

import com.iba.iot.datasimulator.definition.dao.DatasetObjectStorageDaoImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 *
 */
public class ContentSizeAwareInputStreamWrapper extends InputStream {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(ContentSizeAwareInputStreamWrapper.class);

    /** **/
    private InputStream inputStream;

    /** **/
    private long available;

    /**
     *
     * @param inputStream
     */
    public ContentSizeAwareInputStreamWrapper(InputStream inputStream, long size) {
        this.inputStream = inputStream;
        this.available = size;
    }

    @Override
    public int read() throws IOException {
        return inputStream.read();
    }

    @Override
    public int read(byte[] b) throws IOException {
        return inputStream.read(b);
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        return inputStream.read(b, off, len);
    }

    @Override
    public int available() throws IOException {
        return (int) available;
    }

    @Override
    public void close() throws IOException {
        inputStream.close();
    }
}
