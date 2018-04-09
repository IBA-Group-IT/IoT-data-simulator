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
import java.util.Collection;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonDeserialize
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArraySchemaModel implements ArraySchema {

    @NotEmpty
    @Getter
    private final String schemaKey = "http://json-schema.org/draft-04/schema#";

    @Valid
    @NotNull
    @JsonView(SchemaViews.Short.class)
    @Embedded
    private SchemaRootMetadata metadata;

    /** **/
    @NotNull
    private SchemaType type = SchemaType.ARRAY;

    @NotNull
    @Valid
    private Collection<SchemaProperty> items;

    /**
     *
     * @param type
     * @param items
     */
    public ArraySchemaModel(SchemaType type, Collection<SchemaProperty> items) {
        this.type = type;
        this.items = items;
    }
}
