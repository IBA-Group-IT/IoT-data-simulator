import { observable, action, computed, autorun, toJS } from 'mobx';


export default class DeviceInjectorEntry {
    
    @observable rule;

    constructor({ rule, ...restOptions }) {
        this.rule = rule;
        this.data = observable.map({});
        Object.keys(restOptions).forEach((key) => {
            this.data.set(key, restOptions[key]);
        });
    }

    @action.bound
    setRule(rule) {
        this.rule = rule;
        this.resetData();
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

    toJSON(){
        let { data, rule } = this;
        return toJS({
            ...toJS(data),
            rule
        });
    }

}