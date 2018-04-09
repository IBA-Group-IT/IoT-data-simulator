
import React, { Component } from "react";
import { inject, observer } from "mobx-react";
import glamorous from "glamorous";
import { css } from 'glamor';

import DeleteModal from 'components/DeleteModal';

@inject("store") @observer
class DeleteDefinitionModal extends Component { 
    constructor(props) {
        super(props);
    }

    onDelete = () => {
        let {
            definitionModalStore, 
            definitionManagementStore,
            definitionsStore,
            devicesStore,
            view
        } = this.props.store;
        
        let { deletingEntity } = definitionManagementStore;
        
        definitionsStore.delete(deletingEntity.id)
            .then(() => {
                view.closeDeleteDefinition();
                definitionsStore.getAll();
                devicesStore.getAll();
            });
    }

    render() {
        let { store } = this.props;
        let { definitionModalStore, definitionManagementStore, view } = store;
        let { deletingEntity } = definitionManagementStore;

        return (
             <DeleteModal
                open={definitionModalStore.isDeleteModalOpen}
                title={'Delete definition'}
                onClose={view.closeDeleteDefinition}
                onDelete={this.onDelete}
            >
                <p>
                    <span>Are you sure you would like to delete definition</span>
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

export default DeleteDefinitionModal;
