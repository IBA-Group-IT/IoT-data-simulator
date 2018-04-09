
import React, { Component } from "react";
import { inject, observer } from "mobx-react";
import glamorous from "glamorous";
import { css } from 'glamor';

import CreateUpdateModal from 'components/CreateUpdateModal';
import SystemWizard from './SystemWizard';

@inject("store") @observer
class EditSystemModal extends Component { 
    constructor(props) {
        super(props);
    }

    onSubmit = () => {
        let { systemManagementStore, systemsStore, view } = this.props.store;
        let { entity } = systemManagementStore;
        let {id, ...rest} = entity.toJSON();

        systemsStore.update(id, rest).then(() => {
            systemsStore.getAll();
            view.closeEditSystem();
            systemManagementStore.reset();
        });
    }

    render() {
        let { store } = this.props;
        let { systemModalStore, editSystemStore, view } = store;

        return (
            <CreateUpdateModal
                title={'Update target system'}
                open={systemModalStore.isEditModalOpen}
                onClose={view.closeEditSystem}
            >
                <SystemWizard store={editSystemStore} onComplete={this.onSubmit} />
            </CreateUpdateModal>
        )
    }
}

export default EditSystemModal;
