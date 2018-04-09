package com.iba.iot.datasimulator.common.json;

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @param <T>
 * @param <E>
 */
public abstract class TypedPolymorphicDeserializer<T, E> extends PolymorphicDeserializer<T>{

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(TypedPolymorphicDeserializer.class);

    /** **/
    public static final String TYPE_PROPERTY = "type";

    /**
     *
     * @param node
     * @return
     */
    protected Class<? extends T> determineType(JsonNode node) {

        JsonNode jsonNode = node.get(TYPE_PROPERTY);
        if (jsonNode != null) {

            String rawType = jsonNode.asText();
            E type = parseType(rawType);
            if (type != null) {

                Class<? extends T> result = determineConcreteType(type);
                if (result == null) {

                    logger.error(">>> Unsupported entity type: {}", type);
                    throw new RuntimeException("Unsupported entity type error.");
                }

                return result;
            }
        }

        logger.error(">>> Cannot find type property in node: {}", node);
        throw new RuntimeException("Typed entity deserialization error.");
    }

    /**
     *
     * @param rawType
     * @return
     */
    protected abstract E parseType(String rawType);

    /**
     *
     * @param type
     * @return
     */
    protected abstract Class<? extends T> determineConcreteType(E type);
}
