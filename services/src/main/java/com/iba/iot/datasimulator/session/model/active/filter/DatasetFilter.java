package com.iba.iot.datasimulator.session.model.active.filter;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.iba.iot.datasimulator.common.json.DatasetFilterDeserializer;
import com.iba.iot.datasimulator.common.model.TypedEntity;

/**
 *
 */
@JsonDeserialize(using = DatasetFilterDeserializer.class)
public interface DatasetFilter extends TypedEntity<DatasetFilterType> {}
