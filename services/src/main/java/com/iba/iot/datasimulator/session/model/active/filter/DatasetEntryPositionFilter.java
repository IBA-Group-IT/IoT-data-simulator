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
public class DatasetEntryPositionFilter implements DatasetFilter {

    @JsonView(SessionViews.Short.class)
    private final DatasetFilterType type = DatasetFilterType.DATASET_ENTRY_POSITION;

    @JsonView(SessionViews.Short.class)
    @NotEmpty
    private String position;

    @JsonView(SessionViews.Short.class)
    private String value;
}
