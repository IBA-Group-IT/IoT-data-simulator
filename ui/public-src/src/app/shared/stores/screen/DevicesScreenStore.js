import { observable, computed, action, autorun, toJS } from "mobx";

import BaseForm from "components/Form";
import DeviceEntry from 'models/device/DeviceEntry';
import formConfigBuilder from 'models/device/formConfigBuilder';

export default class SystemsScreenStore {
    appStore;

    constructor(appStore) {
        this.appStore = appStore;
    }

    load() {
        this.appStore.devicesStore.getAll();
    }


}
