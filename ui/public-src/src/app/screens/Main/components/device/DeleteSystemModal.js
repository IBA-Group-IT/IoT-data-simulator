
import React, { Component } from "react";
import { inject, observer } from "mobx-react";
import glamorous from "glamorous";
import { css } from 'glamor';

import DeleteModal from 'components/DeleteModal';

@inject("store") @observer
class DeleteDeviceModal extends Component { 
    constructor(props) {
        super(props);
    }

    onDelete = () => {
        let { deviceSystemManagementStore, deviceManagementStore, view } = this.props.store;
        let device = deviceManagementStore.entity;
        let system = deviceSystemManagementStore.deletingEntity;
        view.closeDeleteDeviceSystem();
        device.deleteTargetSystem(system);
    }

    render() {
        let { store } = this.props;
        let { deviceSystemManagementStore, deviceSystemModalStore, view } = store;
        let { deletingEntity } = deviceSystemManagementStore;

        return (
             <DeleteModal
                open={deviceSystemModalStore.isDeleteModalOpen}
                title={'Delete device'}
                onClose={view.closeDeleteDeviceSystem}
                onDelete={this.onDelete}
            >
                <p>
                    <span>Are you sure you would like to delete device system</span>
                    <b>
                        {
                            deletingEntity && ` ${deletingEntity.name}`
                        }
                    </b>
                    ? 
                </p>
            </DeleteModal>
        )
    }
}

export default DeleteDeviceModal;
