import { observable, action, computed, autorun, toJS } from 'mobx';


export default class TimerEntry {
    
    @observable type;

    constructor({ type = 'interval', ...restOptions }) {
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
            replayRate: data.has('replayRate') ? parseFloat(data.get('replayRate')) : 1,
            type
        });
    }

}