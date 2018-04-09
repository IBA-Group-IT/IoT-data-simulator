import {
    observable,
    computed,
    action,
    autorun,
    reaction,
    untracked,
    extendObservable
} from "mobx";

import { FormState, FieldState } from "formstate";
import EditorFieldState from "components/Form/EditorFieldState";
import SelectFieldState from "components/Form/SelectFieldState";

import { throwsNotValidJS, throwsRequired } from 'util/validation';

const FUNCTION_TEMPLATE = `
/**
 * 
 * Please, note the following information:
 * - to print information to docker console use "print" (console.log is not available)
 * - "momentjs" and "lodash" libraries are supported and available in global scope
 * 
 * @param  {object} state may be used to store information between iterations
 * @param  {object} datasetEntry dataset entry value (readonly)
 */
function filter(state, datasetEntry) { 
    return true;
}
`

export default class DatasetFilterStore {
    constructor(state) {
        this.state = state;
    }

    @computed
    get session() {
        return this.state.session;
    }

    @computed
    get paths() {
        return this.state.paths;
    }

    getTypeOptions(session) {
        let isSessionWithSchema = session.hasSchema;

        let filterTypeOptions = [
            { label: "", value: "" },
            { label: "Custom function", value: "custom_function" },
            { label: "Dataset entry position", value: "dataset_entry_position" }
        ];

        let isNotDatasetRelatedOptions = ({ value }) => {
            return value !== "dataset_entry_position";
        };

        if (isSessionWithSchema) {
            return filterTypeOptions;
        }
        return filterTypeOptions.filter(isNotDatasetRelatedOptions);
    }

    @computed
    get isCustomFunctionSelected() {
        return this.form.$.type.$ === "custom_function";
    }

    @computed
    get isDatasetEntrySelected() {
        return this.form.$.type.$ === "dataset_entry_position";
    }

    @computed
    get shouldShowDatasetFilter() { 
        return this.session.hasDefinition && this.session.dataDefinition.hasDataset
    }

    @computed
    get form() {
        let { session, paths } = this;
        let { datasetFilter } = session;

        let type = datasetFilter.type;
        let filterTypeOptions = this.getTypeOptions(session);

        let typeToFieldsMap = {
            custom_function: {
                jsFunction: {
                    type: "text",
                    validators: [throwsNotValidJS],
                    initial: FUNCTION_TEMPLATE
                }
            },
            dataset_entry_position: {

                position: {
                    type: "select",
                    options: paths.map(({ path }) => {
                        return {
                            label: path,
                            value: path
                        };
                    }),
                    validators: [throwsRequired]
                },

                value: {
                    type: "text",
                    validators: []
                }
            }
        };

        let fieldsConfig = {
            type: {
                type: "select",
                initial: datasetFilter.type,
                options: filterTypeOptions,
                onUpdate: field => {
                    datasetFilter.setType(field.value);
                }
            },
            ...typeToFieldsMap[type]
        };

        let formOptions = Object.keys(fieldsConfig).reduce((obj, key) => {
            let fieldConfig = fieldsConfig[key];

            let initialValue =
                untracked(() => datasetFilter.getParameter(key)) ||
                fieldConfig.initial
            let updateFn = ({ value }) => {
                return datasetFilter.setParameter(key, value);
            }
            let { type } = fieldConfig;

            let fieldState = null;

            if (type === "select") {
                fieldState = new SelectFieldState(initialValue, fieldConfig.options);
            } else {
                fieldState = new FieldState(initialValue);
            }

            let fieldWithHooks = fieldState.onUpdate(
                fieldConfig.onUpdate || updateFn
            );
            
            let field = fieldWithHooks.validators.apply(
                fieldWithHooks,
                fieldConfig.validators
            );

            if(key !== 'type') { 
                field.reinitValue(initialValue);
            }

            obj[key] = field

            return obj;
        }, {});

        return new FormState(formOptions);
    }


    @action.bound
    validate() {
        return this.form.validate();
    }
}
