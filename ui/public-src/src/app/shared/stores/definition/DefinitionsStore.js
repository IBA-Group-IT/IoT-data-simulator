import { observable, computed, action } from "mobx";
import { fromPromise } from "mobx-utils";

import DefinitionEntry from "models/DefinitionEntry";
import JsonSchema from "models/schema/JsonSchema";


export default class DefinitionsStore {
    appStore;
    @observable.ref items;
    @observable.ref deletedItemId = null;
    @observable.ref updatedItemId = null;

    constructor(appStore, filterStore) {
        this.appStore = appStore;
        this.filterStore = filterStore;
    }

    @computed
    get filteredItems() {
        return this.filterStore.filter(this.items);
    }

    getItemsBySchemaType(items, schemaType) {
        return items.filter(item => {
            return item.schema && item.schema.schemaType === schemaType;
        });
    }

    getAll() {
        console.log("...loading definitions");
        return this.appStore.transportLayer
            .get("/api/definitions")
            .then(({ data = [] }) => {
                console.log("definitions loaded: ", data);
                this.items = data.map(params => {
                    return new DefinitionEntry(params);
                });
            });
    }

    create(datasetId, schema, name) {
        return this.appStore.transportLayer.post("/api/definitions", {
            datasetId,
            schema,
            name
        });
    }

    findInstanceById(id) {
        let result = null;
        if (this.items) {
            for (let i = 0; i < this.items.length; i++) {
                if (this.items[i].id === id) {
                    result = this.items[i];
                    break;
                }
            }
        }
        return result;
    }

    //TODO move to entry
    update(dataDefinitionId, params) {
        this.updatedItemId = null;
        let { dataset, ...rest } = params;
        let datasetId = dataset ? dataset.id : null;

        if (!dataDefinitionId) {
            return this.create(datasetId, rest.schema, rest.name);
        }
        return this.appStore.transportLayer
            .put(`/api/definitions/${dataDefinitionId}`, {
                ...rest,
                datasetId
            })
            .then(
                action(result => {
                    this.updatedItemId = dataDefinitionId;
                    return result;
                })
            );
    }

    delete(dataDefinitionId) {
        console.log("...deleting definition ", dataDefinitionId);
        return this.appStore.transportLayer
            .delete(`/api/definitions/${dataDefinitionId}`)
            .then(({ data }) => {
                console.log(`${dataDefinitionId} successfully deleted`);
                this.deletedItemId = dataDefinitionId;
                return data;
            });
    }

    getById(dataDefinitionId) {
        console.log("...get details for definition ", dataDefinitionId);
        return this.appStore.transportLayer
            .get(`/api/definitions/${dataDefinitionId}`)
            .then(({ data }) => {
                console.log(`${dataDefinitionId} details received:`, data);
                return new DefinitionEntry(data);
            });
    }

    getSchemaProperties(dataDefinitionId) {
        console.log(
            "...get schema properties for definition ",
            dataDefinitionId
        );
        return this.appStore.transportLayer
            .get(`/api/definitions/${dataDefinitionId}/schema/properties`)
            .then(({ data }) => {
                console.log(
                    `${dataDefinitionId} schema properties received:`,
                    data
                );
                return data;
            });
    }

    getSchemaRules(dataDefinitionId) {
        console.log("...get schema rules for definition ", dataDefinitionId);
        return this.appStore.transportLayer
            .get(`/api/definitions/${dataDefinitionId}/schema/rules`)
            .then(({ data }) => {
                console.log(`${dataDefinitionId} schema rules received`, data);
                return new JsonSchema(data);
            });
    }
}
