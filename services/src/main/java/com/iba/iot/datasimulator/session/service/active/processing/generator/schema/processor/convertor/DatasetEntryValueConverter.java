package com.iba.iot.datasimulator.session.service.active.processing.generator.schema.processor.convertor;

import com.iba.iot.datasimulator.session.model.unfold.UnfoldedSessionSchemaProperty;

import java.io.IOException;

/**
 *
 */
public interface DatasetEntryValueConverter {

    /**
     *
     * @param unfoldedSchemaProperty
     * @param datasetEntryValue
     * @return
     * @throws IOException
     */
    Object convertToOriginalType(UnfoldedSessionSchemaProperty unfoldedSchemaProperty, String datasetEntryValue) throws IOException;

}
