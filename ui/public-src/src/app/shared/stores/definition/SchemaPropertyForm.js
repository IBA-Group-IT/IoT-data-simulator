import {
    observable,
    computed,
    action,
    untracked,
    createTransformer,
    extendObservable,
    runInAction
} from "mobx";

import FormState from "components/Form/FormState";
import FieldState from "components/Form/FieldState";
import SelectFieldState from "components/Form/SelectFieldState";

import {
    throwsRequired,
    throwsNumeric,
    throwsInteger,
    throwsLong,
    throwsGreaterThan,
    throwsNothing
} from "util/validation";

import { types } from "models/schema/JsonSchemaProperty";

const transformProperty = createTransformer(function(property) {
    return new SchemaPropertyForm(property);
});

const transformParameters = createTransformer(function(property) {
    let schemaType = property.schemaType;
    let type = property.type;

    let isRoot = property.isRoot;
    let isNumericPosition = property.hasNumericPosition();

    if (isRoot) {
        return new FormState({
            schemaType: new SelectFieldState(schemaType, [
                {
                    label: "json",
                    value: "json"
                },
                {
                    label: "csv",
                    value: "csv"
                }
            ]).onUpdate(field => {
                property.setSchemaType(field.value);
            })
        });
    }

    let parametersConfig = {
        position: {
            type: "text",
            validators: isNumericPosition
                ? [throwsRequired, throwsNumeric, throwsGreaterThan(0)]
                : [throwsRequired],
            default: ""
        },

        format:
            property.type === types.timestamp
                ? {
                      type: "select",
                      options: [
                          {
                              label: "milliseconds",
                              value: "milliseconds"
                          },
                          {
                              label: "seconds",
                              value: "seconds"
                          }
                      ],
                      validators: [throwsRequired],
                      default: "seconds"
                  }
                : {
                      type: "text",
                      validators: [throwsRequired],
                      default: ""
                  },

        description: {
            type: "text",
            validators: [throwsNothing],
            default: ""
        },

        name: {
            type: "text",
            validators: [throwsRequired],
            default: ""
        }
    };

    let options = property.parameters.reduce((obj, key) => {
        let config = parametersConfig[key];

        let field =
            config.type === "text"
                ? new FieldState(config.default)
                : new SelectFieldState(config.default, config.options);

        let fieldWithUpdate = field.onUpdate(field => {
            property.setParameter(key, field.value);
        });

        obj[key] = fieldWithUpdate.validators.apply(
            fieldWithUpdate,
            config.validators
        );

        // Set values, untracked since we don't
        // want form to be updated at each keypress
        untracked(() => {
            let value = property.getParameter(key);
            if (typeof value !== "undefined" && value !== null) {
                field.reinitValue(value);
            }
        });

        return obj;
    }, {});

    let form = new FormState(options);
    return form;
});

const transformType = createTransformer(function(property) {
    return new SelectFieldState(
        property.type,
        property.getAvailableTypes().map(type => ({
            label: type,
            value: type
        }))
    ).onUpdate(function(field) {
        property.setType(field.value);
    });
});

const positionValidator = property => $ => {
    return untracked(() => {
        let { parent, type } = property;
        let position = $.position && $.position.$;
        let isNumericPosition = property.hasNumericPosition();

        const getChildPosition = child => child.form.$.parameters.$.position.$;

        if (parent) {
            let children = transformProperty(parent)
                .getProperties()
                .map(transformProperty);

            if (children.length) {
                let samePosition = children.find(child => {
                    if (child.property !== property) {
                        let childPosition = getChildPosition(child);
                        if (childPosition && position) {
                            return childPosition === position;
                        }
                    }
                });

                if (samePosition) {
                    return isNumericPosition
                        ? "Position field should be unique"
                        : "Property field should be unique";
                }

                if (isNumericPosition) {

                    let sortedChildren = children.sort((a, b) => {
                        return getChildPosition(a) - getChildPosition(b);
                    });

                    let propertyWithMinPosition = sortedChildren.map(
                        child => child.property
                    )[0];

                    if (
                        propertyWithMinPosition === property &&
                        parseInt(position, 10) !== 1
                    ) {
                        return "Position should start from 1";
                    }

                    let sortedPositions = sortedChildren.map(getChildPosition);
                    let sparsedChild = null;
                    let sparsedPosition = null;

                    for(let i = 0; i < sortedChildren.length; i++) { 
                        if(i > 0) {
                            let prevPosition = parseInt(getChildPosition(sortedChildren[i - 1]), 10);
                            let currPosition = parseInt(getChildPosition(sortedChildren[i]), 10);

                            if(currPosition - prevPosition !== 1) { 
                                sparsedChild = sortedChildren[i];
                                sparsedPosition = prevPosition + 1;
                                break;
                            }
                        }
                    }   

                    if(sparsedChild && sparsedChild.property === property) { 
                        return `Position ${sparsedPosition} is missing`;
                    }

                }
            }
        }
    });
};

const transformPropertyToForm = createTransformer(function(property) {
    let typeForm = transformType(property);
    let parametersForm = transformParameters(property);

    let form = new FormState({
        type: typeForm,
        parameters: parametersForm
            //.assumeAllFieldsValid()
            .compose()
            .validators(positionValidator(property))
    });
    return form;
});

class SchemaPropertyForm {
    defaultCollapseLevel = 4;
    @observable isCollapsed;

    constructor(property) {
        this.property = property;

        extendObservable(this, {
            get form() {
                return transformPropertyToForm(this.property);
            }
        });

        if (
            this.deep >= this.defaultCollapseLevel &&
            (property.type === "object" || property.type === "array")
        ) {
            this.isCollapsed = true;
        }
    }

    @computed
    get parent() {
        return this.property.parent
            ? transformProperty(this.property.parent)
            : null;
    }

    @action.bound
    setCollapsed(collapsed) {
        this.isCollapsed = collapsed;
    }

    @computed
    get children() {
        if (this.isCollapsed) {
            return [];
        }
        return this.getProperties().map(transformProperty);
    }

    @computed
    get deep() {
        let { parent } = this.property;
        return parent === null ? 1 : transformProperty(parent).deep + 1;
    }

    getProperties() {
        let { type } = this.property;
        if (type === "object") {
            return this.property.properties;
        } else if (type === "array") {
            return this.property.items;
        }
        return [];
    }

    _checkHasError(responses) {
        let hasError = false;
        for (let i = 0; i < responses.length; i++) {
            if (responses[i].hasError) {
                hasError = true;
                break;
            }
        }

        return {
            hasError
        };
    }

    /**
     * @returns Promise resolved with object containing "hasError" field
     */
    @action.bound
    validate() {
        let properties = this.getProperties();
        let hasProperties = !!properties.length;

        if (this.property.isRoot && !hasProperties) {
            return Promise.resolve({
                hasError: true,
                message: "Schema should have at least one property"
            });
        }

        return this.form.$.parameters.validate().then(form => {
            if (!hasProperties || form.hasError) {
                return form;
            }
            return Promise.all(
                properties.map(transformProperty).map(schemaForm => {
                    return schemaForm.validate().then(response => {
                        if (response.hasError) {
                            this.setCollapsed(false);
                            schemaForm.validate();
                        }
                        return response;
                    });
                })
            ).then(this._checkHasError);
        });
    }

    @action.bound
    addProperty(property) {
        let { type } = this.property;
        if (type === "object") {
            this.property.addProperty(property);
        } else if (type === "array") {
            return this.property.addItem(property);
        }
        this.setCollapsed(false);
    }
}

export { SchemaPropertyForm, transformProperty };
