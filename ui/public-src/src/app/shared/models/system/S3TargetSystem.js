import { observable, computed, action, extendObservable, toJS } from "mobx";
import TargetSystemEntry from './TargetSystemEntry';

export default class S3TargetSystem extends TargetSystemEntry {

    @observable dataset = null;

    constructor({ dataset, ...rest }) {
        super(rest);
        this.dataset = dataset;
        this.security = null;
        this.headers = null;
    }

    setDataset(dataset) {
        this.dataset = dataset;
    }

    @computed
    get data() { 
        return {
            name: this.name,
            type: this.type,
            dataset: this.dataset,
            messageSerializer: toJS(this.messageSerializer)
        }
    }
}
