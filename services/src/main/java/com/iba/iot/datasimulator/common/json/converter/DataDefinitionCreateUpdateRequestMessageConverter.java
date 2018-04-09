package com.iba.iot.datasimulator.common.json.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.lang.reflect.Type;

/**
 *
 */
public class DataDefinitionCreateUpdateRequestMessageConverter extends MappingJackson2HttpMessageConverter {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(DataDefinitionCreateUpdateRequestMessageConverter.class);

    /** **/
    public static final String TYPE_NAME = "com.iba.iot.datasimulator.definition.model.DataDefinitionCreateUpdateRequest";

    /**
     *
     * @param objectMapper
     */
    public DataDefinitionCreateUpdateRequestMessageConverter(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    public boolean canRead(Type type, Class<?> contextClass, MediaType mediaType) {

        if (type.getTypeName().equalsIgnoreCase(TYPE_NAME)) {

            logger.debug(">>> Processing data definition create update request conversion.");
            return true;
        }

        return false;
    }

    @Override
    public boolean canWrite(Type type, Class<?> clazz, MediaType mediaType) {
        return false;
    }
}
