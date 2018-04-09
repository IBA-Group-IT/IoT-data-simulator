import React, { Component } from "react";
import { inject, observer } from "mobx-react";

import Form from "./Form";
import BaseForm from "components/Form";

@inject("store")
@observer
class FormComponent extends Component {

    componentDidMount() { 
        let { definition } = this.props;
    }

    render() {
        let { definition, finalizeForm } = this.props.store;

        return (
            <Form
                form={finalizeForm}
            />
        );
    }
}
export default FormComponent;
