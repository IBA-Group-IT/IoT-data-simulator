import { observable, action, computed, autorun, toJS } from 'mobx';


export default class DatasetFilterEntry {
    
    @observable type;

    constructor({ type, ...options }) {
        this.type = type;
        this.data = observable.map({});
        Object.keys(options).forEach((key) => {
            this.data.set(key, options[key]);
        });
    }

    @action.bound
    setParameter(name, value) { 
        this.data.set(name, value);
    }

    getParameter(name) {
        return this.data.get(name);
    }

    @action.bound
    resetData() { 
        this.data.clear();
    }

    @action.bound
    setType(type) {
        if(type !== this.type) { 
            this.resetData();
        }
        this.type = type;
    }

    toJSON(){
        let { data, type } = this;
        return toJS({
            ...toJS(data),
            type
        });
    }

}