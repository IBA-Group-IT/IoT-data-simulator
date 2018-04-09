import React, { Component } from "react";
import { inject, observer } from "mobx-react";
import glamorous from "glamorous";

import CreateUpdateModal from 'components/CreateUpdateModal';
import SystemWizard from './SystemWizard';


@observer
class CreateSystemModal extends Component {

    render() {
        let { open, onClose, children, onCancel } = this.props;
        return (
            <CreateUpdateModal
                title={'Create target system'}
                open={open}
                onClose={onClose}
                onCancel={onCancel}
            >
                {this.props.children}
            </CreateUpdateModal>
        )
    }

}


@inject("store")
@observer
class CreateSystemModalContainer extends Component {

    constructor(props) {
        super(props);
    }

    onSubmit = () => {
        let { systemManagementStore, systemsStore, view } = this.props.store;
        let { entity } = systemManagementStore;
        systemsStore.create({ ...entity.toJSON() }).then(() => {
            systemsStore.getAll();
            systemManagementStore.reset();
            view.closeCreateSystem();
        });
    }

    render() {
        let { store } = this.props;
        let { systemModalStore, createSystemStore, systemManagementStore, view } = store;
        
        return (
            <CreateSystemModal
                open={systemModalStore.isCreateModalOpen}
                onClose={view.closeCreateSystem}
                onCancel={() => {
                    view.closeCreateSystem();
                    systemManagementStore.reset();
                }}
            >
                <SystemWizard store={createSystemStore} onComplete={this.onSubmit} />
            </CreateSystemModal>
        );
    }
}

export default CreateSystemModalContainer;

