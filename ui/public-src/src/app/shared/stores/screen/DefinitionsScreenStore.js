import { observable, computed, action } from "mobx";

export default class DefinitionsScreenStore {
    appStore;

    constructor(appStore) {
        this.appStore = appStore;
    }

    @action.bound
    load() {
        this.appStore.datasetsStore.getAll();
        return this.appStore.definitionsStore.getAll();
    }
}
