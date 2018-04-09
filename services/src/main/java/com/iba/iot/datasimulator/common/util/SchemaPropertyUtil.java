package com.iba.iot.datasimulator.common.util;

import com.iba.iot.datasimulator.common.model.schema.property.SchemaProperty;
import com.iba.iot.datasimulator.common.model.schema.property.SchemaPropertyType;
import com.iba.iot.datasimulator.common.model.schema.property.metadata.FormattedTypedSchemaPropertyMetadata;
import com.iba.iot.datasimulator.common.model.schema.property.metadata.SchemaPropertyMetadata;
import com.iba.iot.datasimulator.common.model.schema.property.metadata.SchemaPropertyMetadataType;
import com.iba.iot.datasimulator.common.model.schema.property.metadata.TypedSchemaPropertyMetadata;

/**
 *
 */
public class SchemaPropertyUtil {

    /**
     *
     * @param property
     * @return
     */
    public static boolean isDateProperty(SchemaProperty property) {

       SchemaPropertyType type = property.getType();
       if (type == SchemaPropertyType.STRING) {
           SchemaPropertyMetadata metadata = property.getMetadata();
           if (metadata instanceof FormattedTypedSchemaPropertyMetadata) {
               return ((FormattedTypedSchemaPropertyMetadata) metadata).getType() == SchemaPropertyMetadataType.DATE;
           }
       }

       return false;
   }

    /**
     *
     * @param property
     * @return
     */
    public static boolean isObjectProperty(SchemaProperty property) {

        return property.getType() == SchemaPropertyType.OBJECT;
    }

    /**
     *
     * @param property
     * @return
     */
    public static boolean isArrayProperty(SchemaProperty property) {

        return property.getType() == SchemaPropertyType.ARRAY;
    }

    /**
     *
     * @param property
     * @return
     */
    public static boolean isTimestampProperty(SchemaProperty property) {

        SchemaPropertyType type = property.getType();
        if (type == SchemaPropertyType.NUMBER) {
            SchemaPropertyMetadata metadata = property.getMetadata();
            if (metadata instanceof FormattedTypedSchemaPropertyMetadata) {
                return ((FormattedTypedSchemaPropertyMetadata) metadata).getType() == SchemaPropertyMetadataType.TIMESTAMP;
            }
        }

        return false;
    }

    /**
     *
     * @param property
     * @return
     */
    public static boolean isDateTimeProperty(SchemaProperty property) {
        return isDateProperty(property) || isTimestampProperty(property);
    }

    /**
     *
     * @param property
     * @return
     */
    public static boolean isBooleanProperty(SchemaProperty property) {
        return property.getType() == SchemaPropertyType.BOOLEAN;
    }

    /**
     *
     * @param property
     * @return
     */
    public static boolean isNumberProperty(SchemaProperty property) {
        return property.getType() == SchemaPropertyType.NUMBER;
    }

    /**
     *
     * @param property
     * @return
     */
    public static boolean isIntegerProperty(SchemaProperty property) {

        if (isNumberProperty(property)) {

            TypedSchemaPropertyMetadata metadata = (TypedSchemaPropertyMetadata) property.getMetadata();
            return metadata.getType() == SchemaPropertyMetadataType.INTEGER;
        }

        return false;
    }

    /**
     *
     * @param property
     * @return
     */
    public static boolean isLongProperty(SchemaProperty property) {

        if (isNumberProperty(property)) {

            TypedSchemaPropertyMetadata metadata = (TypedSchemaPropertyMetadata) property.getMetadata();
            return metadata.getType() == SchemaPropertyMetadataType.LONG;
        }

        return false;
    }

    /**
     *
     * @param property
     * @return
     */
    public static boolean isDoubleProperty(SchemaProperty property) {

        if (isNumberProperty(property)) {

            TypedSchemaPropertyMetadata metadata = (TypedSchemaPropertyMetadata) property.getMetadata();
            return metadata.getType() == SchemaPropertyMetadataType.DOUBLE;
        }

        return false;
    }

    /**
     *
     * @param propertyMetadata
     * @return
     */
    public static SchemaPropertyMetadataType getPropertyMetadataType(SchemaPropertyMetadata propertyMetadata) {

        if (propertyMetadata instanceof TypedSchemaPropertyMetadata) {
            return ((TypedSchemaPropertyMetadata) propertyMetadata).getType();
        }

        return null;
    }

    /**
     *
     * @param first
     * @param second
     * @return
     */
    public static boolean isSamePropertyType(SchemaProperty first, SchemaProperty second) {

        SchemaPropertyType firstPropertyType = first.getType();
        SchemaPropertyType secondPropertyType = second.getType();

        if (firstPropertyType == secondPropertyType) {

            SchemaPropertyMetadata firstPropertyMetadata = first.getMetadata();
            SchemaPropertyMetadata secondPropertyMetadata = second.getMetadata();

            return getPropertyMetadataType(firstPropertyMetadata) == getPropertyMetadataType(secondPropertyMetadata);
        }

        return false;
    }
}
