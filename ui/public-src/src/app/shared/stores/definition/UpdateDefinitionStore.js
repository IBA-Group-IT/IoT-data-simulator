import {
    observable,
    computed,
    action,
    extendObservable,
    toJS,
    autorun,
    reaction,
    createTransformer,
    observe
} from "mobx";

import JsonSchema from "models/schema/JsonSchema";
import DatasetEntry from "models/DatasetEntry";
import fileUtils from "util/file";
import { throttle } from "util/function";

import { DatasetSelectionMethods } from "constants/definitionWizard";

import SchemaConstructorStore from "./SchemaConstructorStore";
import WizardStore from "../WizardStore";
import FinalizeFormStore from "./FinalizeFormStore";
import BucketFormStore from "./BucketFormStore";

export default class UpdateDefinitionStore {
    appStore;
    @observable.ref selectionMethod = DatasetSelectionMethods.existing;
    @observable.ref fileToUpload = null;
    @observable isDatasetUploading = false;
    @observable uploadingProgress = 0;
    @observable state;

    @observable isSelectSchemaModalOpen = false;
    @observable selectedDefinitionToCopy = null;
    @observable selectedDatasetId = null;

    @observable.ref schema;

    constructor({ root, state, onComplete }) {
        this.appStore = root;
        this.state = state;
        this.onComplete = onComplete;

        this.wizardStore = new WizardStore(this);
        this.bucketFormStore = new BucketFormStore(this);
        this.schemaConstructorStore = new SchemaConstructorStore(this);
        this.finalizeFormStore = new FinalizeFormStore(this);
    }

    initReactions() {
        this.disposeDatasetReaction = reaction(
            //React when definition has changed
            () => {
                return this.definition;
            },
            () => {
                this.reset();

                let datasetId = this.definition.dataset.id;
                this.selectedDatasetId = datasetId;
            },
            // Fire immediately
            true
        );
    }

    disposeReactions() {
        this.disposeDatasetReaction();
    }

    @action.bound
    onDatasetSelection(selectedDataset) {
        if (!selectedDataset || selectedDataset.id === this.selectedDatasetId) {
            this.selectedDatasetId = null;
            this.updateDefinitionSchema(null);
            return;
        }
        this.selectedDatasetId = selectedDataset.id;
        this.updateDefinitionSchema(selectedDataset.id);
    }

    @action.bound
    updateDefinitionSchema(datasetId) {
        if (!datasetId) {
            // Create flow
            if (!this.definition.id) {
                this.schema = new JsonSchema();
            }
            return;
        }

        this.appStore.datasetsStore.getSchemaById(datasetId).then(schema => {
            this.schema = new JsonSchema(schema);
        });
    }

    @action.bound
    setSelectionMethod(value) {
        this.selectionMethod = value;
    }

    @action.bound
    setFileToUpload(file) {
        this.fileToUpload = file;
    }

    @action.bound
    reset() {
        this.schema = new JsonSchema(
            this.definition.schema && this.definition.schema.toJSON()
        );

        this.setFileToUpload(null);
        this.setSelectionMethod(DatasetSelectionMethods.existing);

        if (this.isDatasetUploading) {
            if (this.cancelToken) {
                this.cancelToken();
                this.cancelToken = null;
            }
            this.setIsDatasetUploading(false);
        }
        this.wizardStore.reset();
    }

    @computed
    get isSchemaSkipAllowed() {
        return this.wizardCurrentStep === 1 && this.selectedDatasetId;
    }

    @computed
    get definition() {
        return this.state.definition;
    }

    @computed
    get selectedDataset() {
        if (this.selectedDatasetId) {
            return this.datasets.filter(
                d => d.id === this.selectedDatasetId
            )[0];
        }
        return null;
    }

    @computed
    get datasets() {
        let { datasets = [] } = this.appStore.datasetsStore;
        return datasets;
    }

    @computed
    get isDatasetSelected() {
        return this.definition && this.definition.hasDataset;
    }

    @computed
    get isBucketOptionsSet() {
        return (
            this.selectionMethod === DatasetSelectionMethods.bucket &&
            definition.dataset &&
            definition.dataset.bucketOptions
        );
    }

    @computed
    get isFileSelected() {
        return !!this.fileToUpload;
    }

    @computed
    get wizardCurrentStep() {
        return this.wizardStore.step;
    }

    @computed
    get finalizeForm() {
        return this.finalizeFormStore.form;
    }

    @action.bound
    setIsDatasetUploading(isDatasetUploading) {
        this.isDatasetUploading = isDatasetUploading;
        if (!isDatasetUploading) {
            this.setUploadingProgress(0);
        }
    }

    @action.bound
    setUploadingProgress(progress) {
        let percentage = 0;
        if (progress.total && progress.loaded) {
            percentage = Math.round(progress.loaded * 100 / progress.total);
        } else {
            percentage = progress;
        }
        this.uploadingProgress = percentage;
    }

    @action.bound
    deleteDataset(dataset) {
        this.appStore.view.openDeleteDataset(dataset);
    }

    setCancelToken(token) {
        this.cancelToken = token;
    }

    @action.bound
    openSchemaSelectModal() {
        this.isSelectSchemaModalOpen = true;
    }

    @action.bound
    closeSchemaSelectModal() {
        this.isSelectSchemaModalOpen = false;
    }

    @action.bound
    selectDefinitionToCopySchema(definition) {
        this.selectedDefinitionToCopy = definition;
    }

    @action.bound
    submitDefinitionToCopySchema() {
        if (this.selectedDefinitionToCopy) {
            this.appStore.definitionsStore
                .getById(this.selectedDefinitionToCopy.id)
                .then(definition => {
                    this.schema = new JsonSchema(definition.schema.toJSON());
                    this.selectedDefinitionToCopy = null;
                    this.closeSchemaSelectModal();
                });
        }
    }

    @computed
    get nextHandler() {
        let { selectionMethod, wizardStore } = this;
        let { step } = wizardStore;

        if (step === 0) {
            return this.onDatasetSubmit;
        } else if (step === 1) {
            return this.onSchemaApply;
        } else if (step === 2) {
            return this.onDefinitionUpdate;
        }
    }

    @computed
    get backHandler() {
        return this.wizardStore.handlePreviousStep;
    }

    @computed
    get nextLabel() {
        let store = this;

        let { selectionMethod, definition, wizardStore } = store;

        let { step } = wizardStore;

        if (step === 0) {
            if (selectionMethod === DatasetSelectionMethods.existing) {
                if (this.selectedDataset) {
                    return "Proceed";
                }
                return "Skip";
            }

            if (selectionMethod === DatasetSelectionMethods.bucket) {
                return "Apply bucket options";
            }

            if (selectionMethod === DatasetSelectionMethods.upload) {
                return "Upload and proceed";
            }

            return "Next";
        }

        if (step === 1) {
            return "Apply schema";
        }

        if (step === 2) {
            if (this.definition.id) {
                return "Update definition";
            }
            return "Create definition";
        }
    }

    @computed
    get isNextActive() {
        let {
            selectionMethod,
            wizardStore,
            fileToUpload,
            definition,
            schemaConstructorStore
        } = this;

        let { step } = wizardStore;

        if (step === 0) {
            if (selectionMethod === DatasetSelectionMethods.existing) {
                return true;
            }

            if (selectionMethod === DatasetSelectionMethods.bucket) {
                return this.bucketFormStore.isValid;
            }

            if (
                selectionMethod === DatasetSelectionMethods.upload &&
                fileToUpload
            ) {
                return true;
            }
        }

        if (step === 1) {
            return true;
        }

        if (step === 2 && definition.name) {
            return true;
        }
    }

    @computed
    get isBackActive() {
        let store = this;
        let { selectionMethod, wizardStore, fileToUpload } = store;
        let { step } = wizardStore;
        if (step === 0) {
            return false;
        }
        return true;
    }

    @action.bound
    onDefinitionUpdate = () => {
        let {
            appStore,
            wizardCurrentStep,
            wizardStore,
            wizardNextState,
            definition,
            onComplete = function() {}
        } = this;

        let json = definition.toJSON();
        let { id, ...rest } = json;

        appStore.definitionsStore
            .update(json.id, {
                ...rest
            })
            .then(() => {
                this.onComplete();
                // move to onComplete
                appStore.datasetsStore.getAll();
                appStore.definitionsStore.getAll();
            });
    };

    @action.bound
    onSchemaApply = () => {
        let {
            appStore,
            definition,
            wizardStore,
            finalizeFormStore,
            schemaConstructorStore
        } = this;

        schemaConstructorStore.displaySchema.validate().then(({ hasError, message }) => {
            if (!hasError) {
                definition.setSchema(schemaConstructorStore.schema);
                wizardStore.handleNextStep();
            } else if(message) { 
                this.appStore.errorStore.addError({ message });
            }
        });
    };

    @action.bound
    onDatasetSubmit = () => {
        let {
            appStore,
            definition,
            wizardStore,
            fileToUpload,
            selectionMethod,
            bucketFormStore,
            selectedDataset
        } = this;

        if (selectionMethod === DatasetSelectionMethods.existing) {
            if (selectedDataset) {
                definition.setDataset(
                    new DatasetEntry(selectedDataset.toJSON())
                );
            }
            //Skip
            wizardStore.handleNextStep();
            return;
        }

        if (selectionMethod === DatasetSelectionMethods.bucket) {
            let bucketForm = bucketFormStore.form;

            let dataset = new DatasetEntry({
                ...definition.dataset.toJSON(),
                name: bucketForm.$.name.$,
                bucket: bucketForm.$.bucket.$,
                objectKey: bucketForm.$.objectKey.$,
                secretKey: bucketForm.$.secretKey.$,
                accessKey: bucketForm.$.accessKey.$
            });

            appStore.datasetsStore
                .updateFromBucket(definition.id, dataset.toJSON())
                .then(dataset => {
                    definition.setDataset(dataset);

                    this.selectedDatasetId = dataset.id;
                    this.updateDefinitionSchema(dataset.id);

                    appStore.datasetsStore.getAll();
                    wizardStore.handleNextStep();
                    return dataset;
                });

            return;
        }

        if (selectionMethod === DatasetSelectionMethods.upload) {
            this.setIsDatasetUploading(true);
            let [promise, cancel] = appStore.datasetsStore.createFromFile(
                fileToUpload,
                throttle(this.setUploadingProgress, 100)
            );

            this.setCancelToken(cancel);

            promise.then(
                dataset => {
                    definition.setDataset(dataset);

                    this.selectedDatasetId = dataset.id;
                    this.updateDefinitionSchema(dataset.id);

                    appStore.datasetsStore.getAll();

                    wizardStore.handleNextStep();
                    this.setIsDatasetUploading(false);
                    return dataset;
                },
                err => {
                    this.setIsDatasetUploading(false);
                }
            );
            return;
        }
    };

    @action.bound
    onSchemaSkip() {
        this.definition.setSchema(null);
        this.wizardStore.handleNextStep();
    }
}
