package com.iba.iot.datasimulator.common.factory.schema.property;

import com.iba.iot.datasimulator.common.model.schema.property.ArraySchemaPropertyModel;
import com.iba.iot.datasimulator.common.model.schema.property.ObjectSchemaPropertyModel;
import com.iba.iot.datasimulator.common.model.schema.property.SchemaPropertyModel;

/**
 *
 */
public interface SchemaPropertyBuilder {

    /**
     *
     * @param position
     * @return
     */
    ArraySchemaPropertyModel buildArrayProperty(String position);

    /**
     *
     * @param position
     * @return
     */
    ObjectSchemaPropertyModel buildObjectProperty(String position);

    /**
     *
     * @param position
     * @return
     */
    SchemaPropertyModel buildStringProperty(String position);

    /**
     *
     * @param position
     * @return
     */
    SchemaPropertyModel buildBooleanProperty(String position);

    /**
     *
     * @param position
     * @return
     */
    SchemaPropertyModel buildIntegerProperty(String position);

    /**
     *
     * @param position
     * @return
     */
    SchemaPropertyModel buildLongProperty(String position);

    /**
     *
     * @param position
     * @return
     */
    SchemaPropertyModel buildDoubleProperty(String position);

    /**
     *
     * @param position
     * @return
     */
    SchemaPropertyModel buildDateProperty(String position, String format);

    /**
     *
     * @param position
     * @return
     */
    SchemaPropertyModel buildTimestampProperty(String position, String format);
}
