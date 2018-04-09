package com.iba.iot.datasimulator.common.json;

import com.iba.iot.datasimulator.session.model.active.filter.CustomFunctionFilter;
import com.iba.iot.datasimulator.session.model.active.filter.DatasetEntryPositionFilter;
import com.iba.iot.datasimulator.session.model.active.filter.DatasetFilter;
import com.iba.iot.datasimulator.session.model.active.filter.DatasetFilterType;

/**
 *
 */
public class DatasetFilterDeserializer extends TypedPolymorphicDeserializer<DatasetFilter, DatasetFilterType> {

    @Override
    protected DatasetFilterType parseType(String rawType) {
        return DatasetFilterType.fromString(rawType);
    }

    @Override
    protected Class<? extends DatasetFilter> determineConcreteType(DatasetFilterType datasetFilterType) {

        switch (datasetFilterType) {

            case DATASET_ENTRY_POSITION:
                return DatasetEntryPositionFilter.class;

            case CUSTOM_FUNCTION:
                return CustomFunctionFilter.class;

            default:
                return null;
        }
    }
}
