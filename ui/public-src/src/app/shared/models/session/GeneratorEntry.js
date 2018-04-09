import { observable, action, computed, autorun, toJS } from 'mobx';
import JsonSchema from 'models/schema/JsonSchema';

export const types = {
    jsFunction: 'js_function',
    schema: 'schema_based'
}

//TODO remove duplication, extend class
export default class GeneratorEntry {
    
    @observable type;
    @observable.ref schema;
    @observable jsFunction;

    constructor({ type, schema = {}, jsFunction = '' }) {
        this.type = type;
        this.init({ schema, jsFunction });
    }

    init({ schema, jsFunction }) { 
        if(this.type === types.schema) { 
            this.schema = new JsonSchema(schema);
        } else if(this.type === types.jsFunction) { 
            this.jsFunction = jsFunction;
        }
    }

    @action.bound
    setType(type) {
        if(type !== this.type) { 
            //reset
            this.init({});
        }
        this.type = type;
    }

    @action.bound
    setJSFunction(jsFunction) {
        this.jsFunction = jsFunction;
    }

    @action.bound
    setSchema(schema) { 
        this.schema = schema; 
    }

    toJSON(){
        let params = {
            type: this.type
        }

        if(this.type === types.jsFunction) { 
            params.jsFunction = this.jsFunction;
        } else if (this.type === types.schema) { 
            params.schema = this.schema.toJSON();
        }
        return params;
    }

}