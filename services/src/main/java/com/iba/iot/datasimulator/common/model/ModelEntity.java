package com.iba.iot.datasimulator.common.model;

import io.swagger.annotations.ApiModelProperty;
import org.bson.types.ObjectId;

/**
 *
 */
public interface ModelEntity {

    /**
     *
     * @param id
     */
    void setId(ObjectId id);

    /**
     *
     * @return
     */
    @ApiModelProperty(dataType = "java.lang.String")
    ObjectId getId();

    /**
     *
     * @param timestamp
     */
    void setModified(long timestamp);

    /**
     *
     * @return
     */
    long getModified();
}
