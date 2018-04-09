package com.iba.iot.datasimulator.definition.validator;

import com.iba.iot.datasimulator.common.model.TimestampType;
import com.iba.iot.datasimulator.common.model.schema.Schema;
import com.iba.iot.datasimulator.common.model.schema.property.metadata.FormattedTypedSchemaPropertyMetadata;
import com.iba.iot.datasimulator.common.model.schema.property.metadata.SchemaPropertyMetadata;
import com.iba.iot.datasimulator.common.model.schema.property.metadata.SchemaPropertyMetadataType;
import com.iba.iot.datasimulator.common.service.schema.traverser.SchemaPropertiesTraverser;
import com.iba.iot.datasimulator.common.util.ValidationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 */
public class DataDefinitionSchemaValidator implements ConstraintValidator<DataDefinitionSchemaValid, Schema> {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(DataDefinitionSchemaValidator.class);

    @Autowired
    private SchemaPropertiesTraverser schemaPropertiesTraverser;

    @Override
    public void initialize(DataDefinitionSchemaValid constraintAnnotation) {}

    @Override
    public boolean isValid(Schema schema, ConstraintValidatorContext context) {

        if (schema != null) {

            return ValidationUtil.validatePropertiesContainer(schema, context) &&
                    validateTimestampProperties(context, schema);
        }

        return true;
    }

    /**
     *
     * @param context
     * @param schema
     * @return
     */
    private boolean validateTimestampProperties(ConstraintValidatorContext context, Schema schema) {

        final AtomicBoolean isValid = new AtomicBoolean(true);

        schemaPropertiesTraverser.traverseWithPlainPropertyConsumer(schema,

            schemaProperty -> {

            SchemaPropertyMetadata metadata = schemaProperty.getMetadata();
            if (metadata instanceof FormattedTypedSchemaPropertyMetadata) {

                FormattedTypedSchemaPropertyMetadata typedFormattedMetadata = (FormattedTypedSchemaPropertyMetadata) metadata;
                SchemaPropertyMetadataType type = typedFormattedMetadata.getType();
                if (type == SchemaPropertyMetadataType.TIMESTAMP) {

                    String rawType = typedFormattedMetadata.getFormat();
                    TimestampType timestampType = TimestampType.fromString(rawType);
                    if (timestampType == null) {

                        isValid.set(false);

                        logger.error(">>> Wrong timestamp {} type provided.", rawType);
                        context.disableDefaultConstraintViolation();
                        context.buildConstraintViolationWithTemplate("{data.definition.schema.wrong.timestamp.type}")
                                .addConstraintViolation();
                    }
                }
            }

            ValidationUtil.validatePlainPropertyMetadata(schemaProperty, isValid, context);
        });

        return isValid.get();
    }
}
