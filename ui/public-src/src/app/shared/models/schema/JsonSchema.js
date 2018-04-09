import { observable, computed, action } from "mobx";
import JsonSchemaProperty, { types } from "./JsonSchemaProperty";

const schemaKey = "http://json-schema.org/draft-04/schema#";

export const SchemaTypes = {
    csv: 'csv',
    json: 'json'
}

export default class JsonSchema extends JsonSchemaProperty {
    isRoot = true;
    @observable schemaType;

    constructor({
        type = types.object,
        metadata = {
            type: SchemaTypes.json
        },
        ...props
    } = {}) {

        super(
            {
                ...props,
                type,
                metadata,
                schemaKey
            },
            {
                schemaType: metadata.type
            }
        );

        this.setType(type);
        this.isRoot = true;
    }

    getAvailableTypes() { 
        if(this.schemaType === SchemaTypes.csv) { 
            return [types.object];
        }
        return [types.object, types.array];
    }

    get parameters() { 
        return [];
    }

    getTypesConfig() {
        return {
            csv: {
                jsonSchemaType: "object"
            },
            json: {
                jsonSchemaType: "object"
            }
        };
    }

    getCommonParameters() {
        return {};
    }
    
    @action.bound
    setType(type) { 
       this.root.type = type;
    }

    @computed
    get type() {
        return this.root.type;
    }

    @action.bound
    setSchemaType(type) { 
        if(type === SchemaTypes.csv) {
            this.setType(types.object);
        }
        this.schemaType = type;
        this.root.metadata.set('type', type);
        this.properties.forEach(property => {
            property.setSchemaType(type);
        });
        this.root.items.forEach((prop) => prop.setSchemaType(type));
    }

    static isFlat(options) {
        let isFlat = true;
        if (options.items) {
            isFlat = false;
        } else if (options.properties) {
            let props = Object.keys(options.properties);
            for (let i = 0; i < props.length; i++) {
                let schemaProp = options.properties[props[i]];
                if (
                    schemaProp.type === types.array ||
                    schemaProp.type === types.object
                ) {
                    isFlat = false;
                }
            }
        }
        return isFlat;
    }

    traverse(cb) { 
        cb(this);
        if(this.type === types.object) { 
            this.properties.forEach(prop => traverse(prop));
        } else if (this.type === types.array) { 
            this.items.forEach(item => traverse(item));
        }
    }

}
