import React, { Component } from "react";
import { computed, action, obervable } from "mobx";
import { inject, observer } from "mobx-react";
import glamorous from "glamorous";

import ViewLayout from "components/ViewLayout";
import DefinitionControlPanel from "../../ControlPanel";
import CreateUpdateModal from "components/CreateUpdateModal";
import DefinitionsList from "../../DefinitionsList";

import Button from "material-ui/Button";

const SubmitButtonLayout = glamorous.div({
    padding: "15px",
    height: "70px",
    height: "60px",
    display: "flex",
    alignItems: "center",
    justifyContent: "flex-end",
    flex: "0 0 60px"
});

const SubmitButton = glamorous(Button)({});

const InnerContainer = glamorous.div({
    display: "flex",
    flexDirection: "column",
    flex: "0 0 auto",
    width: "100%"
});

const EmptyMessage = glamorous.p({
    padding: '0 2em'
});

@observer
class SelectSchemaModal extends Component {
    constructor(props) {
        super(props);
    }

    render() {
        let { store } = this.props;

        let {
            appStore,
            selectedDefinitionToCopy,
            selectDefinitionToCopySchema,
            submitDefinitionToCopySchema,
            definition
        } = store;

        let schemaType = store.schemaConstructorStore.schema.schemaType;
        
        let { definitionsStore } = appStore;
        let items = definitionsStore.getItemsBySchemaType(
            definitionsStore.items,
            schemaType
        );

        return (
            <CreateUpdateModal
                title={"Select data definiton to copy schema from"}
                open={store.isSelectSchemaModalOpen}
                onClose={store.closeSchemaSelectModal}
            >
                <InnerContainer>
                    <SubmitButtonLayout>
                        <SubmitButton
                            disabled={!selectedDefinitionToCopy}
                            onClick={submitDefinitionToCopySchema}
                        >
                            Proceed
                        </SubmitButton>
                    </SubmitButtonLayout>

                    {items.length !== 0 && (
                        <DefinitionsList
                            items={items}
                            hideControls={true}
                            selectable={true}
                            selectedIds={[
                                selectedDefinitionToCopy && selectedDefinitionToCopy.id
                            ]}
                            onSelect={selectDefinitionToCopySchema}
                        />
                    )}

                    {!items.length && (
                        <EmptyMessage>
                            No definitions with <b>{schemaType}</b> schema type
                        </EmptyMessage>
                    )}
                </InnerContainer>
            </CreateUpdateModal>
        );
    }
}

export default SelectSchemaModal;
