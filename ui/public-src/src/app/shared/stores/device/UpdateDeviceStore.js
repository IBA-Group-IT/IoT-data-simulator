import { observable, computed, action, autorun, toJS } from "mobx";

import BaseForm from "components/Form";
import DeviceEntry from 'models/device/DeviceEntry';
import formConfigBuilder from 'models/device/formConfigBuilder';

export default class UpdateDeviceStore {
    appStore;

    @observable.ref deviceForm = null;

    constructor(appStore, deviceManagementStore, wizardStore) {
        this.appStore = appStore;
        this.deviceManagementStore = deviceManagementStore;
        this.wizardStore = wizardStore;
        this.initForm();
    }

    initForm() { 
         
        let getIndex = (path) => {
            return parseInt(path.split('.')[1], 10);
        }

        let onValueChange = (field) => {
            let idx = getIndex(field.container().path);
            this.device.properties[idx].setVal(field.value);
        }
        
        this.hooks = {   
            'name': {
                onChange: (field) => {
                    this.device.setName(field.value);
                } 
            },
            'properties': {
                onAdd: field => {
                    this.device.addProperty();
                }
            },
            'properties[]': {
                onDel: field => {
                    let idx = getIndex(field.path);
                    this.device.deleteProperty(idx);
                }
            },
            'properties[].name': {
                onChange: (field) => {
                    let idx = getIndex(field.container().path);
                    this.device.properties[idx].setName(field.value);
                }
            },
            'properties[].type': {
                onChange: (field) => {
                    let idx = getIndex(field.container().path);
                    this.device.properties[idx].setType(field.value);
                }
            },
            'properties[].stringVal': { onChange: onValueChange },
            'properties[].numberVal': { onChange: onValueChange },
            'properties[].boolVal': { onChange: onValueChange }
        }

        autorun(() => {
            this.deviceForm = new BaseForm(this.deviceFormOptions);
        });
    }


    @computed
    get deviceFormOptions() {
        let { device } = this;
        let config = formConfigBuilder.build(device);
        let { data } = device;

        let newConfig = {
            ...config,
            hooks: this.hooks,
            values: {
                ...data,
                properties: data.properties.map((prop) => {
                    let valField = formConfigBuilder.getPropValueField(prop);
                    let options = {
                        name: prop.name,
                        type: prop.type
                    }
                    // if(prop.type === 'boolean' && !prop.val && typeof prop.val !== 'boolean') {
                    //     options[valField] = 'true';
                    // }else{
                        options[valField] = prop.val;
                    // }
                    return options;
                })
            }
        }
        console.log('new devices config:', newConfig);

        return newConfig;
    }

    getPropValueField(propType) { 
        return formConfigBuilder.getPropValueField({ 
            type: propType
        });
    }

    @computed
    get isEditing() {
        return !!this.editingDeviceId;
    }

    @computed
    get deviceFormIsValid() {
        return this.deviceForm.isValid;
    }

    @computed 
    get device() { 
        return this.deviceManagementStore.entity;
    }

    load() {
        this.appStore.devicesStore.getAll();
    }

    @action.bound
    reset() {
        this.deviceManagementStore.reset();
        this.wizardStore.reset();
    }

}
