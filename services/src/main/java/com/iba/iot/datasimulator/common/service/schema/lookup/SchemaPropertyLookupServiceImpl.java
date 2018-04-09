package com.iba.iot.datasimulator.common.service.schema.lookup;

import com.iba.iot.datasimulator.common.model.schema.Schema;
import com.iba.iot.datasimulator.common.model.schema.property.SchemaProperty;
import com.iba.iot.datasimulator.common.service.schema.parser.SchemaParser;
import com.iba.iot.datasimulator.session.model.unfold.UnfoldedSchemaProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SchemaPropertyLookupServiceImpl implements SchemaPropertyLookupService {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(SchemaPropertyLookupServiceImpl.class);

    @Autowired
    private SchemaParser<UnfoldedSchemaProperty> schemaParser;

    @Override
    public SchemaProperty findProperty(String position, Schema schema) {

        logger.debug(">>> Looking for property {} in schema {}", position, schema);
        return schemaParser.parse(schema)
                           .stream()
                           .filter(unfoldedProperty -> unfoldedProperty.getPosition().equalsIgnoreCase(position))
                           .findFirst()
                           .flatMap(unfoldedProperty -> Optional.of(unfoldedProperty.getProperty()))
                           .orElse(null);
    }
}
