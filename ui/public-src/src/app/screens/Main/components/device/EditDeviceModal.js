
import React, { Component } from "react";
import { inject, observer } from "mobx-react";
import glamorous from "glamorous";
import { css } from 'glamor';

import CreateUpdateModal from 'components/CreateUpdateModal';
import DeviceWizard from './DeviceWizard';

@inject("store") @observer
class EditDeviceModal extends Component { 
    constructor(props) {
        super(props);
    }

    onSubmit = () => {
        let { store } = this.props;
        let { view, deviceManagementStore, editDeviceStore, devicesStore } = store;
        let device = deviceManagementStore.entity;
        let { id, ...params } = device.toJSON();
        devicesStore.update(id, params).then(() =>{ 
            editDeviceStore.reset();
            editDeviceStore.load();
            view.closeEditDevice();
        });
    }


    render() {
        let { store } = this.props;
        let { deviceModalStore, editDeviceStore, view } = store;

        return (
            <CreateUpdateModal
                title={'Update device'}
                open={deviceModalStore.isEditModalOpen}
                onClose={view.closeEditDevice}
            >
                <DeviceWizard store={editDeviceStore} onComplete={this.onSubmit} />
            </CreateUpdateModal>
        )
    }
}

export default EditDeviceModal;
