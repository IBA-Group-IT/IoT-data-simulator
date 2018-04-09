import React, { Component } from "react";
import { inject, observer } from "mobx-react";
import glamorous from "glamorous";

import CreateUpdateModal from 'components/CreateUpdateModal';
import SystemWizard from '../system/SystemWizard';
import targetSystemFactory from 'models/system/factory';

@inject("store")
@observer
class EditDeviceSystemModal extends Component {

    onSubmit = () => {
        let { store } = this.props;
        let { deviceSystemModalStore, deviceManagementStore, deviceSystemManagementStore, view } = store;

        let device = deviceManagementStore.entity;
        let system = deviceSystemManagementStore.entity;
        device.updateTargetSystem(system);
        deviceSystemManagementStore.reset();

        view.closeEditDeviceSystem();
    }

    render() {
        let { store } = this.props;
        let { deviceSystemModalStore, deviceSystemEditStore, view } = store;

        return (
            <CreateUpdateModal
                title={'Update device target system'}
                open={deviceSystemModalStore.isEditModalOpen}
                onClose={view.closeEditDeviceSystem}
            >
                <SystemWizard store={deviceSystemEditStore} onComplete={this.onSubmit} />
            </CreateUpdateModal>
        )
    }
}

export default EditDeviceSystemModal;

