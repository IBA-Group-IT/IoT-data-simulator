
import React, { Component } from "react";
import { inject, observer } from "mobx-react";
import glamorous from "glamorous";
import { css } from 'glamor';

import DeleteModal from 'components/DeleteModal';

@inject("store") @observer
class DeleteDatasetModal extends Component { 

    constructor(props) {
        super(props);
    }

    onDelete = () => {
        let {
            datasetModalStore, 
            datasetManagementStore,
            datasetsStore,
            view
        } = this.props.store;
        
        let { deletingEntity } = datasetManagementStore;
        
        datasetsStore.delete(deletingEntity.id)
            .then(() => {
                view.closeDeleteDataset();
                datasetsStore.getAll();
            });
    }

    render() {
        let { store } = this.props;
        let { datasetModalStore, datasetManagementStore, view } = store;
        let { deletingEntity } = datasetManagementStore;

        return (
             <DeleteModal
                open={datasetModalStore.isDeleteModalOpen}
                title={'Delete dataset'}
                onClose={view.closeDeleteDataset}
                onDelete={this.onDelete}
            >
                <p>
                    <span>Are you sure you would like to delete dataset</span>
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

export default DeleteDatasetModal;
