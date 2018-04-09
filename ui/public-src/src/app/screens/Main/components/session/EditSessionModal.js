
import React, { Component } from "react";
import { inject, observer } from "mobx-react";
import glamorous from "glamorous";
import { css } from 'glamor';

import CreateUpdateModal from 'components/CreateUpdateModal';
import SessionWizard from './SessionWizard';

@inject("store")
@observer
class EditSessionModal extends Component {

    constructor(props) {
        super(props);
    }

    render() {
        let { store } = this.props;
        let { sessionModalStore, editSessionStore, view } = store;

        return (
            <CreateUpdateModal
                title={'Update session'}
                open={sessionModalStore.isEditModalOpen}
                onClose={view.closeEditSession}
            >
                <SessionWizard store={editSessionStore} onComplete={view.closeEditSession} />
            </CreateUpdateModal>
        );
    }
}

export default EditSessionModal;

