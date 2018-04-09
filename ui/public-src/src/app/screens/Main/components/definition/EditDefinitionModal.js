
import React, { Component } from "react";
import { inject, observer } from "mobx-react";
import glamorous from "glamorous";
import { css } from 'glamor';

import CreateUpdateModal from 'components/CreateUpdateModal';
import DefinitionWizard from "./DefinitionWizard";

@inject("store") @observer
class EditDefinitionModal extends Component { 
    constructor(props) {
        super(props);
    }

    render() {
        let { store } = this.props;
        let { view, definitionModalStore, editDefinitionStore } = store;

        return (
            <CreateUpdateModal
                title={'Update definition'}
                open={definitionModalStore.isEditModalOpen}
                onClose={() => {
                    view.closeEditDefinition();
                    editDefinitionStore.reset();
                }}
            >
            {
                definitionModalStore.isEditModalOpen && (
                    <DefinitionWizard store={editDefinitionStore} onComplete={view.closeEditDefinition} />
                )
            }
                
            </CreateUpdateModal>
        )
    }
}

export default EditDefinitionModal;
