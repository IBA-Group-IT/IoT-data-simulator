package com.iba.iot.datasimulator.target.model.serialization;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.NotEmpty;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonDeserialize
public class ProtobufSerializer implements Serializer {

    /** **/
    private final SerializerType type = SerializerType.PROTOBUF;

    @NotEmpty
    private String protoDescriptor;

    @NotEmpty
    private String protoType;

    @NotEmpty
    private String jsBuilder;
}
