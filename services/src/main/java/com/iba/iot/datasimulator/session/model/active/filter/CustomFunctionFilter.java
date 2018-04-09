package com.iba.iot.datasimulator.session.model.active.filter;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.iba.iot.datasimulator.session.model.SessionViews;
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
public class CustomFunctionFilter implements DatasetFilter {

    @JsonView(SessionViews.Short.class)
    private final DatasetFilterType type = DatasetFilterType.CUSTOM_FUNCTION;

    @JsonView(SessionViews.Short.class)
    @NotEmpty
    private String jsFunction;
}
