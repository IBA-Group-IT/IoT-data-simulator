package com.iba.iot.datasimulator.session.model.active.generator;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.iba.iot.datasimulator.session.model.SessionViews;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotEmpty;

@Data
@ToString
@JsonDeserialize
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JsFunctionGenerator implements Generator {

    @JsonView(SessionViews.Short.class)
    @Getter
    private GeneratorType type = GeneratorType.JS_FUNCTION;

    @NotEmpty
    @JsonView(SessionViews.Short.class)
    private String jsFunction;
}
