import { observable, computed, action, runInAction, transaction } from "mobx";
import SessionEntry from "models/session/SessionEntry";

import LogEntry from "models/session/LogEntry";
let LogTypes = LogEntry.types;

class Buffer {
    DELAY = 200; //ms
    buffer = [];
    lastTms = null;

    //@observable.shallow items = [];

    constructor(size) {
        this.size = size;
        this.items = observable.shallowArray([]);
    }

    @action.bound
    push(item) {
        let now = Date.now();

        if (this.lastTms && now - this.lastTms < this.DELAY) {
            if (!this.timer) {
                this.timer = setTimeout(() => {
                    //runInAction(() => {

                    // this.buffer.forEach(datum => {
                    //     if (this.items.length >= this.size) {
                    //         this.items.shift();
                    //     }
                    //     this.items.push(datum);
                    // });

                    if (this.buffer.length >= this.size) {
                        this.items.replace(
                            this.buffer.slice(this.buffer.length - this.size)
                        );
                    } else {
                        let itemsCount = this.size - this.buffer.length;
                        this.items.replace(
                            this.items
                                .slice(0, itemsCount - 1)
                                .concat(this.buffer)
                        );
                    }

                    this.lastTms = Date.now();
                    this.buffer = [];

                    clearTimeout(this.timer);
                    this.timer = null;
                    //});
                }, this.DELAY);
            }
            this.buffer.push(item);
        } else {
            if (!this.timer) {
                this.items.push(item);
                this.lastTms = Date.now();
            } else {
                this.buffer.push(item);
            }
        }
    }
    getAll() {
        return this.items;
    }
    clear() {
        this.items.replace([]);
    }
}

export default class SessionsStore {
    appStore;

    filterTypes = {
        all: "all",
        active: "active",
        notActive: "notActive"
    };

    @observable.shallow items = [];
    logs = new Buffer(100);
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
    }

    handleSubscription = data => {
        let { type } = data;

        if (type === LogTypes.status) {
            let { status: { sessionId, state } } = data;
            this._itemsMap.get(sessionId).setState(state);
        } else if (type === LogTypes.payload || type === LogTypes.error) {
            let { message, sessionId, timestamp } = data;
            let sessionName = this._itemsMap.get(sessionId).name;

            this.logs.push({
                message,
                sessionId,
                sessionName,
                timestamp,
                type
            });
        } else if (type === LogTypes.analytics) {
            let { message, sessionId, timestamp, tag } = data;
            let sessionName = this._itemsMap.get(sessionId).name;

            this.logs.push({
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
