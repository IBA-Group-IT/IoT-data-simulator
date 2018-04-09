
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
        let { deviceManagementStore, deviceModalStore, devicesStore, view } = this.props.store;
        let { deletingEntity } = deviceManagementStore;

        devicesStore.delete(deletingEntity.id)
            .then(() => {
                deviceModalStore.closeDelete();
                view.closeDeleteDevice();
                devicesStore.getAll();
            });
    }

    render() {
        let { store } = this.props;
        let { deviceManagementStore, deviceModalStore, view } = store;
        let { deletingEntity } = deviceManagementStore;

        return (
             <DeleteModal
                open={deviceModalStore.isDeleteModalOpen}
                title={'Delete device'}
                onClose={view.closeDeleteDevice}
                onDelete={this.onDelete}
            >
                <p>
                    <span>Are you sure you would like to delete device</span>
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
