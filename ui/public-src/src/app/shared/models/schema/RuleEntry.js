
import { observable, extendObservable, toJS, action } from 'mobx';

export default class RuleEntry {

    @observable type;

    constructor({type, ...restOptions} = {}) { 
        this.type = type;
        this.data = observable.map({});
        Object.keys(restOptions).forEach((key) => {
            this.data.set(key, restOptions[key]);
        });
    }

    @action.bound
    setType(type) {
        this.type = type;
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
        let { data, type } = this;
        return toJS({
            ...toJS(data),
            type
        });
    }

}