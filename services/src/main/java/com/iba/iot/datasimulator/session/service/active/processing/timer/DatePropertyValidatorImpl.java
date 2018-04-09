package com.iba.iot.datasimulator.session.service.active.processing.timer;

import com.iba.iot.datasimulator.common.model.schema.property.SchemaProperty;
import com.iba.iot.datasimulator.common.model.schema.property.metadata.SchemaPropertyMetadata;
import com.iba.iot.datasimulator.common.model.schema.property.metadata.SchemaPropertyMetadataType;
import com.iba.iot.datasimulator.common.model.schema.property.metadata.TypedSchemaPropertyMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class DatePropertyValidatorImpl implements DatePropertyValidator {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(DatasetTimerProcessor.class);

    @Override
    public void validate(String datePosition, SchemaProperty dateSchemaProperty) {

        if (dateSchemaProperty == null) {

            logger.error(">>> Cannot find definition for property {} in the data definition schema", datePosition);
            throw new RuntimeException("Cannot build dataset timer processor due to the missing schema date property definition.");
        }

        SchemaPropertyMetadata metadata = dateSchemaProperty.getMetadata();
        if (!(metadata instanceof TypedSchemaPropertyMetadata)) {

            logger.error(">>> Wrong date property {} type in the data definition schema", datePosition);
            throw new RuntimeException("Date property should be instance of timestamp or date type.");
        }

        TypedSchemaPropertyMetadata typedMetadata = (TypedSchemaPropertyMetadata) dateSchemaProperty.getMetadata();
        SchemaPropertyMetadataType datePropertyType = typedMetadata.getType();

        if (datePropertyType != SchemaPropertyMetadataType.TIMESTAMP &&
            datePropertyType != SchemaPropertyMetadataType.DATE) {

            logger.error(">>> Property {} isn't date time property.", datePosition);
            throw new RuntimeException("Provided dataset property isn't date time property.");
        }
    }
}
