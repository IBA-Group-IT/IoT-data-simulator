import { observable, computed, action, autorun, toJS } from "mobx";
import targetSystemFactory from "models/system/factory";

export default class SystemsScreenStore {
    appStore;

    constructor(appStore) {
        this.appStore = appStore;
    }

    @action.bound
    load() {
        this.appStore.systemsStore.getAll();
    }
}
