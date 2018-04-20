import { observable, action, computed, toJS } from "mobx";

import DefinitionEntry from "models/DefinitionEntry";
import systemFactory from "models/system/factory";
import TimerEntry from "./TimerEntry";
import DeviceEntry from "models/device/DeviceEntry";
import DeviceInjectorEntry from "./DeviceInjectorEntry";
import DatasetFilterEntry from "./DatasetFilterEntry";

import GeneratorEntry from "./GeneratorEntry";
import { types as generatorTypes } from "./GeneratorEntry";

export default class SessionEntry {
    id;
    @observable name;
    @observable.ref dataDefinition;
    @observable.ref targetSystem;
    @observable.ref timer;
    @observable state;
    @observable.ref deviceInjector;
    @observable devices = [];
    @observable paths = [];
    @observable ticksNumber;
    @observable isReplayLooped = false;

    constructor(
        {
            id,
            name,
            ticksNumber,
            isReplayLooped = false,
            dataDefinition,
            timer = {},
            generator = { type: generatorTypes.jsFunction },
            targetSystem = {},
            state,
            devices = [],
            deviceInjector = {},
            datasetFilter = {}
        } = {}
    ) {
        this.id = id;
        this.name = name;
        this.ticksNumber = ticksNumber;
        this.isReplayLooped = isReplayLooped;

        this.dataDefinition = new DefinitionEntry({
            ...dataDefinition
        });
        this.targetSystem = systemFactory(targetSystem);
        this.timer = new TimerEntry(timer);
        this.state = state;
        this.devices = devices.map(params => {
            return new DeviceEntry(params);
        });
        this.deviceInjector = new DeviceInjectorEntry(deviceInjector);
        this.datasetFilter = new DatasetFilterEntry(datasetFilter);
        this.generator = new GeneratorEntry(generator);
    }

    @action.bound
    setName(name) {
        this.name = name;
    }

    @action.bound
    setState(state) {
        this.state = state;
    }

    @action.bound
    setTicksNumber(ticksNumber) { 
        this.ticksNumber = ticksNumber;
    }

    @action.bound
    setIsReplayLooped(isLooped) { 
        this.isReplayLooped = isLooped;
    }

    @action.bound
    setDefinition(definition) {
        this.dataDefinition = definition;
        this.generator.setType(
            definition.schema
                ? generatorTypes.schema
                : generatorTypes.jsFunction
        );
    }

    @action.bound
    setSystem(system) {
        this.targetSystem = system;
    }

    @action.bound
    setPaths(paths) {
        this.paths = paths;
    }

    @computed
    get datePaths() { 
        return this.paths.filter(path => {
            return path.type === "timestamp" || path.type === "date";
        })
    }

    @computed
    get isRunning() {
        return this.state === "running";
    }

    @computed
    get isCompleted() {
        return this.state === "completed";
    }

    @computed
    get isPaused() {
        return this.state === "paused";
    }

    @computed
    get isFailed() {
        return this.state === "failed";
    }

    @computed
    get canPause() {
        return this.isRunning;
    }

    @computed
    get canResume() {
        return this.isPaused;
    }

    @computed
    get canRun() {
        return !this.state || this.isCompleted || this.isFailed;
    }

    @computed
    get canRestart() {
        return this.isRunning || this.isPaused;
    }

    @computed
    get canStop() {
        return this.isRunning || this.isPaused;
    }

    @computed
    get hasDefinition() {
        return this.dataDefinition && this.dataDefinition.id;
    }

    @computed
    get hasSchema() {
        return !!(
            this.generator.type === generatorTypes.schema &&
            this.generator.schema
        );
    }

    @computed
    get schema() {
        return this.generator.schema;
    }

    @computed
    get hasTargetSystem() {
        return this.targetSystem && this.targetSystem.id;
    }

    @computed
    get hasDevices() {
        return !!this.devices.length;
    }

    @computed
    get devicesIds() {
        return this.hasDevices ? this.devices.map(({ id }) => id) : [];
    }

    @action.bound
    removeDeviceById(id) {
        let idx = -1;
        for (let i = 0; i < this.devices.length; i++) {
            if (this.devices[i].id === id) {
                idx = i;
                break;
            }
        }
        if (idx > -1) {
            this.devices = this.devices
                .slice(0, idx)
                .concat(this.devices.slice(idx + 1));
        }
    }

    @action.bound
    addDevice(device) {
        this.devices.push(device);
    }

    @computed
    get data() {
        let { id, name, isReplayLooped, ticksNumber, timer, ...params } = this.toJSON();

        let data = {
            name,
            ticksNumber,
            timer,
            isReplayLooped
        };

        if (id) {
            data.id = id;
        }
        if (params.dataDefinition) {
            data.dataDefinitionId = params.dataDefinition.id;
        }
        if (params.targetSystem) {
            data.targetSystemId = params.targetSystem.id;
        }
        if (params.devices) {
            data.devices = params.devices.map(device => device.id);
        }
        if (params.deviceInjector) {
            data.deviceInjector = params.deviceInjector;
        }
        if (params.datasetFilter) {
            data.datasetFilter = params.datasetFilter;
        }
        if (params.generator) {
            data.generator = params.generator;
        }

        return data;
    }

    toJSON() {
        let params = toJS({
            id: this.id,
            name: this.name,
            isReplayLooped: this.isReplayLooped
        });

        if(this.ticksNumber) {
            params.ticksNumber = parseInt(this.ticksNumber, 10);
        }

        if (this.dataDefinition && this.dataDefinition.id) {
            params.dataDefinition = this.dataDefinition.toJSON();
        }

        if (this.devices.length) {
            params.devices = this.devices.map(device => device.toJSON());
        }

        if (this.timer) {
            params.timer = this.timer.toJSON();
        }

        if (this.targetSystem.id) {
            params.targetSystem = this.targetSystem.toJSON();
        }

        if (this.deviceInjector && this.deviceInjector.rule) {
            params.deviceInjector = this.deviceInjector.toJSON();
        }

        if (this.datasetFilter && this.datasetFilter.type) {
            params.datasetFilter = this.datasetFilter.toJSON();
        }

        if (this.generator && this.generator.type) {
            params.generator = this.generator.toJSON();
        }

        return params;
    }
}
