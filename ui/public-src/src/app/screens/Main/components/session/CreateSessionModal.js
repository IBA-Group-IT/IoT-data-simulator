import React, { Component } from "react";
import { inject, observer } from "mobx-react";
import glamorous from "glamorous";

import CreateUpdateModal from 'components/CreateUpdateModal';
import SessionWizard from './SessionWizard';

@inject("store")
@observer
class CreateSessionModal extends Component {

    constructor(props) {
        super(props);
    }

    onSubmit = () => {
    }

    render() {
        let { store } = this.props;
        let { sessionModalStore, view, createSessionStore, sessionsManagementStore } = store;
        
        return (
            <CreateUpdateModal
                title={'Create session'}
                open={sessionModalStore.isCreateModalOpen}
                onClose={view.closeCreateSession}
                onCancel={() => {
                    view.closeCreateSession();
                    sessionsManagementStore.reset();
                }}
            >
                <SessionWizard store={createSessionStore} onComplete={view.closeCreateSession} />
            </CreateUpdateModal>
        );
    }
}

export default CreateSessionModal;

