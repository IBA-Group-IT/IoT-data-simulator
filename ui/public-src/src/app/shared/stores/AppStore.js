import { observable, computed, action, autorun, reaction } from "mobx";
import ViewStore from "./ViewStore";
import DimensionsStore from "./screen/DimensionsStore";

import DefinitionsStore from "./definition/DefinitionsStore";
import DefinitionsFilterStore from "./definition/DefinitionsFilterStore";
import DatasetsStore from "./definition/DatasetsStore";

import DefinitionsScreenStore from "./screen/DefinitionsScreenStore";
import WizardStore from "./WizardStore";

import SystemsScreenStore from "./screen/SystemsScreenStore";
import SystemFormStore from "./system/SystemFormStore";
import UpdateSystemStore from "./system/UpdateSystemStore";
import SystemsStore from "./system/SystemsStore";
import ModalStore from "./ModalStore";

import DevicesScreenStore from "./screen/DevicesScreenStore";
import DevicesStore from "./device/DevicesStore";
import UpdateDeviceStore from "./device/UpdateDeviceStore";

import DefinitionEntry from "models/DefinitionEntry";
import DatasetEntry from "models/DatasetEntry";
import DeviceEntry from "models/device/DeviceEntry";
import SessionEntry from "models/session/SessionEntry";
import EntityManagementStore from "./EntityManagementStore";
import targetSystemFactory from "models/system/factory";
import DeviceSystemCreateFormStore from "./device/DeviceSystemCreateFormStore";
import DeviceSystemEditFormStore from "./device/DeviceSystemEditFormStore";

import FinalizeFormStore from "./definition/FinalizeFormStore";
import UpdateDefinitionStore from './definition/UpdateDefinitionStore';

import SessionsScreenStore from "stores/screen/SessionsScreenStore";

import StompQueryManager from "api/stomp/stompQueryManager";
import StompSubscriptionManager from "api/stomp/stompSubscriptionManager";
import SessionsApiQueryManager from "api/sessionsApiQueryManager";
import SessionsApiSubscriptionManager from "api/sessionsApiSubscriptionManager";
import SessionsStore from "stores/session/SessionsStore";

import UpdateSessionStore from "./session/UpdateSessionStore";

import ErrorStore from "stores/ErrorStore";
import FilterStore from 'stores/FilterStore';
import entities from 'constants/entities';

const filterStoreFactory = entity => {

    const filterByName = value => {
        return state => {
            return state.name.toLowerCase().indexOf(value.toLowerCase()) !== -1;
        };
    };

    switch (entity) {

        case entities.definition:

            const filterTypes = {
                byDataset: "byDataset",
                byName: "byName"
            };

            const filterByDatasetTypes = {
                all: "all",
                hasDataset: "hasDataset",
                noDataset: "noDataset"
            }

            const filterByDataset = filterType => {
                if (filterType === filterByDatasetTypes.all) {
                    return item => true;
                }
                return item => {
                    return filterType === filterByDatasetTypes.hasDataset
                        ? item.hasDataset
                        : !item.hasDataset;
                };
            };

            return new FilterStore({
                criteriaOptions: {
                    [filterTypes.byName]: filterByName,
                    [filterTypes.byDataset]: filterByDataset
                },
                values: {
                    [filterTypes.byName]: "",
                    [filterTypes.byDataset]: filterByDatasetTypes.all
                }
            });
    }
};

export default class AppStore {
    view;

    constructor(transportLayer, connectionProvider) {
        this.transportLayer = transportLayer;
        this.errorStore = new ErrorStore();

        transportLayer.interceptors.response.use(
            response => {
                return response;
            },
            error => {
                let { response: { data: { message } } } = error;
                this.errorStore.addError({ message });
                return Promise.reject(error);
            }
        );

        let queryManager = new StompQueryManager(connectionProvider);
        let subscriptionManager = new StompSubscriptionManager(
            connectionProvider
        );
        let sessionsApiQueryManager = new SessionsApiQueryManager(queryManager);
        let sessionsApiSubscriptionManager = new SessionsApiSubscriptionManager(
            subscriptionManager
        );

        this.view = new ViewStore(this);
        this.dimensionsStore = new DimensionsStore();

        this.definitionModalStore = new ModalStore();
        this.datasetModalStore = new ModalStore();
        this.systemModalStore = new ModalStore();
        this.deviceModalStore = new ModalStore();
        this.deviceSystemModalStore = new ModalStore();
        this.sessionModalStore = new ModalStore();

        // Stores with creating/editing entities persisted
        this.definitionManagementStore = new EntityManagementStore(() => {
            return new DefinitionEntry();
        });
        this.datasetManagementStore = new EntityManagementStore(() => {
            return new DatasetEntry();
        });
        this.deviceManagementStore = new EntityManagementStore(() => {
            return new DeviceEntry();
        });
        //TODO refactor duplication
        this.systemManagementStore = new EntityManagementStore(() => {
            return targetSystemFactory({
                type: "dummy"
            });
        });
        this.deviceSystemManagementStore = new EntityManagementStore(() => {
            let { deviceManagementStore } = this;
            let device = deviceManagementStore.entity;
            if (device) {
                let newType = "";
                let types = [
                    "dummy",
                    "mqtt_broker",
                    "rest_endpoint",
                    "websocket_endpoint"
                ];
                for (let i = 0; i < types.length; i++) {
                    if (!device.findTargetSystemByType(types[i])) {
                        newType = types[i];
                        break;
                    }
                }
                return targetSystemFactory({
                    type: newType
                });
            }
            return targetSystemFactory({
                type: "dummy"
            });
        });
        this.sessionsManagementStore = new EntityManagementStore(() => {
            return new SessionEntry();
        });

        // Data stores
        this.definitionsStore = new DefinitionsStore(
            this,
            filterStoreFactory('definition')
        );

        this.datasetsStore = new DatasetsStore(this);
        this.systemsStore = new SystemsStore(this);
        this.devicesStore = new DevicesStore(this);
        this.sessionsStore = new SessionsStore(
            this,
            sessionsApiSubscriptionManager,
            sessionsApiQueryManager
        );

        // Definition
        this.definitionsScreenStore = new DefinitionsScreenStore(this);

        // Create definition
        let test = this.definitionManagementStore;
        this.createDefinitionStore = new UpdateDefinitionStore({
            root: this,
            state: observable({
                get definition(){ return test.creationEntity }
            }),
            onComplete: () => {
                this.definitionModalStore.closeCreate();
                this.definitionManagementStore.reset();
            }
        });

        this.createDefinitionStore.initReactions();

        // Update definition
        this.editDefinitionStore = new UpdateDefinitionStore({
            root: this,
            state: observable({
                get definition(){ return test.editingEntity}
            }),
            onComplete: () => {
                this.definitionModalStore.closeEdit();
                this.definitionManagementStore.resetEditing();
            }
        });

        this.editDefinitionStore.initReactions();

        // Target systems
        this.createSystemFormStore = new SystemFormStore(
            this.systemManagementStore,
            this.deviceManagementStore
        );
        this.createSystemStore = new UpdateSystemStore(
            this,
            this.systemManagementStore,
            this.createSystemFormStore
        );
        this.editSystemFormStore = new SystemFormStore(
            this.systemManagementStore,
            this.deviceManagementStore
        );
        this.editSystemStore = new UpdateSystemStore(
            this,
            this.systemManagementStore,
            this.editSystemFormStore
        );
        this.systemsScreenStore = new SystemsScreenStore(this);

        // Devices
        this.createDeviceWizardStore = new WizardStore();
        this.editDeviceWizardStore = new WizardStore();

        this.createDeviceStore = new UpdateDeviceStore(
            this,
            this.deviceManagementStore,
            this.createDeviceWizardStore
        );
        this.editDeviceStore = new UpdateDeviceStore(
            this,
            this.deviceManagementStore,
            this.editDeviceWizardStore
        );

        this.deviceSystemModalStore = new ModalStore();
        this.deviceSystemCreateFormStore = new DeviceSystemCreateFormStore(
            this.deviceSystemManagementStore,
            this.deviceManagementStore
        );
        this.deviceSystemEditFormStore = new DeviceSystemEditFormStore(
            this.deviceSystemManagementStore,
            this.deviceManagementStore
        );

        this.deviceSystemCreateStore = new UpdateSystemStore(
            this,
            this.deviceSystemManagementStore,
            this.deviceSystemCreateFormStore
        );
        this.deviceSystemEditStore = new UpdateSystemStore(
            this,
            this.deviceSystemManagementStore,
            this.deviceSystemEditFormStore
        );
        this.devicesScreenStore = new DevicesScreenStore(
            this,
            this.deviceSystemManagementStore,
            this.deviceSystemModalStore,
            this.devicesStore
        );

        // Sessions
        this.sessionsScreenStore = new SessionsScreenStore(this);

        this.createSessionStore = new UpdateSessionStore(
            this,
            this.sessionsManagementStore,
            "creationEntity"
        );
        this.editSessionStore = new UpdateSessionStore(
            this,
            this.sessionsManagementStore,
            "editingEntity"
        );

        // For debugging!
        window.___app = this;
    }
}
