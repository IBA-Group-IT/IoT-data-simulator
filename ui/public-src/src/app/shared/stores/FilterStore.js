import { observable, computed, action } from "mobx";

export default class FilterStore {
    constructor({ criteriaOptions = {}, values = {} } = {}) {
        this.criteriaOptions = criteriaOptions;
        this.values = observable(values);
    }

    @action.bound
    setFilterValue(type, value) {
        this.values[type] = value;
    }

    filter(items) {
        return computed(() => {
            let filteredArr = items || [];
            Object.keys(this.criteriaOptions).forEach(key => {
                let criteria = this.criteriaOptions[key];
                let filterValue = this.values[key];
                filteredArr = filteredArr.filter(criteria(filterValue));
            });
            return filteredArr;
        }).get();
    }
}