import { observable, action, computed, autorun, reaction } from "mobx";

import WizardStore from "../WizardStore";
import DefinitionEntry from "models/DefinitionEntry";
import TimerStore from "./TimerStore";
import DevicesPopulationStore from "./DevicesPopulationStore";
import FinalizeFormStore from "./FinalizeFormStore";
import InjectRulesStore from "./InjectRulesStore";
import DatasetFilterStore from "./DatasetFilterStore";

export default class UpdateSessionStore {
    @observable currentStepId;
    @observable.ref session;
    @observable.ref paths = [];

    steps = {
        definition: "select_definition",
        timer: "select_timer",
        devices: "select_devices",
        injector: "select_injector",
        rules: "payload_rules",
        system: "select_system",
        finalize: "finalize"
    };

    constructor(appStore, sessionManagementStore, watchField) {
        this.appStore = appStore;
        this.sessionManagementStore = sessionManagementStore;

        this.wizardStore = new WizardStore(appStore, this);
        this.timerStore = new TimerStore(appStore, this);
        this.devicesPopulationStore = new DevicesPopulationStore(appStore);
        this.finalizeFormStore = new FinalizeFormStore(appStore);
        this.injectRulesStore = new InjectRulesStore(this);
        this.datasetFilterStore = new DatasetFilterStore(this);

        this.currentStepId = this.steps.definition;

        this.definitionsStore = appStore.definitionsStore;
        this.devicesStore = appStore.devicesStore;
        this.systemsStore = appStore.systemsStore;
        this.sessionsStore = appStore.sessionsStore;

        autorun(() => {
            let session = this.sessionManagementStore[watchField];
            this.session = session;
            this.proceedToDefinitions();
            this.wizardStore.setStep(0);
        });

        reaction(
            () => {
                return this.definitionsStore.updatedItemId;
            },
            id => {
                let definitionId = this.selectedDefinitionId;
                if (this.definitionsStore.items && definitionId === id) {
                    let instance = this.definitionsStore.findInstanceById(
                        definitionId
                    );

                    if (this.session.id) {
                        this.sessionsStore
                            .getById(this.session.id)
                            .then(session => {
                                if (session.hasDefinition) {
                                    this.session.setDefinition(session.dataDefinition);
                                }
                                if(session.hasSchema) { 
                                    this.session.generator.setSchema(session.generator.schema);
                                }
                            });
                    } else {
                        this.setDefinition(instance, true);
                    }
                }
            }
        );

        reaction(
            () => {
                return this.definitionsStore.items;
            },
            items => {
                if (items && this.selectedDefinitionId) {
                    let instance = this.definitionsStore.findInstanceById(
                        this.selectedDefinitionId
                    );
                    if (!instance) {
                        this.setDefinition(null);
                    }
                }
            }
        );

        reaction(
            () => {
                return this.devicesStore.items;
            },
            () => {
                if (this.devicesStore.items) {
                    this.session.devices.forEach(device => {
                        let instance = this.devicesStore.findInstanceById(
                            device.id
                        );
                        if (instance) {
                            this.session.removeDeviceById(device.id);
                            this.session.addDevice(instance);
                        } else {
                            this.session.removeDeviceById(device.id);
                        }
                    });
                }
            }
        );

        reaction(
            () => {
                return this.systemsStore.items;
            },
            () => {
                let targetSystemId = this.session.targetSystem.id;
                if (this.systemsStore.items) {
                    let instance = this.systemsStore.findInstanceById(
                        targetSystemId
                    );
                    if (targetSystemId) {
                        if (instance) {
                            this.session.setSystem(instance);
                        } else {
                            this.session.setSystem({});
                        }
                    }
                }
            }
        );

        reaction(
            () => {
                return (
                    this.session &&
                    this.session.hasDefinition &&
                    this.session.hasSchema
                );
            },
            isMatching => {
                if (isMatching) {
                    this.appStore.definitionsStore
                        .getSchemaProperties(this.session.dataDefinition.id)
                        .then(paths => {
                            this.session.setPaths(paths);

                            // TODO USE SESSION PROPERTY
                            // OR REACTIVE STATE INSTEAD
                            this.paths = paths;
                            this.injectRulesStore.setPaths(paths);
                            this.devicesPopulationStore.setPaths(paths);
                        });
                }
            }
        );

        reaction(
            () => {
                return this.session.devices;
            },
            devices => {
                if (!devices.length) {
                    this.session.deviceInjector.setRule(null);
                }
            }
        );
    }

    @computed
    get schema() {
        if (this.session && this.session.hasSchema) {
            return this.session.schema;
        }
        return null;
    }

    @computed
    get stepsCount() {
        return this.session.devices.length ? 7 : 6;
    }

    @computed
    get step() {
        return this.wizardStore.step;
    }

    @computed
    get isNextActive() {
        if (this.currentStepId === this.steps.definition) {
            return true;
        }
        if (this.currentStepId === this.steps.timer) {
            return this.timerStore.isValid;
        }
        if (this.currentStepId === this.steps.devices) {
            return true;
        }
        if (this.currentStepId === this.steps.injector) {
            return this.devicesPopulationStore.form.isValid;
        }
        if (this.currentStepId === this.steps.rules) {
            //return this.injectRulesStore.form.isValid;
            return true;
        }
        if (this.currentStepId === this.steps.system) {
            return this.appStore.systemsStore.findInstanceById(
                this.session.targetSystem.id
            );
        }
        if (this.currentStepId === this.steps.finalize) {
            return this.session.name;
        }
    }

    @computed
    get isBackActive() {
        let { step } = this;
        if (step === 0) {
            return false;
        }
        return true;
    }

    @computed
    get nextHandler() {
        let { view } = this.appStore;

        if (this.currentStepId === this.steps.definition) {
            return () => {
                this.proceedToTimer();
                this.wizardStore.handleNextStep();
            };
        }
        if (this.currentStepId === this.steps.timer) {
            return () => {
                this.timerStore.form
                    .validate({ showErrors: true })
                    .then(form => {
                        if (form.isValid) {
                            return Promise.resolve();
                        }
                        return Promise.reject();
                    })
                    .then(() => {
                        return this.datasetFilterStore.validate();
                    })
                    .then(({ hasError }) => {
                        if (!hasError) {
                            this.proceedToDevices();
                            this.wizardStore.handleNextStep();
                        }
                    });
            };
        }
        if (this.currentStepId === this.steps.devices) {
            if (this.session.devices.length) {
                return () => {
                    this.proceedToInjector();
                    this.wizardStore.handleNextStep();
                };
            }
            return () => {
                this.proceedToRules();
                this.wizardStore.handleNextStep();
            };
        }
        if (this.currentStepId === this.steps.injector) {
            return () => {
                this.proceedToRules();
                this.wizardStore.handleNextStep();
            };
        }
        if (this.currentStepId === this.steps.rules) {
            if (this.injectRulesStore.shouldShowSchema) {
                return () => {
                    this.injectRulesStore.displaySchema
                        .validate()
                        .then(({ hasError, message }) => {
                            if (!hasError) {
                                this.proceedToSystem();
                                this.wizardStore.handleNextStep();
                            } else if (message) {
                                this.appStore.errorStore.addError({ message });
                            }
                        });
                };
            }

            return () => {
                this.injectRulesStore.functionForm
                    .validate()
                    .then(({ hasError }) => {
                        if (!hasError) {
                            this.proceedToSystem();
                            this.wizardStore.handleNextStep();
                        }
                    });
            };
        }
        if (this.currentStepId === this.steps.system) {
            return () => {
                this.proceedToFinalize();
                this.wizardStore.handleNextStep();
            };
        }
        if (this.currentStepId === this.steps.finalize) {
            return () => {
                let { id, ...params } = this.session.data;
                console.log(this.session.data);
                this.sessionsStore.update(id, params).then(() => {
                    this.sessionManagementStore.reset();
                    this.sessionsStore.getSessionsStatus();
                });
            };
        }
        return function() {};
    }

    @computed
    get nextLabel() {
        if (
            (this.currentStepId === this.steps.definition) &
            !this.session.hasDefinition
        ) {
            return "Skip";
        }
        if (this.currentStepId === this.steps.finalize) {
            if (this.session.id) {
                return "Update session";
            }
            return "Create session";
        }
        if (
            this.currentStepId === this.steps.devices &&
            !this.session.hasDevices
        ) {
            return "Skip";
        }
        return "Proceed";
    }

    @computed
    get backHandler() {
        if (this.currentStepId === this.steps.definition) {
            return () => {};
        }
        if (this.currentStepId === this.steps.timer) {
            return () => {
                this.proceedToDefinitions();
                this.wizardStore.handlePreviousStep();
            };
        }
        if (this.currentStepId === this.steps.devices) {
            return () => {
                this.proceedToTimer();
                this.wizardStore.handlePreviousStep();
            };
        }
        if (this.currentStepId === this.steps.injector) {
            return () => {
                this.proceedToDevices();
                this.wizardStore.handlePreviousStep();
            };
        }
        if (this.currentStepId === this.steps.rules) {
            if (this.session.devices.length) {
                return () => {
                    this.proceedToInjector();
                    this.wizardStore.handlePreviousStep();
                };
            }
            return () => {
                this.proceedToDevices();
                this.wizardStore.handlePreviousStep();
            };
        }
        if (this.currentStepId === this.steps.system) {
            return () => {
                this.proceedToRules();
                this.wizardStore.handlePreviousStep();
            };
        }
        if (this.currentStepId === this.steps.finalize) {
            return () => {
                this.proceedToSystem();
                this.wizardStore.handlePreviousStep();
            };
        }
        return function() {};
    }

    @action.bound
    setDefinition(definition, selected) {
        let { appStore } = this;

        if (!selected) {
            this.session.setDefinition(new DefinitionEntry());
        } else {
            this.session.setDefinition(definition);

            if (definition.schema) {
                appStore.definitionsStore
                    .getSchemaRules(definition.id)
                    .then(schema => {
                        this.session.generator.setSchema(schema);
                    });
            }
        }
    }

    @action.bound
    setDevice(device, selected) {
        if (!selected) {
            this.session.removeDeviceById(device.id);
        } else {
            this.session.addDevice(device);
        }
    }

    @computed
    get selectedDefinitionId() {
        let { session } = this;
        return session.hasDefinition && session.dataDefinition.id;
    }

    @computed
    get selectedDevicesIds() {
        return this.session.devices.map(({ id }) => id);
    }

    @action.bound
    proceedToDefinitions() {
        this.definitionsStore.getAll();
        this.currentStepId = this.steps.definition;
    }

    @action.bound
    proceedToTimer() {
        this.timerStore.setSession(this.session);
        this.currentStepId = this.steps.timer;
    }

    @action.bound
    proceedToDevices() {
        this.devicesStore.getAll();
        this.currentStepId = this.steps.devices;
    }

    @action.bound
    proceedToInjector() {
        this.devicesPopulationStore.setSession(this.session);
        this.currentStepId = this.steps.injector;
    }

    @action.bound
    proceedToRules() {
        //this.injectRulesStore.setSession(this.session);
        this.currentStepId = this.steps.rules;
    }

    @action.bound
    proceedToSystem() {
        this.systemsStore.getAll();
        this.currentStepId = this.steps.system;
    }

    @action.bound
    proceedToFinalize() {
        this.finalizeFormStore.setSession(this.session);
        this.currentStepId = this.steps.finalize;
    }
}
