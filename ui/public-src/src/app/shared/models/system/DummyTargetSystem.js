import { observable, computed, action, extendObservable, toJS } from "mobx";
import TargetSystemEntry from './TargetSystemEntry';

export default class DummyTargetSystem extends TargetSystemEntry {
    constructor(params) {
        super(params);
        this.headers = null;
        this.security = null;
    }

    @computed
    get data() { 
        return {
            name: this.name,
            type: this.type
        }
    }
}
