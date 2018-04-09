import React, { Component } from "react";
import { observable, action } from 'mobx';
import { inject, observer } from "mobx-react";
import glamorous from "glamorous";

import Property from './Constructor/Property';

const LoadingMessage = glamorous.p({
    color: '#212121',
    fontWeight: 'bold',
    fontSize: '14px'
});

@observer
export default class SchemaStepComponent extends React.Component {

    constructor(props) { 
        super(props);
    }
    
    render() {
        let { store } = this.props;
        let { definition } = store;

        return (
            <div>

                {
                    store.schemaConstructorStore.displaySchema && (
                        <Property value={store.schemaConstructorStore.displaySchema} />
                    )
                }

            </div>
        )
    }
}
