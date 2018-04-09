package com.iba.iot.datasimulator.common.model.schema;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.iba.iot.datasimulator.common.model.schema.property.SchemaProperty;
import lombok.*;
import org.hibernate.validator.constraints.NotEmpty;
import org.mongodb.morphia.annotations.Embedded;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Map;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonDeserialize
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ObjectSchemaModel implements ObjectSchema {

    @NotEmpty
    @Getter
    private final String schemaKey = "http://json-schema.org/draft-04/schema#";

    @Valid
    @NotNull
    @JsonView(SchemaViews.Short.class)
    @Embedded
    private SchemaRootMetadata metadata;

    @NotNull
    private SchemaType type = SchemaType.OBJECT;

    @NotNull
    @Valid
    private Map<String, SchemaProperty> properties;

    /**
     *
     * @param type
     * @param properties
     */
    public ObjectSchemaModel(SchemaType type, Map<String, SchemaProperty> properties) {
        this.type = type;
        this.properties = properties;
    }
}
