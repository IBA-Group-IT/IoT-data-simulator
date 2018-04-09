import React, { Component } from "react";
import { observable, action } from "mobx";
import { inject, observer } from "mobx-react";
import glamorous from "glamorous";

import SchemaNode from "./SchemaNode";
import JsonSchemaProperty from "models/schema/JsonSchemaProperty";

import Button from "material-ui/Button";
import Input from "material-ui/Input";
import { MenuItem } from "material-ui/Menu";
import { FormControl, FormHelperText } from "material-ui/Form";
import Select from "material-ui/Select";
import Dialog, {
    DialogActions,
    DialogContent,
    DialogTitle
} from "material-ui/Dialog";

const SchemaFormComponent = glamorous.div({
    marginTop: "15px"
});

const ControlsContainer = glamorous.div({
    display: "flex",
    alignItems: "center",
    justifyContent: "space-between",
    paddingLeft: "35px"
});

const CopyFromButtonLayout = glamorous.div({});
const CopyFromButton = glamorous(Button, {
    withProps: {
        style: {
            fontSize: "13px",
            background: "rgb(230, 230, 230)"
        }
    }
})();

@observer
export default class SchemaConstructor extends Component {
    @observable isConfirmShown = false;

    @action.bound
    openConfirm = () => {
        this.isConfirmShown = true;
    };

    @action.bound
    closeConfirm = () => {
        this.isConfirmShown = false;
    };

    @action.bound
    onConfirm = () => {
        this.closeConfirm();
        this.props.store.openSchemaSelectModal();
    };

    constructor(props) {
        super(props);
    }

    handleFormatChange = e => {
        let { schemaConstructorStore } = this.props.store;
        let { schema } = schemaConstructorStore;
        schema.setSchemaType(e.target.value);
    };

    handleTypeChange = e => {
        let { schemaConstructorStore } = this.props.store;
        let { schema } = schemaConstructorStore;
        schema.setType(e.target.value);
    };

    render() {
        let {
            schemaConstructorStore,
            definition,
            openSchemaSelectModal
        } = this.props.store;

        let { schema, form, isRuleMode } = schemaConstructorStore;

        return (
            <SchemaFormComponent>
                <ControlsContainer>
                    <div>
                        <FormControl>
                            <Select
                                value={schema.schemaType}
                                onChange={this.handleFormatChange}
                                input={<Input id="schema-format" />}
                                disabled={definition.hasDataset}
                            >
                                <MenuItem value={"csv"}>csv</MenuItem>
                                <MenuItem value={"json"}>json</MenuItem>
                            </Select>
                        </FormControl>

                        <FormControl>
                            <Select
                                style={{
                                    marginLeft: "10px",
                                    display:
                                        schema.schemaType === "csv"
                                            ? "none"
                                            : "inline-block"
                                }}
                                value={schema.type}
                                onChange={this.handleTypeChange}
                                input={<Input id="schema-type" />}
                                disabled={definition.hasDataset}
                            >
                                <MenuItem value={"object"}>object</MenuItem>
                                <MenuItem value={"array"}>array</MenuItem>
                            </Select>
                        </FormControl>
                    </div>
                    {!definition.id && (
                        <CopyFromButtonLayout>
                            <CopyFromButton onClick={this.openConfirm}>
                                Copy from definition
                            </CopyFromButton>
                        </CopyFromButtonLayout>
                    )}
                </ControlsContainer>

                {!definition.id && (
                    <Dialog
                        ignoreBackdropClick
                        ignoreEscapeKeyUp
                        open={this.isConfirmShown}
                    >
                        <DialogTitle>Warning</DialogTitle>
                        <DialogContent>
                            When definition selected, all your current schema
                            changes will be lost. Do you want to proceed?
                        </DialogContent>
                        <DialogActions>
                            <Button onClick={this.closeConfirm} color="primary">
                                No
                            </Button>
                            <Button onClick={this.onConfirm} color="accent">
                                Yes
                            </Button>
                        </DialogActions>
                    </Dialog>
                )}

                <SchemaNode form={form} isRoot={true} />
            </SchemaFormComponent>
        );
    }
}
