import React, { Component } from "react";
import { inject, observer } from "mobx-react";
import glamorous from "glamorous";

import CreateUpdateModal from "components/CreateUpdateModal";
import SystemWizard from "../system/SystemWizard";
import targetSystemFactory from 'models/system/factory';

@inject("store")
@observer
class CreateDeviceSystemModal extends Component {

    onSubmit = () => {
        let { deviceManagementStore, deviceSystemManagementStore, devicesStore, view } = this.props.store;
        
        let device = deviceManagementStore.entity;
        let system = deviceSystemManagementStore.entity;

        device.addTargetSystem(system);
        view.closeCreateDeviceSystem();
        deviceSystemManagementStore.reset();
    };

    render() {
        let { store } = this.props;
        let {
            deviceSystemModalStore,
            deviceSystemCreateStore,
            deviceSystemManagementStore,
            view
        } = store;

        return (
            <CreateUpdateModal
                title={"Create device target system"}
                open={deviceSystemModalStore.isCreateModalOpen}
                onClose={view.closeCreateDeviceSystem}
                onCancel={() => {
                    view.closeCreateDeviceSystem();
                    deviceSystemManagementStore.reset();
                }}
            >
                <SystemWizard
                    store={deviceSystemCreateStore}
                    onComplete={this.onSubmit}
                />
            </CreateUpdateModal>
        );
    }
}

export default CreateDeviceSystemModal;
