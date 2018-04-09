package com.iba.iot.datasimulator.common.service.schema.traverser;

import com.iba.iot.datasimulator.common.model.schema.ArraySchema;
import com.iba.iot.datasimulator.common.model.schema.ObjectSchema;
import com.iba.iot.datasimulator.common.model.schema.Schema;
import com.iba.iot.datasimulator.common.model.schema.property.ArraySchemaProperty;
import com.iba.iot.datasimulator.common.model.schema.property.ObjectSchemaProperty;
import com.iba.iot.datasimulator.common.model.schema.property.SchemaProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.function.Consumer;

@Service
public class SchemaPropertiesTraverserImpl implements SchemaPropertiesTraverser {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(SchemaPropertiesTraverserImpl.class);

    @Override
    public void traverseWithPlainPropertyConsumer(Schema schema, Consumer<SchemaProperty> plainPropertyConsumer) {
        traverse(schema, plainPropertyConsumer, null, null);
    }

    @Override
    public void traverseWithObjectPropertyConsumer(Schema schema, Consumer<ObjectSchemaProperty> objectPropertyConsumer) {
        traverse(schema, null, objectPropertyConsumer, null);
    }

    @Override
    public void traverseWithArrayPropertyConsumer(Schema schema, Consumer<ArraySchemaProperty> arrayPropertyConsumer) {
        traverse(schema, null, null, arrayPropertyConsumer);
    }

    @Override
    public void traverse(Schema schema, Consumer<SchemaProperty> plainPropertyConsumer,
                         Consumer<ObjectSchemaProperty> objectPropertyConsumer,
                         Consumer<ArraySchemaProperty> arrayPropertyConsumer) {

        Collection<? extends SchemaProperty> schemaProperties = getProperties(schema);
        logger.debug(">>> Traversing schema properties: {}", schemaProperties);
        traverseProperties(schemaProperties, plainPropertyConsumer, objectPropertyConsumer, arrayPropertyConsumer);
    }

    /**
     *
     * @param schema
     * @return
     */
    private Collection<? extends SchemaProperty> getProperties(Schema schema) {

        if (schema instanceof ObjectSchema) {
            return ((ObjectSchema) schema).getProperties().values();
        }

        // If schema isn't object schema it can be only array schema
        return ((ArraySchema) schema).getItems();
    }

    /**
     *
     * @param properties
     * @param plainPropertyConsumer
     * @param objectPropertyConsumer
     * @param arrayPropertyConsumer
     */
    private void traverseProperties(Collection<? extends SchemaProperty> properties,
                                    Consumer<SchemaProperty> plainPropertyConsumer,
                                    Consumer<ObjectSchemaProperty> objectPropertyConsumer,
                                    Consumer<ArraySchemaProperty> arrayPropertyConsumer) {
        if (properties != null) {
            properties.forEach(property -> traverseProperty(property, plainPropertyConsumer, objectPropertyConsumer, arrayPropertyConsumer));
        }
    }

    /**
     *
     * @param property
     * @param plainPropertyConsumer
     * @param objectPropertyConsumer
     * @param arrayPropertyConsumer
     */
    private void traverseProperty(Object property, Consumer<SchemaProperty> plainPropertyConsumer,
                                  Consumer<ObjectSchemaProperty> objectPropertyConsumer,
                                  Consumer<ArraySchemaProperty> arrayPropertyConsumer) {

        if (property instanceof ObjectSchemaProperty) {
            traverseObjectProperty((ObjectSchemaProperty) property, plainPropertyConsumer, objectPropertyConsumer, arrayPropertyConsumer);
        } else if(property instanceof ArraySchemaProperty) {
            traverseArrayProperty((ArraySchemaProperty) property, plainPropertyConsumer, objectPropertyConsumer, arrayPropertyConsumer);
        } else {
            traversePlainProperty((SchemaProperty) property, plainPropertyConsumer);
        }
    }

    /**
     *
     * @param objectProperty
     * @param plainPropertyConsumer
     * @param objectPropertyConsumer
     * @param arrayPropertyConsumer
     */
    private void traverseObjectProperty(ObjectSchemaProperty objectProperty,
                                        Consumer<SchemaProperty> plainPropertyConsumer,
                                        Consumer<ObjectSchemaProperty> objectPropertyConsumer,
                                        Consumer<ArraySchemaProperty> arrayPropertyConsumer) {

        if (objectPropertyConsumer != null) {
            objectPropertyConsumer.accept(objectProperty);
        }

        Map<String, ? extends SchemaProperty> properties = objectProperty.getProperties();
        if (properties != null) {
            traverseProperties(properties.values(), plainPropertyConsumer, objectPropertyConsumer, arrayPropertyConsumer);
        }
    }

    /**
     *
     * @param arrayProperty
     * @param plainPropertyConsumer
     * @param objectPropertyConsumer
     * @param arrayPropertyConsumer
     */
    private void traverseArrayProperty(ArraySchemaProperty arrayProperty,
                                       Consumer<SchemaProperty> plainPropertyConsumer,
                                       Consumer<ObjectSchemaProperty> objectPropertyConsumer,
                                       Consumer<ArraySchemaProperty> arrayPropertyConsumer) {

        if (arrayPropertyConsumer != null) {
            arrayPropertyConsumer.accept(arrayProperty);
        }

        traverseProperties(arrayProperty.getItems(), plainPropertyConsumer, objectPropertyConsumer, arrayPropertyConsumer);
    }

    /**
     *
     * @param property
     */
    private void traversePlainProperty(SchemaProperty property, Consumer<SchemaProperty> plainPropertyConsumer) {

        if (plainPropertyConsumer != null) {
            plainPropertyConsumer.accept(property);
        }
    }
}
