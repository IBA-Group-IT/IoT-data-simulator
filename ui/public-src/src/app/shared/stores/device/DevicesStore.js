import { observable, computed, action } from "mobx";
import { fromPromise } from 'mobx-utils';

import DeviceEntry from 'models/device/DeviceEntry';

export default class DevicesStore {
    appStore;
    @observable.ref items;
    @observable nameFilter = "";

    constructor(appStore) {
        this.appStore = appStore;
    }

    @action.bound
    setNameFilter(filter) {
        this.nameFilter = filter;
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

            return filtered;
        }
        return [];
    }

    getAll() {
        console.log('...loading devices');
        return this.appStore.transportLayer.get('/api/devices')
            .then(({ data = [] }) => {
                console.log('devices loaded: ', data);
                this.items = data.map((params) => {
                    return new DeviceEntry(params);
                });
            });
    }

    findInstanceById(id) { 
        let result = null;

        if(this.items) { 
            for(let i = 0; i < this.items.length; i++) { 
                if(this.items[i].id === id) {
                    result = this.items[i];
                    break;
                }
            }
        }

        return result;
    }

    create(params) {
        return this.appStore.transportLayer.post('/api/devices', {
            ...params
        })
    }

    //TODO move to entry
    update(deviceId, params) { 
        if(!deviceId) {
            return this.create(params);
        }
        return this.appStore.transportLayer.put(`/api/devices/${deviceId}`, {
            ...params
        })
    }

    delete(deviceId) {
        console.log('...deleting device ', deviceId);
        return this.appStore.transportLayer.delete(`/api/devices/${deviceId}`).then(({ data }) => {
            console.log(`${deviceId} successfully deleted`);
            return data;
        });
    }

    getById(deviceId) { 
        console.log('...get details for device ', deviceId);
        return this.appStore.transportLayer.get(`/api/devices/${deviceId}`).then(({ data }) => {
            console.log(`${deviceId} details received:`, data);
            return new DeviceEntry(data);
        });
    }

}   