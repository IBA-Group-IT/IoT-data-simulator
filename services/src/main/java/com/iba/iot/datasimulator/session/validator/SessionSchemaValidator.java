package com.iba.iot.datasimulator.session.validator;

import com.iba.iot.datasimulator.common.model.schema.Schema;
import com.iba.iot.datasimulator.common.model.schema.property.SessionArraySchemaProperty;
import com.iba.iot.datasimulator.common.model.schema.property.SessionObjectSchemaProperty;
import com.iba.iot.datasimulator.common.model.schema.property.SessionSchemaProperty;
import com.iba.iot.datasimulator.common.model.schema.property.rule.SchemaPropertyRuleType;
import com.iba.iot.datasimulator.common.service.schema.parser.SchemaParser;
import com.iba.iot.datasimulator.common.service.schema.rule.SchemaPropertyRuleDependencyManager;
import com.iba.iot.datasimulator.common.service.schema.traverser.SchemaPropertiesTraverser;
import com.iba.iot.datasimulator.common.util.StringUtil;
import com.iba.iot.datasimulator.common.util.ValidationUtil;
import com.iba.iot.datasimulator.session.model.unfold.UnfoldedSessionSchemaProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.util.stream.Collectors.toList;

/**
 *
 */
public class SessionSchemaValidator implements ConstraintValidator<SessionSchemaValid, Schema> {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(SessionSchemaValidator.class);

    @Autowired
    private SchemaPropertiesTraverser schemaPropertiesTraverser;

    @Autowired
    private SchemaParser<UnfoldedSessionSchemaProperty> sessionSchemaParser;

    @Autowired
    private SchemaPropertyRuleDependencyManager ruleDependencyManager;

    @Override
    public void initialize(SessionSchemaValid constraintAnnotation) {
    }

    @Override
    public boolean isValid(Schema schema, ConstraintValidatorContext context) {

        if (schema != null) {

            return ValidationUtil.validatePropertiesContainer(schema, context) &&
                   validateSchemaValueProperties(context, schema) &&
                   validateSchemaPropertyRuleDependencies(context, schema);
        }

        return true;
    }

    /**
     * @param context
     * @param schema
     * @return
     */
    private boolean validateSchemaValueProperties(ConstraintValidatorContext context, Schema schema) {

        final AtomicBoolean isValid = new AtomicBoolean(true);

        schemaPropertiesTraverser.traverse(schema,

            schemaProperty -> {

                if (!(schemaProperty instanceof SessionSchemaProperty)) {

                    logger.error(">>> Schema property {} failed validation due to empty rule child.", schemaProperty);

                    isValid.set(false);
                    context.disableDefaultConstraintViolation();
                    context.buildConstraintViolationWithTemplate("{session.schema.property.rule.empty}")
                            .addConstraintViolation();
                }

                ValidationUtil.validatePlainPropertyMetadata(schemaProperty, isValid, context);
            },

            schemaProperty -> {

                SessionObjectSchemaProperty objectSchemaProperty = (SessionObjectSchemaProperty) schemaProperty;
                if ((objectSchemaProperty.getProperties() == null || objectSchemaProperty.getProperties().isEmpty()) &&
                    (objectSchemaProperty.getRule() == null ||
                    (objectSchemaProperty.getRule().getType() != SchemaPropertyRuleType.CUSTOM_FUNCTION &&
                     objectSchemaProperty.getRule().getType() != SchemaPropertyRuleType.AS_IS))) {


                    logger.error(">>> Object schema property {} failed validation due to empty properties and rule or wrong rule type.", objectSchemaProperty);

                    isValid.set(false);
                    context.disableDefaultConstraintViolation();
                    context.buildConstraintViolationWithTemplate("{session.object.schema.property.invalid}")
                            .addConstraintViolation();
                }
            },


            schemaProperty -> {

                SessionArraySchemaProperty arraySchemaProperty = (SessionArraySchemaProperty) schemaProperty;
                if ((arraySchemaProperty.getItems() == null || arraySchemaProperty.getItems().isEmpty()) &&
                    (arraySchemaProperty.getRule() == null ||
                    (arraySchemaProperty.getRule().getType() != SchemaPropertyRuleType.CUSTOM_FUNCTION &&
                     arraySchemaProperty.getRule().getType() != SchemaPropertyRuleType.AS_IS))) {

                    logger.error(">>> Array schema property {} failed validation due to empty items and rule or wrong rule type.", arraySchemaProperty);

                    isValid.set(false);
                    context.disableDefaultConstraintViolation();
                    context.buildConstraintViolationWithTemplate("{session.array.schema.property.invalid}")
                            .addConstraintViolation();
                }
            }
        );

        return isValid.get();
    }

    /**
     *
     * @param context
     * @param schema
     * @return
     */
    private boolean validateSchemaPropertyRuleDependencies(ConstraintValidatorContext context, Schema schema) {

        Collection<UnfoldedSessionSchemaProperty> unfoldedProperties = sessionSchemaParser.parse(schema);
        Collection<Collection<UnfoldedSessionSchemaProperty>> circularRuleDependencies =
                ruleDependencyManager.findCircularRuleDependencies(unfoldedProperties);

        if (!circularRuleDependencies.isEmpty()) {

            String circles = String.join(StringUtil.COMMA,
                    circularRuleDependencies.stream()
                                            .map(ruleCircularDependencies ->
                                                    String.join(" -> ",
                                                            ruleCircularDependencies.stream()
                                                                                    .map(UnfoldedSessionSchemaProperty::getPosition)
                                                                                    .collect(toList())))
                                            .collect(toList()));

            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Cannot create session due to the following property " +
                            "rule circular dependencies: " + circles)
                   .addConstraintViolation();

            return false;

        }

        return true;
    }
}
