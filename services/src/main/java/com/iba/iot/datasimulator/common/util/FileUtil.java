package com.iba.iot.datasimulator.common.util;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 *
 */
public class FileUtil {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);

    /**
     *
     * @param fileName
     * @return
     * @throws IOException
     */
    public static String readFile(String fileName) throws IOException {

        logger.debug(">>> Reading file {} from classpath...", fileName);

        InputStream stream = FileUtil.class.getClassLoader().getResourceAsStream(fileName);
        return IOUtils.toString(stream, StandardCharsets.UTF_8.toString());
    }
}
