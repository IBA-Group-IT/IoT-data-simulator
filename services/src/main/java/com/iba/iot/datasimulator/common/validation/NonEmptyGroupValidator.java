package com.iba.iot.datasimulator.common.validation;

import com.iba.iot.datasimulator.common.model.function.ThrowingPredicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 */
public class NonEmptyGroupValidator implements ConstraintValidator<NonEmptyGroup, Object> {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(NonEmptyGroupValidator.class);

    @Override
    public void initialize(NonEmptyGroup constraintAnnotation) {}

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {

        Class<?> clazz = value.getClass();
        Field[] declaredFields = clazz.getDeclaredFields();
        List<Field> groupMembers = Stream.of(declaredFields)
                .filter(field -> field.isAnnotationPresent(GroupMember.class))
                .collect(Collectors.toList());

        if (!groupMembers.isEmpty() && !isNonEmptyGroupMemberPresented(value, groupMembers)) {

            String clazzName = clazz.getCanonicalName();
            String fieldNames = groupMembers.stream().map(Field::getName)
                    .collect(Collectors.joining(", "));

            String errorMessage = "Non empty group validation error for object: " + clazzName +
                    ". At least one field from the following fields shouldn't be empty: " + fieldNames;

            logger.error(">>> {}", errorMessage);
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(errorMessage).addConstraintViolation();

            return false;
        }

        return true;
    }

    /**
     *
     * @param value
     * @param groupMembers
     * @return
     */
    private boolean isNonEmptyGroupMemberPresented(Object value, List<Field> groupMembers) {

        return groupMembers.stream()
                           .anyMatch((ThrowingPredicate<Field>) field -> {

                                field.setAccessible(true);
                                Object fieldValue = field.get(value);

                                // In case of string values check for not blank value
                                if (fieldValue instanceof String) {

                                    String stringFieldValue = (String) fieldValue;
                                    return stringFieldValue.trim().length() > 0;
                                }

                                return fieldValue != null;
                           });
    }
}
