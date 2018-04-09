
import React, { Component } from "react";
import { inject, observer } from "mobx-react";
import glamorous from "glamorous";
import { css } from 'glamor';

import DeleteModal from 'components/DeleteModal';

@inject("store")
@observer
class DeleteSessionModal extends Component {

    constructor(props) {
        super(props);
    }

    onDelete = () => {
        let { store } = this.props;
        let { sessionModalStore, sessionsManagementStore, view, sessionsStore } = store;
        let { deletingEntity } = sessionsManagementStore;

        sessionsStore.delete(deletingEntity.id).then(() => {
            sessionsStore.getAll();
            view.closeDeleteSession();
        });
    }

    render() {
        let { store } = this.props;
        let { sessionModalStore, sessionsManagementStore, view, sessionsStore } = store;
        let { deletingEntity } = sessionsManagementStore;
        
        return (
            <DeleteModal
                open={sessionModalStore.isDeleteModalOpen}
                title={'Delete session'}
                onClose={view.closeDeleteSession}
                onDelete={this.onDelete}
            >
                <p>
                    <span>Are you sure you would like to delete session</span>
                    <b>
                        {
                            deletingEntity && ` ${deletingEntity.name}`
                        }
                    </b>
                    ? 
                </p>
            </DeleteModal>
        );
    }
}

export default DeleteSessionModal;

