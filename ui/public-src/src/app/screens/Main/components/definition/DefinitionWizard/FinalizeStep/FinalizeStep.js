import React, { Component } from "react";
import { observer } from "mobx-react";
import glamorous from "glamorous";
import FinalizeForm from "./FinalizeForm";

@observer
export default class DefinitionWizard extends React.Component {


    
    render() {
        let { store } = this.props;
        return (
            <FinalizeForm store={store} />
        );
    }
}