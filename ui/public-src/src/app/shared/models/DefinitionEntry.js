import { observable, computed, action, extendObservable, toJS } from "mobx";
import { fromPromise } from 'mobx-utils';

import DatasetEntry from './DatasetEntry';
import JsonSchema from 'models/schema/JsonSchema';

//TODO move to models

export default class DefinitionEntry {
    id;
    @observable dataset = new DatasetEntry();
    @observable schema;
    @observable name;
    
    constructor({dataset, schema, ...params} = {}) {
        this.setDataset(new DatasetEntry(dataset));
        if(schema) { 
            this.setSchema(new JsonSchema(schema))
        }
        extendObservable(this, params);
    }

    setId(id) { 
        this.id = id;
    }

    @action.bound
    setDataset(dataset) {
        this.dataset = dataset || new DatasetEntry();
        if(this.schema && dataset && dataset.type) {
            this.schema.setSchemaType(dataset.type);
        }
    }

    setName(name) {
        this.name = name;
    }

    setSchema(schema) { 
        this.schema = schema;
    }

    @computed
    get hasDataset() { 
        return !!(this.dataset && this.dataset.id);
    }

    toJSON() {
        let definition = toJS(this);
        let dataset = this.dataset ? this.dataset.toJSON() : null;
        let schema = this.schema ? this.schema.toJSON() : null;
        definition.dataset = dataset;
        definition.schema = schema;
        return definition;
    }
}   