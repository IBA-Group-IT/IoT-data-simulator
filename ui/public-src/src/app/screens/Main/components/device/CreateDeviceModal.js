import React, { Component } from "react";
import { inject, observer } from "mobx-react";
import glamorous from "glamorous";

import CreateUpdateModal from 'components/CreateUpdateModal';
import DeviceWizard from './DeviceWizard';

@inject("store")
@observer
class CreateDeviceModal extends Component {

    constructor(props) {
        super(props);
    }

    onSubmit = () => {
        let { store } = this.props;
        let { view, deviceManagementStore, devicesStore, createDeviceStore } = store;
        let device = deviceManagementStore.entity;
        let { id, ...params } = device.toJSON();
        devicesStore.update(id, params).then(() =>{ 
            createDeviceStore.reset();
            createDeviceStore.load();
            view.closeCreateDevice();
        })
    }

    render() {
        let { store } = this.props;
        let { view, deviceModalStore, createDeviceStore, deviceManagementStore } = store;

        return (
            <CreateUpdateModal
                title={'Create device'}
                open={deviceModalStore.isCreateModalOpen}
                onClose={view.closeCreateDevice}
                onCancel={() => {
                    view.closeCreateDevice();
                    deviceManagementStore.reset();
                    createDeviceStore.reset();
                }}
            >
                <DeviceWizard store={createDeviceStore} onComplete={this.onSubmit} />
            </CreateUpdateModal>
        );
    }
}

export default CreateDeviceModal;
