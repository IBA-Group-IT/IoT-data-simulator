import { observable, computed, action } from "mobx";
import { fromPromise } from "mobx-utils";

import DatasetEntry from "models/DatasetEntry";
import JsonSchema from "models/schema/JsonSchema";
import axios from "axios";

var CancelToken = axios.CancelToken;

export default class DatasetsStore {
    appStore;
    @observable.ref datasets;
    @observable.ref deletedItemId = null;

    constructor(appStore) {
        this.appStore = appStore;
    }

    getAll() {
        console.log("...loading datasets");
        return this.appStore.transportLayer
            .get("/api/datasets")
            .then(({ data = [] }) => {
                console.log("datasets loaded: ", data);
                this.datasets = data.map(params => {
                    return new DatasetEntry(params);
                });
            });
    }

    createFromFile(file, onUploadProgress) {
        var data = new FormData();
        data.append("file", file);

        let cancelToken = null;
        let promise =  this.appStore.transportLayer
            .post("/api/datasets/upload", data, {
                cancelToken: new CancelToken((c) => {
                    cancelToken = c;
                }),
                onUploadProgress,
                headers: {
                    "Content-Type": "multipart/form-data",
                    "File-Size": file.size
                }
            })
            .then(({ data }) => {
                console.log("Dataset created. Response:", data);
                return new DatasetEntry(data);
            });

        return [promise, cancelToken];
    }

    updateFromBucket(id, options) {
        if (!id) {
            return this.appStore.transportLayer
                .post("/api/datasets", options)
                .then(({ data }) => {
                    console.log("Dataset created. Response:", data);
                    return new DatasetEntry(data);
                });
        }
        return this.appStore.transportLayer
            .put(`/api/datasets/${id}`, {
                ...options
            })
            .then(({ data }) => {
                console.log("Dataset updated. Response:", data);
                return new DatasetEntry(data);
            });
    }

    getSchemaById(datasetId) {
        console.log("...retrieving schema for dataset:", datasetId);
        return this.appStore.transportLayer
            .get(`/api/datasets/${datasetId}/schema`)
            .then(({ data }) => {
                console.log("Schema received: ", data);
                return data;
                //return new JsonSchema(data);
            });
    }

    getById(datasetId) {
        return this.appStore.transportLayer
            .get(`/api/datasets/${datasetId}`)
            .then(({ data }) => {
                return new DatasetEntry(data);
            });
    }

    delete(datasetId) {
        console.log('...deleting dataset ', datasetId);
        return this.appStore.transportLayer.delete(`/api/datasets/${datasetId}`).then(({ data }) => {
            console.log(`dataset ${datasetId} successfully deleted`);
            this.deletedItemId = datasetId;
            return data;
        });
    }
}
