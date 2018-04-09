package com.iba.iot.datasimulator.common.util;

import com.iba.iot.datasimulator.common.model.schema.Schema;
import com.iba.iot.datasimulator.common.model.schema.property.SchemaProperty;
import com.iba.iot.datasimulator.common.model.schema.property.metadata.SchemaPropertyMetadata;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintValidatorContext;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 */
public class ValidationUtil {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(ValidationUtil.class);

    /**
     *
     * @param schema
     * @param context
     * @return
     */
    public static boolean validatePropertiesContainer(Schema schema,
                                                      ConstraintValidatorContext context) {


        AtomicBoolean result = new AtomicBoolean(true);
        SchemaUtil.processSchema(schema,

                objectSchema -> {

                    Map<String, SchemaProperty> properties = objectSchema.getProperties();
                    if (properties == null || properties.values().isEmpty()) {
                        processEmptyPropertiesError(context, result);
                    }

                },

                arraySchema -> {

                    Collection<SchemaProperty> items = arraySchema.getItems();
                    if (items == null || items.isEmpty()) {
                        processEmptyPropertiesError(context, result);
                    }
                });

        return result.get();
    }

    /**
     *
     * @param context
     * @param result
     */
    private static void processEmptyPropertiesError(ConstraintValidatorContext context, AtomicBoolean result) {

        logger.error(">>> Schema failed validation due to empty properties.");

        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate("{schema.properties.empty}")
                .addConstraintViolation();

        result.set(false);
    }

    /**
     *
     * @param schemaProperty
     * @param isValid
     * @param context
     */
    public static void validatePlainPropertyMetadata(SchemaProperty schemaProperty,
                                                     AtomicBoolean isValid,
                                                     ConstraintValidatorContext context) {

        SchemaPropertyMetadata metadata = schemaProperty.getMetadata();
        if (StringUtils.isEmpty(metadata.getName())) {

            logger.error(">>> Schema property {} failed validation due to empty name property in metadata.", schemaProperty);
            isValid.set(false);
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("{session.schema.property.metadata.name.empty}")
                    .addConstraintViolation();
        }

    }


}
