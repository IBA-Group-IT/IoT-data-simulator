import React, { Component } from "react";
import { inject, observer } from "mobx-react";

import Form from "./Form";

@observer
class BucketFormComponent extends Component {

    onSubmit = (e, form) => {}

    render() {
        let { store } = this.props;

        return (
            <Form form={store.bucketFormStore.form} onSubmit={this.onSubmit}/>
        );
    }
}

export default BucketFormComponent;
