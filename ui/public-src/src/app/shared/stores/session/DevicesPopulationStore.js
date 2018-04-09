import { action, computed, observable, autorun, untracked } from "mobx";
import BaseForm from "components/Form";

export default class DevicesPopulationStore {
    @observable.ref session;
    @observable.ref paths = [];
    @observable.ref form;

    constructor(appStore) {
        autorun(() => {
            if (this.session) {
                this.form = this.buildForm(this.session);
            }
        });
    }

    buildForm(session) {
        let form = new BaseForm({});
        this.addFields(form, session);
        return form;
    }

    addFields(form, session) {
        let { deviceInjector } = session;
        let configArr = this.injectorFormConfig(session);

        configArr.forEach(config => {
            form.add("", { key: config.key });
            let field = form.$(config.key);
            this.setFieldOptions(field, config);

            untracked(() => {
                let value = deviceInjector.getParameter(config.key);
                let valueToApply = value;

                if (config.key === "ruleType") {
                    return;
                }

                if (config.key === "datasetPosition") {
                    let isValueIncluded = this.pathsOptions.filter(
                        option => option.value === value
                    ).length;
                    if (value && isValueIncluded) {
                        valueToApply = value;
                    } else if (this.pathsOptions.length) {
                        valueToApply = this.pathsOptions[0].value;
                    }
                } else if (config.key === "deviceProperty") {
                    let isValueIncluded = this.devicePropsOptions.filter(
                        option => option.value === value
                    ).length;
                    if (value && isValueIncluded) {
                        valueToApply = value;
                    } else if (this.pathsOptions.length) {
                        valueToApply = this.devicePropsOptions.length
                            ? this.devicePropsOptions[0].value
                            : null;
                    }
                } else if (config.key === "delay") {
                    valueToApply = value || 0;
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

    @action.bound
    setPaths(paths) {
        this.paths = paths;
    }

    @computed
    get pathsOptions() {
        return this.paths.map(({ path, type }) => {
            return {
                label: path,
                value: path
            };
        });
    }

    @computed
    get deviceProperties() {
        let { session } = this;
        let properties = [];

        session.devices.forEach(device => {
            device.properties.forEach(property => {
                properties.push(property.toJSON());
            });
        });
        return properties;
    }

    @computed
    get devicePropsOptions() {
        return this.deviceProperties.map(({ name, value }) => {
            return {
                label: name,
                value: name
            };
        });
    }

    setFieldOptions(field, { key, name, ...rest }) {
        let { isRuleMode } = this;
        Object.keys(rest).forEach(key => {
            field.set(key, rest[key]);
        });
    }

    injectorFormConfig = session => {
        let injectorTypesOptions = {
            all: {
                label: "All",
                value: "all"
            },
            specific: {
                label: "Specific",
                value: "specific"
            },
            random: {
                label: "Random",
                value: "random"
            },
            roundRobin: {
                label: "Round robin",
                value: "round_robin"
            }
        };

        let availableTypes = [
            injectorTypesOptions.all,
            injectorTypesOptions.random,
            injectorTypesOptions.roundRobin
        ];
        if (session.dataDefinition && session.dataDefinition.hasDataset) {
            availableTypes.push(injectorTypesOptions.specific);
        }

        let allDelayConfig = {
            key: "delay",
            name: "delay",
            label: "delay (milliseconds)",
            placeholder: "delay (milliseconds)",
            rules: "numeric|min:0"
        };

        let datasetPositionConfig = {
            key: "datasetPosition",
            name: "datasetPosition",
            type: "select",
            label: "position",
            placeholder: "dataset position",
            rules: "string|required"
            //extra
        };

        let devicePropertyConfig = {
            key: "deviceProperty",
            type: "select",
            name: "deviceProperty",
            label: "device property",
            placeholder: "device property",
            rules: "string|required"
        };

        let ruleToFieldsMap = {
            all: [allDelayConfig],
            random: [],
            round_robin: [],
            specific: [
                {
                    ...datasetPositionConfig,
                    extra: this.pathsOptions
                },
                {
                    ...devicePropertyConfig,
                    extra: this.devicePropsOptions
                }
            ]
        };

        let initialValue = session.deviceInjector.rule;
        if (!initialValue && session.devices.length) {
            initialValue = "round_robin";
            session.deviceInjector.setRule(initialValue);
        }

        let config = [
            {
                key: "ruleType",
                type: "select",
                label: "type",
                bindings: "MaterialSelect",
                hooks: {
                    onChange: field => {
                        session.deviceInjector.setRule(field.value);
                    }
                },
                extra: availableTypes,
                value: initialValue
            }
        ];

        if (initialValue) {
            ruleToFieldsMap[initialValue]
                .map(fieldConfig => {
                    return {
                        ...fieldConfig,
                        hooks: {
                            onChange: field => {
                                session.deviceInjector.setParameter(
                                    fieldConfig.key,
                                    field.value
                                );
                            }
                        }
                    };
                })
                .forEach(fieldConfig => {
                    config.push(fieldConfig);
                });
        }

        return config;
    };
}
