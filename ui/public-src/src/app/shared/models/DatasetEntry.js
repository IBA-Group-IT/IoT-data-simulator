import { observable, computed, action, extendObservable, toJS } from "mobx";
import { fromPromise } from 'mobx-utils';


//TODO move to models

export default class DatasetEntry {
    id;
    @observable name;
    @observable type;
    @observable bucketOptions;
    
    constructor({ id, name, type = 'json', ...bucketOptions } = {}) {
        Object.assign(this, { id, name, type });
        if(bucketOptions.bucket) {
            this.setBucketOptions(bucketOptions);
        }
    }

    setId(id) {
        this.id = id;
    }

    setName(name) {
        this.name = name;
    }

    setType(type) { 
        this.type = type;
    }

    setBucketOptions(bucketOptions) { 
        this.bucketOptions = bucketOptions;
    }

    @computed
    get hasBucketOptions() { 
        return this.bucketOptions && this.bucketOptions.url;
    }

    toJSON() {
        let { id, name, type, bucketOptions } = this
        return {
            id: this.id,
            name: this.name,
            type: this.type,
            ...bucketOptions
        }
    }

}   