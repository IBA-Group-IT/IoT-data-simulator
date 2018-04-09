import React, { Component } from "react";
import { inject, observer } from "mobx-react";

import BaseForm from "./formInstance";
import Fields from "./Fields";

@inject("store")
class BucketFormComponent extends Component {
    constructor(props) {
        super(props);
        let options = {
            fields: {
                url: {
                    label: 'URL',
                    placeholder: "Insert URL",
                    rules: "required|string",
                    bindings: "MaterialTextField",
                },
                bucket: {
                    label: 'Bucket',
                    placeholder: "Insert bucket",
                    rules: "required|string",
                    bindings: "MaterialTextField"
                },
                objectKey: {
                    label: 'Object key',
                    placeholder: "Insert object key",
                    rules: "required|string",
                    bindings: "MaterialTextField"
                },
                secretKey: {
                    label: 'Secret key',
                    placeholder: "Insert secret key",
                    rules: "required|string",
                    bindings: "MaterialTextField"
                },
                accessKey: {
                    label: 'Access key',
                    placeholder: "Insert access key",
                    rules: "required|string",
                    bindings: "MaterialTextField"
                },
                name: {
                    label: 'Name',
                    placeholder: "Insert name",
                    rules: "required|string",
                    bindings: "MaterialTextField"
                }
            }
        };

        Object.keys(options.fields).forEach((fieldKey) => {
            options.fields[fieldKey].hooks = {
                onChange: this.handleChange.bind(this)
            }
        });

        this.form = new BaseForm(options)
    }

    componentDidMount() { 
        let { definition } = this.props.store;
        let { dataset } = definition;
        let { bucketOptions, ...rest} = dataset;

        if(dataset && dataset.bucketOptions) {
            this.form.set('value', {
                ...bucketOptions,
                ...rest
            });
        }
         if(dataset && dataset.bucketOptions) {
            this.form.set('value', {
                ...bucketOptions,
                ...rest
            });
        }
    }

    onSuccess = () => { 
        let { definition } = this.props.store;
        let { dataset } = definition;
        let { name, ...rest} = this.form.values();

        console.log('setting bucket options', this.form.values())
        dataset.setName(name);
        dataset.setBucketOptions(rest);
    }

    handleChange(field, e) {
        //field.set('value', e.target.value);
        console.log('validate form');
        this.form.validate({ showErrors: false }).then((form) => {
            console.log('form is valid:', form.isValid)
            if(form.isValid) {
                form.submit({
                    onSuccess: this.onSuccess
                });
            }   
        })
    }

    render() {
        return (
            <Fields form={this.form} />
        );
    }
}

export default BucketFormComponent;
