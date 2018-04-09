package com.iba.iot.datasimulator.common.json;

import com.iba.iot.datasimulator.session.model.active.generator.Generator;
import com.iba.iot.datasimulator.session.model.active.generator.GeneratorType;
import com.iba.iot.datasimulator.session.model.active.generator.JsFunctionGenerator;
import com.iba.iot.datasimulator.session.model.active.generator.SchemaBasedGenerator;

/**
 *
 */
public class GeneratorDeserializer extends TypedPolymorphicDeserializer<Generator, GeneratorType> {

    @Override
    protected GeneratorType parseType(String rawType) {
        return GeneratorType.fromString(rawType);
    }

    @Override
    protected Class<? extends Generator> determineConcreteType(GeneratorType type) {

        switch (type) {

            case JS_FUNCTION:
                return JsFunctionGenerator.class;

            case SCHEMA_BASED:
                return SchemaBasedGenerator.class;

            default:
                return null;
        }
    }
}
