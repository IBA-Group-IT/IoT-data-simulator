package com.iba.iot.datasimulator.common.service.schema.parser;

import com.iba.iot.datasimulator.common.model.schema.property.ArraySchemaProperty;
import com.iba.iot.datasimulator.common.model.schema.property.ObjectSchemaProperty;
import com.iba.iot.datasimulator.common.model.schema.property.SchemaProperty;
import com.iba.iot.datasimulator.common.model.schema.property.SessionSchemaProperty;
import com.iba.iot.datasimulator.session.model.unfold.UnfoldedSessionSchemaProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class SessionSchemaParser extends CommonSchemaParser<UnfoldedSessionSchemaProperty> {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(SessionSchemaParser.class);

    @Override
    protected void processFlatPropertyValue(SchemaProperty schemaProperty, Collection<UnfoldedSessionSchemaProperty> result, String position) {
        processProperty(schemaProperty, result, position);
    }

    @Override
    protected void processEmptyObjectProperty(ObjectSchemaProperty objectSchemaProperty, Collection<UnfoldedSessionSchemaProperty> result, String position) {
        processProperty(objectSchemaProperty, result, position);
    }

    @Override
    protected void processEmptyArrayProperty(ArraySchemaProperty arraySchemaProperty, Collection<UnfoldedSessionSchemaProperty> result, String position) {
        processProperty(arraySchemaProperty, result, position);
    }

    /**
     *
     * @param schemaProperty
     * @param result
     * @param position
     */
    private void processProperty(SchemaProperty schemaProperty, Collection<UnfoldedSessionSchemaProperty> result, String position) {

        logger.debug(">>> Processing session schema property {} for position {}", schemaProperty, position);
        result.add(new UnfoldedSessionSchemaProperty(position, (SessionSchemaProperty) schemaProperty));
    }
}