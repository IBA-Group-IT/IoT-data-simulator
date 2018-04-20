import { observable, computed, action, runInAction, transaction } from "mobx";
import { chunkProcessor } from 'mobx-utils';
import SessionEntry from "models/session/SessionEntry";

import LogEntry from "models/session/LogEntry";
let LogTypes = LogEntry.types;

export default class SessionsStore {
    appStore;

    filterTypes = {
        all: "all",
        active: "active",
        notActive: "notActive"
    };

    bufferSize = 100;

    @observable.shallow items = [];
    
    @observable.shallow logs = [];
    @observable.shallow logsBuffer = [];

    @observable nameFilter = "";
    @observable stateFilter = this.filterTypes.all;

    @action.bound
    setNameFilter(filter) {
        this.nameFilter = filter;
    }

    @action.bound
    setStateFilter(filter) {
        this.stateFilter = filter;
    }

    @computed
    get filteredItems() {
        if (this.items) {
            let filtered = this.items;

            if (this.nameFilter) {
                filtered = filtered.filter(item => {
                    return (
                        item.name
                            .toLowerCase()
                            .indexOf(this.nameFilter.toLowerCase()) !== -1
                    );
                });
            }

            if (this.stateFilter !== this.filterTypes.all) {
                if (this.stateFilter === this.filterTypes.active) {
                    filtered = filtered.filter(item => {
                        return item.state;
                    });
                } else if (this.stateFilter === this.filterTypes.notActive) {
                    filtered = filtered.filter(item => {
                        return !item.state;
                    });
                }
            }
            return filtered;
        }
        return [];
    }

    constructor(appStore, subscriptionManager, queryManager) {
        this.appStore = appStore;
        this._itemsMap = observable.map({});
        this.subscriptionManager = subscriptionManager;
        this.queryManager = queryManager;

        this.subscriptionManager.subscribeToAllSessions(
            this.handleSubscription
        );

        let bufferSize = this.bufferSize;
        const stop = chunkProcessor(this.logsBuffer, chunk => {
            let itemsCount = this.logs.length + chunk.length;
            let offset = itemsCount > bufferSize ? itemsCount - bufferSize : 0;
            this.logs.replace(
                this.logs.slice(offset).concat(chunk)
            )
            this.logsBuffer.clear();
        }, 200, bufferSize)
    }

    handleSubscription = data => {
        let { type } = data;

        if (type === LogTypes.status) {
            let { status: { sessionId, state } } = data;
            this._itemsMap.get(sessionId).setState(state);
        } else if (type === LogTypes.payload || type === LogTypes.error) {
            let { message, sessionId, timestamp } = data;
            let sessionName = this._itemsMap.get(sessionId).name;

            this.logsBuffer.push({
                message,
                sessionId,
                sessionName,
                timestamp,
                type
            });
        } else if (type === LogTypes.analytics) {
            let { message, sessionId, timestamp, tag } = data;
            let sessionName = this._itemsMap.get(sessionId).name;

            this.logsBuffer.push({
                message,
                sessionId,
                sessionName,
                timestamp,
                tag,
                type
            });
        }
    };

    getAll() {
        console.log("...loading sessions");
        return this.appStore.transportLayer.get("/api/sessions").then(
            action(({ data = [] }) => {
                console.log("sessions loaded: ", data);
                this.items = data.map(params => {
                    let session = new SessionEntry(params);
                    this._itemsMap.set(params.id, session);
                    return session;
                });
            })
        );
    }

    getById(sessionId) {
        return this.appStore.transportLayer
            .get(`/api/sessions/${sessionId}`)
            .then(({ data }) => {
                console.log(`${sessionId} details received:`, data);
                return new SessionEntry(data);
            });
    }

    create(params) {
        return this.appStore.transportLayer.post("/api/sessions", {
            ...params
        });
    }

    update(sessionId, params) {
        if (!sessionId) {
            return this.create(params);
        }
        return this.appStore.transportLayer.put(`/api/sessions/${sessionId}`, {
            ...params
        });
    }

    delete(sessionId) {
        console.log("...deleting session ", sessionId);
        return this.appStore.transportLayer
            .delete(`/api/sessions/${sessionId}`)
            .then(({ data }) => {
                console.log(`session ${sessionId} successfully deleted`);
                return data;
            });
    }

    import(file) {
        let reader = new FileReader();
        let scope = this;

        reader.readAsText(file);
        reader.onload = function() {
            let { result } = reader;

            scope.appStore
                .transportLayer({
                    method: "post",
                    url: "/api/sessions/import",
                    data: result,
                    headers: {
                        "Content-Type": "application/json"
                    }
                })
                .then(() => {
                    scope.getAll();
                });
        };
    }

    getSessionsStatus() {
        return this.getAll()
            .then(() => {
                return this.queryManager.getSessionsStatus();
            })
            .then(data => {
                let { statuses = [] } = data;
                statuses.forEach(
                    action(status => {
                        let { state, sessionId } = status;
                        this._itemsMap.get(sessionId).setState(state);
                    })
                );
            });
    }

    startSession = id => {
        return this.queryManager.startSession(id);
    };

    pauseSession = id => {
        return this.queryManager.pauseSession(id);
    };

    stopSession = id => {
        return this.queryManager.stopSession(id);
    };

    resumeSession = id => {
        return this.queryManager.resumeSession(id);
    };

    restartSession = id => {
        return this.queryManager.restartSession(id);
    };

    clearLogs = () => {
        this.logs.clear();
    };
}
