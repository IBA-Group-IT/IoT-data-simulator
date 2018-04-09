import { observable, computed, action, extendObservable, toJS } from "mobx";
import idUtils from "util/id";

import RuleEntry from "./RuleEntry";

export const types = {
    array: "array",
    object: "object",
    string: "string",
    timestamp: "timestamp",
    date: "date",
    integer: "integer",
    double: "double",
    long: "long",
    boolean: "boolean"
};

export default class JsonSchemaProperty {
    @observable schemaType;
    @observable.ref rule;
    @observable isInArray;

    id = idUtils.generate();

    /**
     * 
     * @param {Object} schemaProperties
     * @param {Object} configProperties (name, schemaType, isInArray)
     */
    constructor(
        {
            metadata = {},
            type = "string",
            properties = {},
            items = [],
            rule = {},
            ...rest
        },
        { schemaType = "csv", isInArray = false, parent = null } = {}
    ) {
        if (!this.isRoot) {
            this.parent = parent;
        }

        let propType = metadata.type || type;
        let typesKeys = Object.keys(this.getTypesConfig());

        if (!this.validateType(propType, typesKeys)) {
            throw Error(`Not valid type for Property class: ${propType}`);
        }

        this.root = {};
        extendObservable(this.root, {
            type,
            items: observable([]),
            properties: observable.map({}),
            metadata: observable.map(metadata),
            ...rest
        });

        this.setType(propType);
        this.setSchemaType(schemaType);

        if (type === types.object) {
            Object.keys(properties).forEach(nestedPropKey => {
                this.addProperty(
                    new JsonSchemaProperty(properties[nestedPropKey], {
                        schemaType,
                        parent: this
                    })
                );
            });
        } else if (type === types.array) {
            items.forEach(item =>
                this.addItem(
                    new JsonSchemaProperty(item, {
                        schemaType,
                        isInArray: true,
                        parent: this
                    })
                )
            );
        }

        this.rule = new RuleEntry(rule);
        this.isInArray = isInArray;
    }

    @action.bound
    setRule(rule) {
        this.rule = rule;
    }

    getRule() {
        return this.rule;
    }

    @action.bound
    setName(name) {
        this.setParameter("name", name);
    }

    @action.bound
    setSchemaType(type) {
        this.schemaType = type;
        this.root.properties.forEach(property => {
            property.setSchemaType(type);
        });
    }

    @action.bound
    setType(type) {
        let typeConfig = this.getTypeConfig(type);
        if (!typeConfig) {
            throw Error("Invalid Property type: ", type);
        }

        // No need to set metadata type if its equal to root type
        this.root.type = typeConfig.jsonSchemaType;
        this.root.metadata.delete("type");
        if (this.root.type !== type) {
            this.root.metadata.set("type", type);
        }

        let parameters = this.getConfigForType(type);

        Object.keys(parameters).forEach(paramKey => {
            let currentValue = this.getParameter(paramKey);
            if (currentValue) {
                parameters[paramKey] = currentValue;
            }
        });

        this.clearParameters();
        this.setParameters(parameters);

        if (this.root.type !== "object") {
            this.root.properties.clear();
        }
    }

    @computed
    get type() {
        return this.root.metadata.get("type") || this.root.type;
    }

    getConfigForType(type) {
        let config = this.getTypeConfig(type).parameters;
        let commonConfig = this.getCommonParameters();

        return {
            ...config,
            ...commonConfig
        };
    }

    getTypeConfig(type) {
        return this.getTypesConfig()[type];
    }

    getTypesConfig() {
        return {
            [types.array]: {
                jsonSchemaType: "array"
            },
            [types.object]: {
                jsonSchemaType: "object"
            },
            [types.string]: {
                jsonSchemaType: "string",
                parameters: {
                    name: ""
                }
            },
            [types.timestamp]: {
                jsonSchemaType: "number",
                parameters: {
                    format: "seconds",
                    name: ""
                }
            },
            [types.date]: {
                jsonSchemaType: "string",
                parameters: {
                    format: "",
                    name: ""
                }
            },
            [types.integer]: {
                jsonSchemaType: "number",
                parameters: {
                    name: ""
                }
            },
            [types.double]: {
                jsonSchemaType: "number",
                parameters: {
                    name: ""
                }
            },
            [types.long]: {
                jsonSchemaType: "number",
                parameters: {
                    name: ""
                }
            },
            [types.boolean]: {
                jsonSchemaType: "boolean",
                parameters: {
                    name: ""
                }
            }
        };
    }

    getAvailableTypes() {
        return Object.keys(types);
    }

    /** Properties */
    @computed
    get properties() {
        let { properties } = this.root;
        if (properties) {
            return properties.values();
        }
    }

    @action.bound
    addProperty(
        property = new JsonSchemaProperty(
            { type: "string" },
            {
                schemaType: this.schemaType,
                parent: this
            }
        )
    ) {
        this.root.properties.set(property.id, property);
    }

    @action.bound
    removeProperty(property) {
        this.root.properties.delete(property.id);
    }

    @action.bound
    addItem(
        property = new JsonSchemaProperty(
            { type: "string" },
            {
                schemaType: this.schemaType,
                parent: this,
                isInArray: true
            }
        )
    ) {
        this.root.items.push(property);
    }

    @action.bound
    removeItem(property) {
        let idx = this.root.items.indexOf(property);
        if (idx !== -1) {
            this.root.items.replace(
                this.root.items
                    .slice(0, idx)
                    .concat(this.root.items.slice(idx + 1))
            );
        }
    }

    @action.bound
    delete() {
        let { parent } = this;
        if (parent) {
            if (parent.type === types.object) {
                parent.removeProperty(this);
            } else if (parent.type === types.array) {
                parent.removeItem(this);
            }
        }
    }

    @computed
    get items() {
        return this.root.items;
    }

    /** Parameters */

    @computed
    get parameters() {
        let params = this.getConfigForType(this.type);
        let _parameters = this.root.metadata
            .keys()
            .filter(key => key !== "type");
        /* .map(key => {
                return {
                    key: params[key]
                };
            }) */
        return _parameters;
    }

    @action.bound
    setParameters(parameters) {
        Object.keys(parameters).forEach(parameterKey => {
            this.setParameter(parameterKey, parameters[parameterKey]);
        });
    }

    @action.bound
    clearParameters() {
        let { metadata } = this.root;
        let type = metadata.get("type");
        metadata.clear();
        metadata.set("type", type);
    }

    @action.bound
    setParameter(parameter, value) {
        this.root.metadata.set(parameter, value);
    }

    getParameter(parameter) {
        return this.root.metadata.get(parameter);
    }

    getCommonParameters() {
        return {
            position: "",
            description: ""
        };
    }

    /** Util */

    hasNumericPosition() { 
        return this.schemaType === "csv" || this.isInArray
    }

    validateType(type, types) {
        let isValid = false;
        for (let i = 0; i < types.length; i++) {
            if (type === types[i]) {
                isValid = true;
                break;
            }
        }
        return isValid;
    }

    toJSON() {
        let root = this.root;
        let jsRoot = toJS(root);

        delete jsRoot.properties;
        delete jsRoot.items;

        if (this.rule && this.rule.type) {
            jsRoot.rule = this.rule.toJSON();
        }

        if (this.type === types.object) {
            let properties = {};
            let shouldApply = true;
            this.properties.forEach(property => {
                properties[
                    property.getParameter("position")
                ] = property.toJSON();
                shouldApply = true;
            });
            let json = toJS({
                ...jsRoot
            });
            if (shouldApply) {
                json.properties = properties;
            }
            return json;
        }

        if (this.type === types.array) {
            let json = toJS({
                ...jsRoot
            });
            json.items = this.items.map(prop => {
                return prop.toJSON();
            });
            return json;
        }

        return jsRoot;
    }
}
