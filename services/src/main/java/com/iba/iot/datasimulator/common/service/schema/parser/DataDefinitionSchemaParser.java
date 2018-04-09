package com.iba.iot.datasimulator.common.service.schema.parser;

import com.iba.iot.datasimulator.common.model.schema.property.SchemaProperty;
import com.iba.iot.datasimulator.session.model.unfold.UnfoldedSchemaProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class DataDefinitionSchemaParser extends CommonSchemaParser<UnfoldedSchemaProperty> {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(DataDefinitionSchemaParser.class);

    @Override
    protected void processFlatPropertyValue(SchemaProperty schemaProperty, Collection<UnfoldedSchemaProperty> result, String position) {

        logger.debug(">>> Processing data definition schema property {} for position {}", schemaProperty, position);
        result.add(new UnfoldedSchemaProperty(position, schemaProperty));
    }
}
