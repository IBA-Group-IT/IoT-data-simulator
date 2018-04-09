package com.iba.iot.datasimulator.target.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.iba.iot.datasimulator.common.json.TargetSystemDeserializer;
import com.iba.iot.datasimulator.common.model.ModelEntity;
import com.iba.iot.datasimulator.common.model.TargetSystemType;
import com.iba.iot.datasimulator.common.model.TypedEntity;
import com.iba.iot.datasimulator.common.model.security.Security;
import com.iba.iot.datasimulator.target.model.serialization.Serializer;
import org.mongodb.morphia.annotations.Entity;

import java.io.Serializable;
import java.util.Collection;

@Entity(value = "targetSystem")
@JsonDeserialize(using = TargetSystemDeserializer.class)
public interface TargetSystem extends TypedEntity<TargetSystemType>, ModelEntity, Serializable {

    /**
     *
     * @return
     */
    String getName();

    /**
     *
     * @return
     */
    Security getSecurity();

    /**
     *
     * @return
     */
    void setSecurity(Security security);

    /**
     *
     * @return
     */
    Collection<Header> getHeaders();

    /**
     *
     * @param headers
     */
    void setHeaders(Collection<Header> headers);

    /**
     *
     * @return
     */
    Serializer getMessageSerializer();

    /**
     *
     */
    void setMessageSerializer(Serializer serializer);
}
