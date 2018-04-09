
import { observable, computed, action, toJS } from "mobx";

import DeviceProperty from './DeviceProperty';
import targetSystemFactory from 'models/system/factory';

export default class DeviceEntry {

    id
    @observable name = '';
    @observable targetSystems = [];
    @observable properties = [];

    constructor({ id, name = '', targetSystems = [], properties = [] } = {}) { 
        this.id = id;
        this.setName(name);
        this.setTargetSystems(targetSystems);
        this.setProperties(properties.map((params) => {
            return new DeviceProperty(params);
        }));
    }

    @action.bound
    setName(name) { 
        this.name = name;
    }

    @action.bound
    setTargetSystems(systems) { 
        this.targetSystems = systems.map(targetSystemFactory);
    }

    @action.bound
    setProperties(properties) { 
        this.properties = properties;
    }

    @action.bound
    addProperty(property = new DeviceProperty()) { 
        this.properties.push(property);
    }

    @action.bound
    deleteProperty(idx) { 
        let properties = this.properties.toJS();
        let newProperties = properties.slice(0, idx).concat(properties.slice(idx + 1));
        this.setProperties(newProperties);
    }

    @action.bound
    addTargetSystem(system) { 
        this.targetSystems.push(system);
    }

    getSystemPositionIndex(system) {
        let { targetSystems } = this;
        let idx = -1;
        for(let i = 0; i < targetSystems.length; i++) { 
            if(targetSystems[i].id === system.id) {
                idx = i;
                break;
            }
        }
        return idx;
    }

    findTargetSystemByType(type) { 
        for(let i = 0; i < this.targetSystems.length; i++) { 
            if(this.targetSystems[i].type === type) {
                return this.targetSystems[i];
            }
        }
        return null;
    }

    @action.bound
    updateTargetSystem(system) { 
        let { targetSystems } = this;
        let idx = this.getSystemPositionIndex(system);
        if(idx !== -1) {
            this.targetSystems = targetSystems.slice(0, idx).concat(system).concat(targetSystems.slice(idx + 1));
        }
    }
    
    @action.bound
    deleteTargetSystem(system) { 
        let { targetSystems } = this;
        let idx = this.getSystemPositionIndex(system);
        if(idx !== -1) { 
            this.targetSystems = targetSystems.slice(0, idx).concat(targetSystems.slice(idx + 1));
        }
    }

    @computed
    get data() { 
        return toJS({
            name: this.name,
            targetSystems: this.targetSystems,
            properties: this.properties.map(prop => prop.data)
        })
    }

    @action.bound
    toJSON() { 
        return {
            ...toJS(this),
            properties: this.properties.map((prop) => {
                return prop.toJSON();
            }),
            targetSystems: this.targetSystems.map((system) => {
                return system.toJSON();
            })
        }
    }

}