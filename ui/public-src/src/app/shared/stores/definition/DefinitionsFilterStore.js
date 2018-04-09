import { observable, computed, action } from "mobx";
import FilterStore from '../FilterStore';
import { filterByName } from 'util/filter';

export const filterTypes = {
    byDataset: "byDataset",
    byName: "byName"
};

export const filterByDatasetTypes = {
    all: "all",
    hasDataset: "hasDataset",
    noDataset: "noDataset"
};

const filterByDataset = filterType => {
    if (filterType === filterByDatasetTypes.all) {
        return item => true;
    }
    return item => {
        return filterType === filterByDatasetTypes.hasDataset
            ? item.hasDataset
            : !item.hasDataset;
    };
};

export default class DefinitionsFilterStore extends FilterStore {

    constructor(props = {}) {

        super(props);

        this.criteriaOptions = {
            [filterTypes.byName]: filterByName,
            [filterTypes.byDataset]: filterByDataset
        };

        this.values = observable(props.values || {
            [filterTypes.byName]: "",
            [filterTypes.byDataset]: filterByDatasetTypes.all
        });
    }

}