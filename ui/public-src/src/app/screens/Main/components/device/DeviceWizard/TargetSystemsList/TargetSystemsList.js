
import React, { Component } from "react";
import { inject, observer } from "mobx-react";
import glamorous from "glamorous";
import SystemsList from '../../../system/SystemsList';


@observer
class WizardSystemsList extends Component { 

    constructor(props) {
        super(props);
    }

    onDelete = (system) => {
        let { view } = this.props.store.appStore;
        view.openDeleteDeviceSystem(system);
    }

    onEdit = (system) => {
        let { view } = this.props.store.appStore;
        view.openEditDeviceSystem(system);
    }

    render() {
        let { store } = this.props;
        let { device } = store;
        
        return (
            <SystemsList
                items={device.targetSystems}
                onEdit={this.onEdit}
                onDelete={this.onDelete}
                width={'260px'}
                height={'220px'}
            />
        )
    }
}


export default WizardSystemsList;
