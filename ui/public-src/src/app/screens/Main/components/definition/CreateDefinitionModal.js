import React, { Component } from "react";
import { inject, observer } from "mobx-react";
import glamorous from "glamorous";

import CreateUpdateModal from "components/CreateUpdateModal";
import DefinitionWizard from "./DefinitionWizard";

@inject("store")
@observer
class CreateDefinitionModal extends Component {
    constructor(props) {
        super(props);
    }

    render() {
        let { store } = this.props;
        let {
            view,
            definitionModalStore,
            createDefinitionStore,
            definitionManagementStore
        } = store;

        return (
            <CreateUpdateModal
                title={"Create definition"}
                open={definitionModalStore.isCreateModalOpen}
                onClose={view.closeCreateDefinition}
                onCancel={() => {
                    view.closeCreateDefinition();
                    //createDefinitionStore.reset();
                    definitionManagementStore.reset();
                }}
            >
                {definitionModalStore.isCreateModalOpen && (
                    <DefinitionWizard
                        store={createDefinitionStore}
                        onComplete={view.closeCreateDefinition}
                    />
                )}
            </CreateUpdateModal>
        );
    }
}

export default CreateDefinitionModal;
