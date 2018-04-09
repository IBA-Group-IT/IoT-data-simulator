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

import JsonSchema from "models/schema/JsonSchema";
import BaseForm from "components/Form";
import { types } from "models/schema/JsonSchemaProperty";

import SchemaConstructorStore from "../definition/SchemaConstructorStore";
import { transformSessionProperty } from "./SchemaRuleForm";
import { FormState, FieldState } from "formstate";
import { throwsNotValidJS } from "util/validation";

const DATASET_DEVICE_TEMPLATE = `
/**
 * 
 * Please, note the following information:
 * - to print information to docker console use "print" (console.log is not available)
 * - "momentjs" and "lodash" libraries are supported and available in global scope
 * 
 * @param  {object} state may be used to store information between iterations
 * @param  {object} datasetEntry dataset entry value (readonly)
 * @param  {string} deviceName device name
 */
function process(state, datasetEntry, deviceName) { 
    return datasetEntry;
}
`;

const DATASET_TEMPLATE = `
/**
 * 
 * Please, note the following information:
 * - to print information to docker console use "print" (console.log is not available)
 * - "momentjs" and "lodash" libraries are supported and available in global scope
 * 
 * @param  {object} state may be used to store information between iterations
 * @param  {object} datasetEntry dataset entry value (readonly)
 * @param  {string} deviceName device name
 */
function process(state, datasetEntry, deviceName) {
    return datasetEntry;
}
`;

const DEVICE_TEMPLATE = `
/**
 * 
 * Please, note the following information:
 * - to print information to docker console use "print" (console.log is not available)
 * - "momentjs" and "lodash" libraries are supported and available in global scope
 * 
 * @param  {object} state may be used to store information between iterations
 * @param  {object} datasetEntry dataset entry value (readonly)
 * @param  {string} deviceName device name
 */
function process(state, datasetEntry, deviceName) { 
    return {
        timestamp: moment().valueOf()
    };
}
`;

const STATE_TEMPLATE = `
/**
 * 
 * Please, note the following information:
 * - to print information to docker console use "print" (console.log is not available)
 * - "momentjs" and "lodash" libraries are supported and available in global scope
 * 
 * @param  {object} state may be used to store information between iterations
 * @param  {object} datasetEntry dataset entry value (readonly)
 * @param  {string} deviceName device name
 */
function process(state, datasetEntry, deviceName) { 
    return {
        timestamp: moment().valueOf()
    };
}
`;

export default class InjectRulesStore extends SchemaConstructorStore {
    appStore;

    @observable.ref paths = [];

    constructor(state) {
        super(state);
    }

    @computed
    get displaySchema() {
        if (this.schema) {
            untracked(() => {
                this.schema._session = this.session;
            });
            return transformSessionProperty(this.schema);
        }
        return null;
    }

    @computed
    get functionForm() {
        return new FormState({
            function: new FieldState(
                untracked(() => (this.session.generator.jsFunction || this.processingFunction))
            )
                .validators(throwsNotValidJS)
                .onUpdate(field => {
                    this.session.generator.setJSFunction(field.value);
                })
        });
    }

    @computed
    get processingFunction() {
        let { session } = this;
        let isDatasetProvided =
            session.hasDefinition && session.dataDefinition.hasDataset;
        let isDeviceAttached = !!session.devices.length;

        if (isDatasetProvided && isDeviceAttached) {
            return DATASET_DEVICE_TEMPLATE;
        }
        if (isDatasetProvided) {
            return DATASET_TEMPLATE;
        }
        if (isDeviceAttached) {
            return DEVICE_TEMPLATE;
        }
        return STATE_TEMPLATE;
    }

    @computed
    get shouldShowSchema() {
        return this.session.hasSchema && this.session.schema;
    }

    @computed
    get session() {
        return this.state.session;
    }

    @computed
    get schema() {
        let { session } = this;
        if (session && session.hasSchema) {
            return session.schema;
        }
        return null;
    }

    @action.bound
    setPaths(paths) {
        this.paths = paths;
    }

    @action.bound
    onEditorCancel() {
        this.isEditorModalOpen = false;
        this.currentEditorProperty = null;
        this.currentEditorValue = null;
    }

    @action.bound
    onEditorChange(value) {
        this.currentEditorValue = value;
    }

    @action.bound
    onEditorSave(value) {
        try {
            eval(`"use strict"; ${this.currentEditorValue}`);
            this.currentEditorProperty.rule.setParameter(
                "function",
                this.currentEditorValue
            );
            this.closeEditorModal();
        } catch (e) {
            this.appStore.errorStore.addError({
                message: "Function is not valid"
            });
        }
    }

    @computed
    get datePaths() {
        return this.paths.filter(path => {
            return path.type === "timestamp" || path.type === "date";
        });
    }

    @computed
    get datePathsOptions() {
        return this.datePaths.map(({ path, type }) => {
            return {
                label: path,
                value: path
            };
        });
    }
}

// let additionalFieldsConfig = {
//     as_is: [],
//     literal_string: [
//         {
//             type: "text",
//             validators: [],
//             default: "",
//             key: "value",
//             name: "value"
//         }
//     ],
//     literal_boolean: [
//         {
//             key: "value",
//             name: "value",
//             label: "value",
//             placeholder: "value",
//             type: "select",
//             rules: "boolean|required",
//             extra: [
//                 { label: "true", value: true },
//                 { label: "false", value: false }
//             ]
//         }
//     ],
//     literal_integer: [
//         {
//             key: "value",
//             name: "value",
//             label: "value",
//             placeholder: "value",
//             rules: "numeric|integer|required"
//         }
//     ],
//     literal_long: [
//         {
//             key: "value",
//             name: "value",
//             label: "value",
//             placeholder: "value",
//             rules: "numeric|long|required"
//         }
//     ],
//     literal_double: [
//         {
//             key: "value",
//             name: "value",
//             label: "value",
//             placeholder: "value",
//             rules: "numeric|required"
//         }
//     ],
//     uuid: [
//         {
//             key: "prefix",
//             name: "prefix",
//             label: "prefix",
//             placeholder: "prefix",
//             rules: "string"
//         },
//         {
//             key: "postfix",
//             name: "postfix",
//             label: "postfix",
//             placeholder: "postfix",
//             rules: "string"
//         }
//     ],
//     random_integer: [
//         {
//             key: "min",
//             name: "min",
//             label: "min",
//             placeholder: "min",
//             rules: "required|numeric|integer|lower_than:max"
//         },
//         {
//             key: "max",
//             name: "max",
//             label: "max",
//             placeholder: "max",
//             rules: "required|numeric|integer|greather_than:min"
//         }
//     ],
//     random_long: [
//         {
//             key: "min",
//             name: "min",
//             label: "min",
//             placeholder: "min",
//             rules: "required|numeric|long|lower_than:max"
//         },
//         {
//             key: "max",
//             name: "max",
//             label: "max",
//             placeholder: "max",
//             rules: "required|numeric|long|greather_than:min"
//         }
//     ],
//     random_double: [
//         {
//             key: "min",
//             name: "min",
//             label: "min",
//             placeholder: "min",
//             rules: "required|numeric|lower_than:max"
//         },
//         {
//             key: "max",
//             name: "max",
//             label: "max",
//             placeholder: "max",
//             rules: "required|numeric|greather_than:min"
//         }
//     ],
//     random_boolean: [
//         {
//             key: "successProbability",
//             name: "successProbability",
//             label: "success probability",
//             placeholder: "success probability",
//             rules: "required|numeric|between:0,1"
//         }
//     ],
//     device_property: [
//         {
//             key: "propertyName",
//             name: "propertyName",
//             label: "name",
//             type: "select",
//             placeholder: "property name",
//             rules: "required",
//             extra: []
//             // extra - all devices properties
//         }
//     ],
//     current_time: [
//         {
//             key: "shift",
//             name: "shift",
//             label: "shift",
//             placeholder: "shift",
//             rules: "required|numeric"
//         },
//         {
//             key: "metric",
//             name: "metric",
//             label: "shift metric",
//             type: "select",
//             placeholder: "shift metric",
//             rules: "string",
//             extra: [
//                 { label: "milliseconds", value: "milliseconds" },
//                 { label: "seconds", value: "seconds" },
//                 { label: "minutes", value: "minutes" },
//                 { label: "hours", value: "hours" }
//             ]
//         }
//     ],
//     relative_time: [
//         {
//             key: "relativePosition",
//             name: "relativePosition",
//             label: "position",
//             placeholder: "relative position",
//             rules: "string|required",
//             type: "select"
//             // extra with schema paths (date|timestamp) from /v1/definitions/{dataDefinitionId}/schema/properties
//         },
//         {
//             key: "shift",
//             name: "shift",
//             label: "shift",
//             placeholder: "shift",
//             rules: "required|numeric"
//         },
//         {
//             key: "metric",
//             name: "metric",
//             label: "shift metric",
//             placeholder: "shift metric",
//             type: "select",
//             rules: "string",
//             extra: [
//                 { label: "milliseconds", value: "milliseconds" },
//                 { label: "seconds", value: "seconds" },
//                 { label: "minutes", value: "minutes" },
//                 { label: "hours", value: "hours" }
//             ]
//         }
//     ],
//     custom_function: [
//         {
//             key: "editorButton",
//             label: "Open editor",
//             type: "button",
//             hooks: { onClick: () => this.openEditorModal(property) }
//         },
//         {
//             key: "function",
//             label: "function",
//             name: "function",
//             type: "editor"
//         }
//     ]
// };
