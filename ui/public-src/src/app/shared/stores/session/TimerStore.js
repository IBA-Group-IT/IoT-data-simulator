import { action, computed, observable, autorun, untracked } from "mobx";
import BaseForm from "components/Form";

const CUSTOM_FUNCTION_DATASET_TEMPLATE = `
/**
 *
 * Please, note the following information:
 * - to print information to docker console use "print" (console.log is not available)
 * - "momentjs" and "lodash" libraries are supported and available in global scope
 *
 * @param  {object} state may be used to store information between iterations
 * @param  {string} datasetEntry - current dataset entry value if presented.
*  @param  {string} previousDatasetEntry - previous dataset entry value if presented.
 * @return {number} interval value in millisecodns
 */
function custom(state, datasetEntry, previousDatasetEntry) {
    return 5000;
}
`;

const CUSTOM_FUNCTION_NO_DATASET_TEMPLATE = `
/**
 *
 * Please, note the following information:
 * - to print information to docker console use "print" (console.log is not available)
 * - "momentjs" and "lodash" libraries are supported and available in global scope
 *
 * @param  {object} state may be used to store information between iterations
 * @return {number} interval value in millisecodns
 */
function custom(state) {
    return 5000;
}
`;

export default class TimerStore {
    dispose;

    @observable form;
    @observable.ref session;
    @observable.ref paths = [];
    @observable isAdvancedPanelOpen = false;
    

    constructor(appStore) {
        this.dispose = autorun(() => {
            if (this.session) {
                this.form = this.buildForm(this.session);
            }
        });
    }

    @action.bound
    toggleAdvancedPanel() { 
        this.isAdvancedPanelOpen = !this.isAdvancedPanelOpen;
    }

    buildForm(session) {
        let form = new BaseForm({});
        this.addFields(form, session);
        return form;
    }

    addFields(form, session) {
        let { timer, paths } = session;
        let configArr = this.timerFormConfig(session);

        configArr.forEach(config => {
            form.add("", { key: config.key });
            let field = form.$(config.key);
            this.setFieldOptions(field, config);

            untracked(() => {
                let value = timer.getParameter(config.key);
                let valueToApply = null;

                if (config.key === "timerType") {
                    return;
                }

                if (config.key === "metric") {
                    valueToApply = value || "seconds";
                } else if (config.key === "datePosition") {
                    valueToApply =
                        value ||
                        (session.datePaths.length
                            ? session.datePaths[0].path
                            : null);
                } else if (config.key === "value") {
                    valueToApply = value || 5;
                } else if (config.key === "function") {
                    valueToApply =
                        value ||
                        (session.hasDefinition &&
                        session.dataDefinition.hasDataset
                            ? CUSTOM_FUNCTION_DATASET_TEMPLATE
                            : CUSTOM_FUNCTION_NO_DATASET_TEMPLATE);
                } else if (config.key === "min") {
                    valueToApply = typeof value !== "undefined" ? value : 3;
                } else if (config.key === "max") {
                    valueToApply = typeof value !== "undefined" ? value : 5;
                } else if (config.key === "replayRate") {
                    valueToApply = value || 1;
                } else {
                    valueToApply = value;
                }

                if (
                    typeof valueToApply !== "undefined" &&
                    valueToApply !== null
                ) {
                    field.set("value", valueToApply);
                    field.$hooks.onChange && field.$hooks.onChange(field);
                }
            });
        });
    }

    @action.bound
    setSession(session) {
        this.session = session;
    }

    @computed
    get isValid() {
        return this.form.isValid;
    }

    setFieldOptions(field, { key, name, ...rest }) {
        let { isRuleMode } = this;
        Object.keys(rest).forEach(key => {
            field.set(key, rest[key]);
        });
    }

    timerFormConfig = session => {
        let timerTypesOptions = {
            interval: {
                label: "Interval",
                value: "interval"
            },
            datasetTimer: {
                label: "Dataset timer",
                value: "dataset_provided"
            },
            randomTimer: {
                label: "Random",
                value: "random"
            },
            customTimer: {
                label: "Custom function",
                value: "custom_function"
            }
        };

        let availableTypes = [
            timerTypesOptions.customTimer,
            timerTypesOptions.interval,
            timerTypesOptions.randomTimer
        ];

        if (
            session.dataDefinition &&
            session.dataDefinition.hasDataset &&
            session.datePaths.length
        ) {
            availableTypes.push(timerTypesOptions.datasetTimer);
        }

        let intervalMetricConfig = {
            key: "metric",
            name: "metric",
            label: "metric",
            placeholder: "metric",
            type: "select",
            rules: "string",
            extra: [
                { label: "milliseconds", value: "milliseconds" },
                { label: "seconds", value: "seconds" },
                { label: "minutes", value: "minutes" },
                { label: "hours", value: "hours" }
            ]
        };

        let intervalValueConfig = {
            key: "value",
            name: "value",
            label: "value",
            placeholder: "value",
            rules: "numeric|required"
        };

        let datePositionConfig = {
            key: "datePosition",
            name: "date position",
            label: "position",
            placeholder: "date position",
            rules: "string|required",
            type: "select"
        };

        let minConfig = {
            key: "min",
            name: "min",
            label: "min",
            placeholder: "min value",
            rules: "numeric|required|min:0|lower_than:max",
            type: "text"
        };

        let maxConfig = {
            key: "max",
            name: "max",
            label: "max",
            placeholder: "max value",
            rules: "numeric|required|greather_than:min",
            type: "text"
        };

        let customFunctionEditorConfig = {
            key: "function",
            name: "function",
            label: "Custom function",
            rules: "required|editor",
            type: "editor"
        };

        let rateConfig = {
            key: "replayRate",
            name: "replayRate",
            label: "Replay rate",
            rules: "required|numeric|exclusive_min:0",
            type: "text"
        };

        let typeToFieldsMap = {
            interval: [intervalValueConfig, intervalMetricConfig],
            dataset_provided: [
                {
                    ...datePositionConfig,
                    extra: session.datePaths.map(({ path, type }) => {
                        return {
                            label: path,
                            value: path
                        };
                    })
                },
                rateConfig
            ],
            random: [minConfig, maxConfig, intervalMetricConfig],
            custom_function: [customFunctionEditorConfig]
        };

        let initialValue = session.timer.type || "interval";
        let config = [
            {
                key: "timerType",
                type: "select",
                label: "type",
                bindings: "MaterialSelect",
                hooks: {
                    onChange: field => {
                        session.timer.setType(field.value);
                    }
                },
                extra: availableTypes,
                value: initialValue
            },
            {
                key: 'ticksNumber',
                name: 'ticks number',
                label: 'ticks number',
                rules: 'integer|exclusive_min:0',
                hooks: {
                    onChange: field => {
                        session.setTicksNumber(field.value)
                    }
                },
                value: untracked(() => session.ticksNumber || '')
            },
            {
                key: 'isReplayLooped',
                name: 'auto-replay',
                label: 'Auto-replay',
                hooks: {
                    onChange: field => {
                        session.setIsReplayLooped(field.value);
                    }
                },
                value: untracked(() => session.isReplayLooped)
            }
        ];

        typeToFieldsMap[initialValue]
            .map(fieldConfig => {
                return {
                    ...fieldConfig,
                    hooks: {
                        onChange: field => {
                            session.timer.setParameter(
                                fieldConfig.key,
                                field.value
                            );

                            if (field.key === "function") {
                                field.validate();
                            }
                        }
                    }
                };
            })
            .forEach(fieldConfig => {
                config.push(fieldConfig);
            });

        return config;
    };

    destroy() {
        this.dispose();
    }
}
