import { observable, computed, action, autorun, toJS } from "mobx";
import targetSystemFactory from "models/system/factory";

export default class UpdateSystemStore {
    appStore;

    @observable.ref caCert = null;
    @observable.ref deviceCert = null;
    @observable.ref keyCert = null;

    constructor(appStore, systemManagementStore, systemFormStore) {
        this.appStore = appStore;
        this.systemManagementStore = systemManagementStore;
        this.formStore = systemFormStore;
    }

    @computed
    get system() { 
        return this.systemManagementStore.entity;
    }

    @computed
    get systemForm() { 
        return this.formStore.systemForm;
    }

    @computed
    get formIsValid() { 
        return this.systemForm.isValid;
    }

    @computed
    get systemType() {
        return this.system.type;
    }

    load() {
        this.appStore.systemsStore.getAll();
    }

    @action.bound
    clearCreateSystem() {
        this.creationSystem = targetSystemFactory({});
    }
}
