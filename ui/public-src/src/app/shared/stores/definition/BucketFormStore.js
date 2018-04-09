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

import { FormState, FieldState } from "formstate";

const isRequired = function(value) {
    if (typeof value === "undefined" || value === "" || value == null) {
        return "This field is required";
    }
};

export default class BucketFormStore {
    @observable.ref form;

    /**
     * converting plain object to formstate object
     * possible could be moved to utils function
     */
    buildForm(options) {
        let _options = Object.keys(options.values).reduce((obj, key) => {
            obj[key] = new FieldState(
                options.values[key]
            ).enableAutoValidation();

            let rules = options.rules[key];
            if (rules) {
                obj[key] = obj[key].validators.apply(obj[key], rules);
            }
            return obj;
        }, {});
        return new FormState(_options);
    }

    @computed
    get isValid() {
        let { form } = this;
        let errorFields = form
            .getValues()
            .filter(field => !field.hasBeenValidated || field.hasError);
        return !errorFields.length;
    }

    constructor(store) {
        this.store = store;

        /* convert definition data graph to form data graph 
         * with memoization
         * https://mobx.js.org/refguide/create-transformer.html
         */
        const convert = createTransformer(definition => {
            let isDefinitionWithBucket =
                definition.hasDataset && definition.dataset.hasBucketOptions;

            let fields = {
                url: "",
                bucket: "",
                objectKey: "",
                secretKey: "",
                accessKey: "",
                name: ""
            };

            let options = Object.keys(fields).reduce((obj, key) => {
                if (!obj.values) {
                    obj.values = {};
                }
                if (!obj.rules) {
                    obj.rules = {};
                }
                obj.values[key] = fields[key];
                obj.rules[key] = [isRequired];
                return obj;
            }, {});

            if (isDefinitionWithBucket) {
                let { dataset } = definition;
                Object.assign(options.values, {
                    url: dataset.bucketOptions.url,
                    bucket: dataset.bucketOptions.bucket,
                    objectKey: dataset.bucketOptions.objectKey,
                    secretKey: dataset.bucketOptions.secretKey,
                    accessKey: dataset.bucketOptions.accessKey,
                    name: dataset.name
                });
            }

            let form = this.buildForm(options);
            if (definition.id && isDefinitionWithBucket) {
                form.validate();
            }

            return form;
        });

        this.dispose = autorun(() => {
            this.form = convert(store.definition);
        });
    }

    destroy() {
        this.dispose();
    }
}