package com.iba.iot.datasimulator.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 *
 */
public class RemovalValidationUtil {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(RemovalValidationUtil.class);

    /**
     *
     * @param linkedEntitiesGetter
     */
    public static <T> void checkReferences(String entityId, Function<String, List<T>> linkedEntitiesGetter, Function<T, String> nameProvider) {

        List<T> linkedEntities = linkedEntitiesGetter.apply(entityId);
        if (!linkedEntities.isEmpty()) {

            String linkedEntityNames = linkedEntities.stream()
                    .map(nameProvider)
                    .collect(Collectors.joining(StringUtil.COMMA_WITH_SPACE));

            logger.error(">>> Cannot remove {} entity as it is linked by the entities: {}", entityId, linkedEntityNames);
            throw new IllegalArgumentException("Cannot remove entity due to references from: " + linkedEntityNames);
        }
    }
}
