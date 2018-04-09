
import React, { Component } from "react";
import { inject, observer } from "mobx-react";
import glamorous from "glamorous";
import { css } from 'glamor';

import DeleteModal from 'components/DeleteModal';

@inject("store") @observer
class DeleteSystemModal extends Component { 
    constructor(props) {
        super(props);
    }

    onDelete = () => {
        let { systemModalStore, systemManagementStore, systemsStore, view } = this.props.store;
        let { deletingEntity } = systemManagementStore;
        
        systemsStore.delete(deletingEntity.id)
            .then(() => {
                view.closeDeleteSystem();
                systemsStore.getAll();
            });
    }

    render() {
        let { store } = this.props;
        let { systemModalStore, systemManagementStore, view } = store;
        let { deletingEntity } = systemManagementStore;

        return (
            <DeleteModal
                open={systemModalStore.isDeleteModalOpen}
                title={'Delete target system'}
                onClose={view.closeDeleteSystem}
                onDelete={this.onDelete}
            >
                <p>
                    <span>Are you sure you would like to delete target system</span>
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

export default DeleteSystemModal;
