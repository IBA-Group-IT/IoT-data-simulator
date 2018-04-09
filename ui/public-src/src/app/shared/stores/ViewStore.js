import { observable, computed, action } from "mobx";
import JsonSchema from "models/schema/JsonSchema";

export default class ViewStore {
    appStore;
    @observable currentTab = 0;
    @observable page = "sessions";

    constructor(appStore) {
        this.appStore = appStore;
    }

    @computed
    get url() {
        switch (this.page) {
            case "main":
                return "/";
            case "sessions":
                return "/sessions";
            case "definitions":
                return "/definitions";
            case "devices":
                return "/devices";
            case "systems":
                return "/systems";
            default:
                return "/404";
        }
    }

    // Tabs
    openSessionsPage = () => {
        this.page = "sessions";
        this.currentTab = 0;
        this.appStore.sessionsScreenStore.load();
    };

    openDefinitionsPage = () => {
        this.page = "definitions";
        this.currentTab = 1;
        this.appStore.definitionsScreenStore.load();
    };

    openDevicesPage = () => {
        this.page = "devices";
        this.currentTab = 2;
        this.appStore.devicesScreenStore.load();
    };

    openSystemsPage = () => {
        this.page = "systems";
        this.currentTab = 3;
        this.appStore.systemsScreenStore.load();
    };

    // Modals
    openCreateDefinition = () => {
        let { definitionModalStore, datasetsStore } = this.appStore;
        datasetsStore.getAll();
        definitionModalStore.openCreate();
    };

    closeCreateDefinition = () => {
        this.appStore.definitionModalStore.closeCreate();
    };

    openEditDefinition = id => {
        let {
            appStore: {
                definitionModalStore,
                definitionManagementStore,
                definitionsStore,
                editDefinitionStore,
                datasetsStore
            }
        } = this;

        definitionManagementStore.setEditingEntityId(id);
        datasetsStore.getAll();

        definitionsStore.getById(id).then(definition => {
            definitionManagementStore.setEditingEntity(definition);
            editDefinitionStore.reset();
            definitionModalStore.openEdit();
        });
    };

    closeEditDefinition = () => {
        this.appStore.definitionManagementStore.setEditingEntityId(null);
        this.appStore.definitionModalStore.closeEdit();
    };

    openDeleteDefinition = definition => {
        this.appStore.definitionManagementStore.setDeletingEntity(definition);
        this.appStore.definitionModalStore.openDelete();
    };

    closeDeleteDefinition = () => {
        this.appStore.definitionModalStore.closeDelete();
    };

    openCreateSystem = () => {
        this.appStore.systemModalStore.openCreate();
    };

    closeCreateSystem = () => {
        this.appStore.systemModalStore.closeCreate();
    };

    openEditSystem = id => {
        let {
            appStore: { systemModalStore, systemManagementStore, systemsStore }
        } = this;

        systemManagementStore.setEditingEntityId(id);
        systemModalStore.openEdit();

        systemsStore.getById(id).then(system => {
            systemManagementStore.setEditingEntity(system);
        });
    };

    closeEditSystem = () => {
        let { appStore: { systemModalStore, systemManagementStore } } = this;

        systemManagementStore.setEditingEntityId(null);
        systemModalStore.closeEdit();
    };

    openDeleteSystem = system => {
        let { appStore: { systemManagementStore, systemModalStore } } = this;

        systemManagementStore.setDeletingEntity(system);
        systemModalStore.openDelete();
    };

    closeDeleteSystem = () => {
        this.appStore.systemModalStore.closeDelete();
    };

    openCreateDevice = () => {
        let {
            appStore: {
                deviceManagementStore,
                deviceSystemManagementStore,
                deviceModalStore
            }
        } = this;
        deviceSystemManagementStore.reset();
        this.appStore.deviceModalStore.openCreate();
    };

    closeCreateDevice = () => {
        this.appStore.deviceModalStore.closeCreate();
    };

    openEditDevice = deviceId => {
        let {
            appStore: {
                deviceModalStore,
                deviceManagementStore,
                deviceSystemManagementStore,
                devicesStore,
                editDeviceStore
            }
        } = this;

        editDeviceStore.reset();
        deviceManagementStore.setEditingEntityId(deviceId);
        devicesStore.getById(deviceId).then(device => {
            deviceManagementStore.setEditingEntity(device);
            deviceSystemManagementStore.reset();
            deviceModalStore.openEdit();
        });
    };

    closeEditDevice = () => {
        let {
            appStore: { deviceModalStore, devicesStore, deviceManagementStore }
        } = this;

        deviceManagementStore.setEditingEntityId(null);
        deviceModalStore.closeEdit();
    };

    openDeleteDevice = device => {
        let { appStore: { deviceModalStore, deviceManagementStore } } = this;
        deviceManagementStore.setDeletingEntity(device);
        deviceModalStore.openDelete();
    };

    closeDeleteDevice = () => {
        let { appStore: { deviceModalStore, deviceManagementStore } } = this;
        deviceModalStore.closeDelete();
    };

    openCreateDeviceSystem = () => {
        let { deviceSystemModalStore } = this.appStore;
        deviceSystemModalStore.openCreate();
    };

    closeCreateDeviceSystem = () => {
        let { deviceSystemModalStore } = this.appStore;
        deviceSystemModalStore.closeCreate();
    };

    openEditDeviceSystem = system => {
        let { appStore } = this;
        let {
            deviceSystemManagementStore,
            deviceSystemModalStore,
            devicesStore
        } = appStore;

        deviceSystemManagementStore.setEditingEntityId(system.id);
        deviceSystemManagementStore.setEditingEntity(system);
        deviceSystemModalStore.openEdit();
    };

    closeEditDeviceSystem = () => {
        let { appStore } = this;
        let { deviceSystemModalStore, deviceSystemManagementStore } = appStore;
        deviceSystemManagementStore.setEditingEntityId(null);
        deviceSystemModalStore.closeEdit();
    };

    openDeleteDeviceSystem = system => {
        let { appStore } = this;
        let { deviceSystemModalStore, deviceSystemManagementStore } = appStore;

        deviceSystemManagementStore.setDeletingEntity(system);
        deviceSystemModalStore.openDelete();
    };

    closeDeleteDeviceSystem = () => {
        let { appStore } = this;
        let { deviceSystemModalStore } = appStore;
        deviceSystemModalStore.closeDelete();
    };

    // Sessions
    openCreateSession = () => {
        let { sessionModalStore } = this.appStore;
        sessionModalStore.openCreate();
    };

    closeCreateSession = () => {
        let { sessionModalStore } = this.appStore;
        sessionModalStore.closeCreate();
    };

    openEditSession = session => {
        let { appStore } = this;
        let {
            sessionsManagementStore,
            sessionModalStore,
            sessionsStore
        } = appStore;

        sessionsManagementStore.setEditingEntityId(session.id);
        sessionsStore.getById(session.id).then(sessionDetails => {
            sessionsManagementStore.setEditingEntity(sessionDetails);
            sessionModalStore.openEdit();
        });
    };

    closeEditSession = () => {
        let { appStore } = this;
        let { sessionModalStore, sessionsManagementStore } = appStore;
        sessionsManagementStore.setEditingEntityId(null);
        sessionModalStore.closeEdit();
    };

    openDeleteSession = session => {
        let { appStore } = this;
        let { sessionModalStore, sessionsManagementStore } = appStore;

        sessionsManagementStore.setDeletingEntity(session);
        sessionModalStore.openDelete();
    };

    closeDeleteSession = () => {
        let { appStore } = this;
        let { sessionModalStore } = appStore;
        sessionModalStore.closeDelete();
    };

    openDeleteDataset = dataset => {
        let { appStore } = this;
        let { datasetModalStore, datasetManagementStore } = appStore;
        datasetManagementStore.setDeletingEntity(dataset);
        datasetModalStore.openDelete();
    };

    closeDeleteDataset = () => {
        let { appStore } = this;
        let { datasetModalStore, datasetManagementStore } = appStore;
        datasetModalStore.closeDelete();
        setTimeout(() => {
            datasetManagementStore.setDeletingEntity(null);
        });
    };
}
