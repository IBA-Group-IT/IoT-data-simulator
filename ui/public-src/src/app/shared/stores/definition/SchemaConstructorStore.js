import {
    observable,
    computed,
    extendObservable,
} from "mobx";

import { transformProperty } from './SchemaPropertyForm';
import JsonSchema from 'models/schema/JsonSchema';

export default class SchemaConstructorStore {

    constructor(state) {
        this.state = state;
    }

    @computed
    get schema() { 
        return this.state.schema;
    }

    // Reactive transformation schema -> schema form
    @computed
    get displaySchema() {
        if (this.schema) {
            return transformProperty(this.schema);
        }
        return null;
    }

}
