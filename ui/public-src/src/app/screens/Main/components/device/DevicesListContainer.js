
import React, { Component } from "react";
import { inject, observer } from "mobx-react";
import glamorous from "glamorous";

import DevicesList from './DevicesList';

@inject("store") @observer
class DevicesScreenDevicesList extends Component { 

    constructor(props) {
        super(props);
    }

    onDelete = (device) => {
        let { view } = this.props.store;
        view.openDeleteDevice(device);
    }

    onEdit = (device) => {
        let { view } = this.props.store;
        view.openEditDevice(device.id);
    }

    render() {
        let { devicesStore } = this.props.store;
        let { selectable, onSelect, selectedIds } = this.props;

        return (
            <DevicesList
                items={devicesStore.filteredItems}
                onEdit={this.onEdit}
                onDelete={this.onDelete}
                width={'255px'}
                height={'135px'}
                selectable={selectable}
                selectedIds={selectedIds}
                onSelect={onSelect}
            />
        )
    }
}

export default DevicesScreenDevicesList;
