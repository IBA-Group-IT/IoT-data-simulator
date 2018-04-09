package com.iba.iot.datasimulator.common.util;

import com.iba.iot.datasimulator.common.model.ModelEntity;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 */
public class ModelEntityUtil {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(ModelEntityUtil.class);

    /**
     *
     * @param entity
     * @param id
     */
    public static void setId(ModelEntity entity, String id){

        logger.debug(">>> Setting id: {} to model entity: {}.", id, entity);

        if (ObjectId.isValid(id)) {
            ObjectId objectId = new ObjectId(id);
            entity.setId(objectId);
        } else {
            throw new IllegalArgumentException("Wrong id provided.");
        }
    }

    /**
     *
     * @param entity
     */
    public static void updateModified(ModelEntity entity) {
        entity.setModified(TimeUtil.getUnixTime());
    }

    /**
     *
     * @param entities
     * @param <T>
     * @return
     */
    public static <T extends ModelEntity> Collection<T> sortByModified(Collection<T> entities) {

        if (entities != null && !entities.isEmpty()) {

            List<T> list = new ArrayList<>(entities);
            list.sort((first, second) -> (int)(second.getModified() - first.getModified()));
            return list;
        }

        return entities;
    }
}
