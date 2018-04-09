import { observable, computed, action } from "mobx";
import { fromPromise } from "mobx-utils";
import systemTypes from "models/system/types";
import targetSystemFactory from "models/system/factory";

export default class SystemsStore {
    appStore;

    filterTypes = {
        all: "all"
    };

    @observable.ref items;
    @observable nameFilter = "";
    @observable typeFilter = this.filterTypes.all;

    constructor(appStore) {
        this.appStore = appStore;
        systemTypes.forEach(option => {
            this.filterTypes[option.value] = option.label;
        });
    }

    @action.bound
    setNameFilter(filter) {
        this.nameFilter = filter;
    }

    @action.bound
    setTypeFilter(filter) {
        this.typeFilter = filter;
    }

    @computed
    get filteredItems() {
        if (this.items) {
            let filtered = this.items;

            if (this.nameFilter) {
                filtered = filtered.filter(item => {
                    return item.name.toLowerCase().indexOf(this.nameFilter.toLowerCase()) !== -1;
                });
            }

            if (this.typeFilter !== this.filterTypes.all) {
                filtered = filtered.filter(item => {
                    return item.type === this.typeFilter;
                });
            }
            return filtered;
        }
        return [];
    }

    getAll() {
        console.log("...loading target systems");
        return this.appStore.transportLayer
            .get("/api/systems")
            .then(({ data = [] }) => {
                console.log("target systems loaded: ", data);
                this.items = data.map(targetSystemFactory);
            });
    }

    findInstanceById(id) {
        let result = null;
        if (this.items) {
            for (let i = 0; i < this.items.length; i++) {
                if (this.items[i].id === id) {
                    result = this.items[i];
                    break;
                }
            }
        }
        return result;
    }

    create(params) {
        return this.appStore.transportLayer.post("/api/systems", {
            ...params
        });
    }

    update(systemId, params) {
        if (!systemId) {
            return this.create(params);
        }
        return this.appStore.transportLayer.put(`/api/systems/${systemId}`, {
            ...params
        });
    }

    delete(systemId) {
        console.log("...deleting target system: ", systemId);
        return this.appStore.transportLayer
            .delete(`/api/systems/${systemId}`)
            .then(({ data }) => {
                console.log(`${systemId} successfully deleted`);
                return targetSystemFactory(data);
            });
    }

    getById(systemId) {
        return this.appStore.transportLayer
            .get(`/api/systems/${systemId}`)
            .then(({ data }) => {
                return targetSystemFactory(data);
            });
    }
}
