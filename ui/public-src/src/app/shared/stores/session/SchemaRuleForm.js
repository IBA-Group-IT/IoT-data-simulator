import {
    observable,
    computed,
    action,
    autorun,
    reaction,
    untracked,
    runInAction,
    createTransformer,
    extendObservable
} from "mobx";

import editorTemplateBuilder from "./customFunctionBuilder";
import FormState from 'components/Form/FormState';
import FieldState from 'components/Form/FieldState';
import SelectFieldState from "components/Form/SelectFieldState";

import { types } from "models/schema/JsonSchemaProperty";
import { SchemaPropertyForm } from "../definition/SchemaPropertyForm";


import {
    throwsRequired,
    throwsNumeric,
    throwsInteger,
    throwsLong,
    throwsGreaterThan,
    throwsNothing,
    throwsGreaterOrEqual,
    throwsLowerThan,
    throwsLowerOrEqual,
    throwsMinMax,
    throwsNotValidJS
} from "util/validation";

function hasValue(options, value) {
    return options.filter(option => option.value === value)[0];
}

export const transformSessionProperty = createTransformer(property => {
    return new SchemaRuleForm(property);
});

export const transformPairToRuleForm = createTransformer(property => {
    let session = property._session;
    let isSessionHasDataset =
        session.hasDefinition && session.dataDefinition.hasDataset;

    let allRules = {
        asIs: {
            label: "As is",
            value: "as_is"
        },
        literalString: {
            label: "Literal string",
            value: "literal_string"
        },
        literalBoolean: {
            label: "Literal boolean",
            value: "literal_boolean"
        },
        literalInteger: {
            label: "Literal integer",
            value: "literal_integer"
        },
        literalDouble: {
            label: "Literal double",
            value: "literal_double"
        },
        literalLong: {
            label: "Literal long",
            value: "literal_long"
        },
        uuid: {
            label: "UUID",
            value: "uuid"
        },
        randomInteger: {
            label: "Random integer",
            value: "random_integer"
        },
        randomDouble: {
            label: "Random double",
            value: "random_double"
        },
        randomLong: {
            label: "Random long",
            value: "random_long"
        },
        randomBoolean: {
            label: "Random boolean",
            value: "random_boolean"
        },
        deviceProperty: {
            label: "Device property",
            value: "device_property"
        },
        currentTime: {
            label: "Current time",
            value: "current_time"
        },
        relativeTime: {
            label: "Relative time",
            value: "relative_time"
        },
        customFunction: {
            label: "Custom function",
            value: "custom_function"
        }
    };

    let typeToRulesMap = {
        [types.string]: [
            allRules.literalString,
            allRules.uuid,
            allRules.customFunction
        ],
        [types.boolean]: [
            allRules.literalBoolean,
            allRules.randomBoolean,
            allRules.customFunction
        ],
        [types.integer]: [
            allRules.literalInteger,
            allRules.randomInteger,
            allRules.customFunction
        ],
        [types.double]: [
            allRules.literalDouble,
            allRules.randomDouble,
            allRules.customFunction
        ],
        [types.long]: [
            allRules.literalLong,
            allRules.randomLong,
            allRules.customFunction
        ],
        [types.date]: [allRules.currentTime, allRules.customFunction],
        [types.timestamp]: [allRules.currentTime, allRules.customFunction]
    };

    let ruleTypeToFieldConfigMap = {
        [allRules.asIs.value]: {},

        [allRules.literalString.value]: {
            value: {
                type: "text",
                validators: [throwsRequired],
                default: ""
            }
        },

        [allRules.literalBoolean.value]: {
            value: {
                type: "select",
                validators: [throwsRequired],
                options: [
                    { label: "true", value: true },
                    { label: "false", value: false }
                ],
                default: true
            }
        },

        [allRules.literalInteger.value]: {
            value: {
                type: "text",
                validators: [throwsRequired, throwsNumeric, throwsInteger],
                default: ""
            }
        },

        [allRules.literalLong.value]: {
            value: {
                type: "text",
                validators: [throwsRequired, throwsNumeric, throwsLong],
                default: ""
            }
        },

        [allRules.literalDouble.value]: {
            value: {
                type: "text",
                validators: [throwsRequired, throwsNumeric],
                default: ""
            }
        },

        [allRules.uuid.value]: {
            prefix: {
                type: "text",
                validators: [],
                default: ""
            },

            postfix: {
                type: "text",
                validators: [],
                default: ""
            }
        },

        [allRules.randomInteger.value]: {
            min: {
                type: "text",
                validators: [throwsRequired, throwsNumeric],
                default: ""
            },
            max: {
                type: "text",
                validators: [throwsRequired, throwsNumeric],
                default: ""
            }
        },

        [allRules.randomLong.value]: {
            min: {
                type: "text",
                validators: [throwsRequired, throwsNumeric, throwsLong],
                default: ""
            },
            max: {
                type: "text",
                validators: [throwsRequired, throwsNumeric, throwsLong],
                default: ""
            }
        },

        [allRules.randomDouble.value]: {
            min: {
                type: "text",
                validators: [throwsRequired, throwsNumeric],
                default: 0
            },
            max: {
                type: "text",
                validators: [throwsRequired, throwsNumeric],
                default: 100
            }
        },

        [allRules.randomBoolean.value]: {
            successProbability: {
                type: "text",
                validators: [
                    throwsRequired,
                    throwsNumeric,
                    throwsGreaterThan(0),
                    throwsLowerThan(1)
                ],
                default: ""
            }
        },

        [allRules.deviceProperty.value]: {
            propertyName: {
                type: "select"
            }
        },

        [allRules.currentTime.value]: {
            shift: {
                type: "text",
                validators: [throwsRequired, throwsNumeric],
                default: 0
            },
            metric: {
                type: "select",
                validators: [],
                options: [
                    { label: "milliseconds", value: "milliseconds" },
                    { label: "seconds", value: "seconds" },
                    { label: "minutes", value: "minutes" },
                    { label: "hours", value: "hours" }
                ],
                default: "milliseconds"
            }
        },

        [allRules.relativeTime.value]: {
            relativePosition: {
                type: "select",
                validators: [throwsRequired],
                default: null
            },
            shift: {
                type: "text",
                validators: [throwsRequired, throwsNumeric],
                default: 0
            },
            metric: {
                type: "select",
                validators: [],
                options: [
                    { label: "milliseconds", value: "milliseconds" },
                    { label: "seconds", value: "seconds" },
                    { label: "minutes", value: "minutes" },
                    { label: "hours", value: "hours" }
                ],
                default: "milliseconds"
            }
        },

        [allRules.customFunction.value]: {
            function: {
                type: "text",
                validators: [],
                default: editorTemplateBuilder.build(session, property)
            },
            editorFunction: {
                type: "text",
                validators: [throwsNotValidJS],
                default: untracked(() => {
                    return property.rule.getParameter("function") || editorTemplateBuilder.build(session, property)
                }),
                onUpdate: field => {}
            },
            dependsOn: {
                type: "select",
                validators: [],
                options: [
                    {
                        label: "",
                        value: ""
                    }
                ].concat(
                    session.paths.map(({ path, type }) => {
                        return {
                            label: path,
                            value: path
                        };
                    })
                )
            }
        }
    };

    const minMaxValidator = throwsMinMax("min", "max");

    let ruleTypeToValidatorsMap = {
        [allRules.randomInteger.value]: minMaxValidator,
        [allRules.randomDouble.value]: minMaxValidator,
        [allRules.randomLong.value]: minMaxValidator
    };

    //////////////////////////////////////////////////////////////////////////////

    let availableTypes = typeToRulesMap[property.type] || [];
    let isDateProperty =
        property.type === "timestamp" || property.type === "date";

    let isSessionWithDataset =
        session.hasDefinition && session.dataDefinition.hasDataset;
    let isSessionWithDevices = !!session.devices.length;

    let isArray = property.type === "array";
    let isObject = property.type === "object";
    let isEmptyObject = isObject && property.properties.length === 0;
    let isEmptyArray = isArray && property.items.length === 0;

    // Check and add as is rule
    if (
        isSessionWithDataset && 
        (
            (!isArray && !isObject) ||
            (isArray && isEmptyArray) ||
            (isObject && isEmptyObject)
        )
    ) {
        availableTypes.push(allRules.asIs);
    }

    // Check if empty object or array and add custom function rule
    if (isEmptyObject || isEmptyArray) {
        availableTypes.push(allRules.customFunction);
    }

    // Check and add deviceProperty rule
    if (isSessionWithDevices) {
        let devicePropertyExtra = [];
        
        session.devices.forEach(device => {

            device.properties.forEach(property => {

                let { name, value } = property.toJSON();

                let numOfDevicesWithProp = session.devices.filter(device => {
                    return device.properties.find(prop => prop.name === name);
                }).length;
                let isPropAlreadyAdded = devicePropertyExtra.find(prop => prop.value === name);

                if(numOfDevicesWithProp === session.devices.length && !isPropAlreadyAdded) { 
                    devicePropertyExtra.push({ label: name, value: name });
                }
            });
        });

        let devicesWithProps = session.devices.filter(device => {
            return device.properties.length;
        });

        if (devicesWithProps.length && devicePropertyExtra.length) {
            let devicePropertyAdditionalFieldsMap =
                ruleTypeToFieldConfigMap[allRules.deviceProperty.value];
            let propertyNameConfig =
                devicePropertyAdditionalFieldsMap.propertyName;
            propertyNameConfig.options = devicePropertyExtra;
            availableTypes.push(allRules.deviceProperty);
        }
    }

    // Check and add relative time options
    if (isDateProperty && isSessionHasDataset) {
        let relativeTimeAdditionalFieldsMap =
            ruleTypeToFieldConfigMap[allRules.relativeTime.value];
        let positionConfig = relativeTimeAdditionalFieldsMap.relativePosition;

        positionConfig.options = session.paths
            ? session.paths
                  .filter(path => {
                      return path.type === "timestamp" || path.type === "date";
                  })
                  .map(({ path }) => {
                      return {
                          label: path,
                          value: path
                      };
                  })
            : [];

        if (positionConfig.options.length) {
            availableTypes.push(allRules.relativeTime);
        }
    }

    let defaultType =
        (hasValue(availableTypes, property.rule.type) && property.rule.type) ||
        (availableTypes[0] ? availableTypes[0].value : null);

    let availableFields = ruleTypeToFieldConfigMap[defaultType];

    let config = {
        ruleType: {
            type: "select",
            options: availableTypes.sort((a, b) =>
                a.label.localeCompare(b.label)
            ),
            default: defaultType,
            validators: [],
            onUpdate: field => {
                property.rule.setType(field.value);
            }
        },

        ...availableFields
    };

    let formConfig = Object.keys(config).reduce((obj, key) => {
        let fieldConfig = config[key];
        let rawField =
            fieldConfig.type === "text"
                ? new FieldState(fieldConfig.default)
                : new SelectFieldState(
                      fieldConfig.default,
                      fieldConfig.options
                  );

        let updateRuleParameter = field => {
            property.rule.setParameter(key, field.value);
        };

        let fieldWithUpdate = rawField.onUpdate(
            fieldConfig.onUpdate || updateRuleParameter
        );

        obj[key] = fieldWithUpdate.validators.apply(
            fieldWithUpdate,
            fieldConfig.validators
        );

        // Set values, untracked since we don't
        // want form to be updated at each keypress
        if (key !== "ruleType") {
            untracked(() => {
                let value = property.rule.getParameter(key);
                if (
                    typeof value !== "undefined" &&
                    value !== null &&
                    value !== ""
                ) {
                    rawField.reinitValue(value);
                } else {
                    rawField.reinitValue(fieldConfig.default);
                }
            });
        }

        return obj;
    }, {});

    let form = new FormState(formConfig);
    if (ruleTypeToValidatorsMap[defaultType]) {
        form.enableAutoValidation();
        return form
            .assumeAllFieldsValid()
            .compose()
            .validators(ruleTypeToValidatorsMap[defaultType]);
    }
    return form;
});

export class SchemaRuleForm extends SchemaPropertyForm {
    constructor(property) {
        let session = property._session;
        super(property);
        this.session = session;
        extendObservable(this, {
            get ruleForm() {
                return transformPairToRuleForm(property);
            }
        });
    }

    @computed
    get deep() {
        let { parent } = this.property;
        return parent === null ? 1 : transformSessionProperty(parent).deep + 1;
    }

    @computed
    get children() {
        if (this.isCollapsed) {
            return [];
        }
        return this.getProperties().map(property => {
            untracked(() => {
                property._session = this.session;
            });
            return transformSessionProperty(property);
        });
    }

    /**
     * @returns Promise resolved with object containing "hasError" field
     */
    @action.bound
    validate() {
        return this.ruleForm.validate().then(form => {
            if (!this.getProperties().length || form.hasError) {
                return form;
            }

            return Promise.all(
                this.getProperties()
                    .map((prop) => {
                        prop._session = this.session;
                        return prop;
                    })
                    .map(transformSessionProperty)
                    .map(schemaForm => {
                        return schemaForm.validate().then((response) => {
                            if(response.hasError) { 
                                this.setCollapsed(false);
                                schemaForm.validate();
                            }
                            return response;
                        });
                    })
            ).then(this._checkHasError);
        });
    }
}

export default {
    SchemaRuleForm,
    transformSessionProperty
};
