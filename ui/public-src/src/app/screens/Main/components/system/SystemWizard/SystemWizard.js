import React, { Component } from "react";
import { inject, observer } from "mobx-react";
import glamorous from "glamorous";

import Dialog, {
    DialogActions,
    DialogContent,
    DialogTitle
} from "material-ui/Dialog";

import AceEditor from "react-ace";
import "brace/mode/javascript";
import "brace/theme/monokai";

import MaterialTextField from "components/Form/inputs/MaterialTextField";
import MaterialSelect from "components/Form/inputs/MaterialSelect";
import MaterialFile from "components/Form/inputs/MaterialFile";

import Button from "material-ui/Button";
import IconButton from "material-ui/IconButton";

import DeleteIcon from "components/Icons/DeleteIcon";
import AdvancedLink from 'components/AdvancedLink'

const CreateEditSystemPanelLayout = glamorous.div({
    width: "100%",
    display: "flex",
    flexDirection: "column",
    overflow: "hidden"
});

const InputLayout = glamorous.div({
    marginTop: "10px"
});

const Section = glamorous.div({
    marginTop: "10px",
    padding: "10px 30px"
});

const SectionTitle = glamorous.div({
    fontSize: "15px",
    fontWeight: "bold"
});

const SectionContent = glamorous.div({
    marginTop: "10px",
    padding: "10px"
});

const InlineInput = glamorous.div({
    display: "inline-block",
    marginRight: "10px"
});

const AddPropertyButton = glamorous(Button, {
    withProps: {
        style: {
            width: "242px",
            margin: "13px",
            background: "#e6e6e6"
        }
    }
})({});

const SubmitButtonLayout = glamorous.div({
    padding: "15px",
    height: "70px",
    height: "60px",
    display: "flex",
    alignItems: "center",
    justifyContent: "flex-end",
    flex: "0 0 60px"
});

const PanelInnerContainer = glamorous.div({
    display: "flex",
    flexDirection: "column",
    minHeight: "0",
    height: "100%",
    overflow: "auto"
});

const TextField = glamorous(MaterialTextField, {
    withProps: {
        style: {
            minWidth: "250px"
        }
    }
})({});
const SelectField = glamorous(MaterialSelect)({
    minWidth: "125px"
});

const EditorButton = glamorous(Button, {
    withProps: {
        style: { background: "rgb(230, 230, 230)" }
    }
})({});

const SubmitButton = glamorous(Button)({});

const ProtoTypeField = glamorous(TextField, {
    withProps: {
        style: { minWidth: "375px" }
    }
})({});

@observer
export default class SystemWizard extends Component {
    //TODO move to form utils
    getFieldComponent(type) {
        if (type === "text") {
            return TextField;
        } else if (type === "select") {
            return SelectField;
        }
    }

    renderInput(fieldName) {
        let { store } = this.props;
        let { systemForm } = store;

        let input = null;

        if (systemForm.has(fieldName)) {
            let $field = systemForm.$(fieldName);
            let binding = $field.$bindings;
            let FieldComponent = this.getFieldComponent($field.type);
            input = <FieldComponent field={$field} />;
        }

        if (input) {
            return <InputLayout>{input}</InputLayout>;
        }

        return null;
    }

    render() {
        let { store, onComplete } = this.props;
        let { systemForm, formIsValid } = store;
        let $securityForm =
            systemForm.has("security") && systemForm.$("security");
        let $headersForm = systemForm.has("headers") && systemForm.$("headers");

        return (
            <CreateEditSystemPanelLayout>
                <SubmitButtonLayout>
                    <SubmitButton disabled={!formIsValid} onClick={onComplete}>
                        Proceed
                    </SubmitButton>
                </SubmitButtonLayout>
                <PanelInnerContainer>
                    <Section>
                        <SectionTitle>1. Populate name</SectionTitle>
                        <SectionContent>
                            {this.renderInput("name")}
                        </SectionContent>
                    </Section>
                    <Section>
                        <SectionTitle>2. Populate target fields</SectionTitle>
                        <SectionContent>
                            {this.renderInput("type")}
                            {systemForm.has("url") && (
                                <InputLayout>
                                    <TextField field={systemForm.$("url")} />
                                </InputLayout>
                            )}
                            {this.renderInput("method")}
                            {this.renderInput("topic")}
                            {this.renderInput("bucket")}
                            {this.renderInput("dataset")}
                            {this.renderInput("queue")}

                            {systemForm.has("keyFunctionButton") && (
                                <EditorButton
                                    key={systemForm.$("keyFunctionButton").key}
                                    onClick={
                                        systemForm.$("keyFunctionButton").$hooks
                                            .onClick
                                    }
                                >
                                    {systemForm.$("keyFunctionButton").label}
                                </EditorButton>
                            )}

                            <Dialog
                                maxWidth="md"
                                fullWidth={true}
                                open={store.formStore.isKeyFunctionOpen}
                            >
                                <DialogTitle>Edit key function</DialogTitle>
                                <DialogContent>
                                    <AceEditor
                                        mode="javascript"
                                        theme="monokai"
                                        width="100%"
                                        value={
                                            store.formStore.system.keyFunction
                                        }
                                        name="editor"
                                        onChange={
                                            store.formStore.onKeyFunctionChange
                                        }
                                        editorProps={{
                                            $blockScrolling: true
                                        }}
                                    />
                                </DialogContent>
                                <DialogActions>
                                    <Button
                                        color="accent"
                                        onClick={
                                            store.formStore.onKeyFunctionCancel
                                        }
                                    >
                                        Cancel
                                    </Button>
                                    <Button
                                        color="primary"
                                        onClick={
                                            store.formStore.onKeyFunctionApply
                                        }
                                    >
                                        Save
                                    </Button>
                                </DialogActions>
                            </Dialog>
                        </SectionContent>
                    </Section>

                    {$securityForm && (
                        <Section>
                            <SectionTitle>
                                3. Select security options
                            </SectionTitle>
                            <SectionContent>
                                {$securityForm.has("type") && (
                                    <SelectField
                                        field={$securityForm.$("type")}
                                    />
                                )}
                                {$securityForm.has("username") && (
                                    <TextField
                                        field={$securityForm.$("username")}
                                    />
                                )}
                                {$securityForm.has("password") && (
                                    <TextField
                                        type="password"
                                        field={$securityForm.$("password")}
                                    />
                                )}
                                {$securityForm.has("token") && (
                                    <TextField
                                        field={$securityForm.$("token")}
                                        type="password"
                                    />
                                )}
                                {$securityForm.has("authHost") && (
                                    <TextField
                                        field={$securityForm.$("authHost")}
                                    />
                                )}
                                {$securityForm.has("apiKey") && (
                                    <TextField
                                        type="password"
                                        field={$securityForm.$("apiKey")}
                                    />
                                )}
                                {$securityForm.has("restKey") && (
                                    <TextField
                                        type="password"
                                        field={$securityForm.$("restKey")}
                                    />
                                )}
                                {$securityForm.has("tenant") && (
                                    <TextField
                                        field={$securityForm.$("tenant")}
                                    />
                                )}

                                {$securityForm.has("ca") &&
                                    $securityForm.has("caFile") && (
                                        <div>
                                            <InlineInput>
                                                <TextField
                                                    field={$securityForm.$(
                                                        "ca"
                                                    )}
                                                />
                                            </InlineInput>
                                            <InlineInput>
                                                <MaterialFile
                                                    field={$securityForm.$(
                                                        "caFile"
                                                    )}
                                                />
                                            </InlineInput>
                                        </div>
                                    )}
                                {$securityForm.has("deviceCertificate") &&
                                    $securityForm.has(
                                        "deviceCertificateFile"
                                    ) && (
                                        <div>
                                            <InlineInput>
                                                <TextField
                                                    field={$securityForm.$(
                                                        "deviceCertificate"
                                                    )}
                                                />
                                            </InlineInput>
                                            <InlineInput>
                                                <MaterialFile
                                                    field={$securityForm.$(
                                                        "deviceCertificateFile"
                                                    )}
                                                />
                                            </InlineInput>
                                        </div>
                                    )}
                                {$securityForm.has("privateKey") &&
                                    $securityForm.has("privateKeyFile") && (
                                        <div>
                                            <InlineInput>
                                                <TextField
                                                    field={$securityForm.$(
                                                        "privateKey"
                                                    )}
                                                />
                                            </InlineInput>
                                            <InlineInput>
                                                <MaterialFile
                                                    field={$securityForm.$(
                                                        "privateKeyFile"
                                                    )}
                                                />
                                            </InlineInput>
                                        </div>
                                    )}
                                {$securityForm.has("accessKey") && (
                                    <TextField
                                        field={$securityForm.$("accessKey")}
                                    />
                                )}
                                {$securityForm.has("secretKey") && (
                                    <TextField
                                        field={$securityForm.$("secretKey")}
                                    />
                                )}
                            </SectionContent>
                        </Section>
                    )}

                    {$headersForm && (
                        <Section>
                            <SectionTitle>4. Add headers</SectionTitle>
                            <SectionContent>
                                <AddPropertyButton onClick={$headersForm.onAdd}>
                                    Add header
                                </AddPropertyButton>
                                {$headersForm.map(headerField => {
                                    let keyField = headerField.$("headerKey");
                                    let valueField = headerField.$(
                                        "headerValue"
                                    );
                                    return (
                                        <div key={headerField.key}>
                                            <InlineInput>
                                                <TextField
                                                    key={keyField.key}
                                                    field={keyField}
                                                />
                                            </InlineInput>
                                            <InlineInput>
                                                <TextField
                                                    key={valueField.key}
                                                    field={valueField}
                                                />
                                            </InlineInput>
                                            <InlineInput>
                                                <IconButton
                                                    onClick={() => {
                                                        headerField.del();
                                                        headerField.$hooks.onDel(
                                                            headerField
                                                        );
                                                    }}
                                                >
                                                    <DeleteIcon />
                                                </IconButton>
                                            </InlineInput>
                                        </div>
                                    );
                                })}
                            </SectionContent>
                        </Section>
                    )}

                    {systemForm.has("messageSerializer") &&
                        systemForm.$("messageSerializer").has("type") && (
                            <Section>
                                <SectionTitle>
                                    <AdvancedLink
                                        onClick={() =>
                                            store.formStore.toggleAdvancedOptions(
                                                !store.formStore
                                                    .isAdvancedOptionsOpen
                                            )
                                        }
                                        isExpanded={
                                            store.formStore
                                                .isAdvancedOptionsOpen
                                        }
                                    >
                                        <span>Advanced options</span>
                                    </AdvancedLink>
                                </SectionTitle>

                                {store.formStore.isAdvancedOptionsOpen && (
                                    <SectionContent>
                                        <p>Message serializer</p>

                                        {
                                            <SelectField
                                                field={systemForm
                                                    .$("messageSerializer")
                                                    .$("type")}
                                            />
                                        }

                                        {systemForm
                                            .$("messageSerializer")
                                            .has("protoDescriptor") && (
                                            <div>
                                                <InlineInput>
                                                    <MaterialFile
                                                        field={systemForm
                                                            .$(
                                                                "messageSerializer"
                                                            )
                                                            .$(
                                                                "protoDescriptorFile"
                                                            )}
                                                    />
                                                </InlineInput>
                                                <InlineInput>
                                                    <TextField
                                                        field={systemForm
                                                            .$(
                                                                "messageSerializer"
                                                            )
                                                            .$(
                                                                "protoDescriptor"
                                                            )}
                                                    />
                                                </InlineInput>
                                            </div>
                                        )}

                                        {systemForm
                                            .$("messageSerializer")
                                            .has("protoType") && (
                                            <div>
                                                <InlineInput>
                                                    <ProtoTypeField
                                                        field={systemForm
                                                            .$(
                                                                "messageSerializer"
                                                            )
                                                            .$("protoType")}
                                                    />
                                                </InlineInput>
                                            </div>
                                        )}

                                        {systemForm
                                            .$("messageSerializer")
                                            .has("jsBuilder") && (
                                            <div>
                                                <InlineInput>
                                                    <EditorButton
                                                        onClick={
                                                            store.formStore
                                                                .openMessageSerializerEditor
                                                        }
                                                    >
                                                        type builder
                                                    </EditorButton>

                                                    <Dialog
                                                        maxWidth="md"
                                                        fullWidth={true}
                                                        open={
                                                            store.formStore
                                                                .isMessageSerializerEditorOpen
                                                        }
                                                    >
                                                        <DialogTitle>
                                                            Edit type builder
                                                            function
                                                        </DialogTitle>
                                                        <DialogContent>
                                                            <AceEditor
                                                                mode="javascript"
                                                                theme="monokai"
                                                                width="100%"
                                                                value={
                                                                    store
                                                                        .formStore
                                                                        .currentMessageSerializerEditorValue
                                                                }
                                                                name="editor"
                                                                onChange={
                                                                    store
                                                                        .formStore
                                                                        .onMessageSerializerEditorChange
                                                                }
                                                                editorProps={{
                                                                    $blockScrolling: true
                                                                }}
                                                            />
                                                        </DialogContent>
                                                        <DialogActions>
                                                            <Button
                                                                color="accent"
                                                                onClick={
                                                                    store
                                                                        .formStore
                                                                        .onMessageSerializerEditorCancel
                                                                }
                                                            >
                                                                Cancel
                                                            </Button>
                                                            <Button
                                                                color="primary"
                                                                onClick={
                                                                    store
                                                                        .formStore
                                                                        .onMessageSerializerEditorApply
                                                                }
                                                            >
                                                                Save
                                                            </Button>
                                                        </DialogActions>
                                                    </Dialog>
                                                </InlineInput>
                                            </div>
                                        )}

                                        <br />

                                        {systemForm.has("keySerializer") &&
                                            systemForm
                                                .$("keySerializer")
                                                .has("type") && (
                                                <div>
                                                    <p>Key serializer</p>

                                                    {
                                                        <SelectField
                                                            field={systemForm
                                                                .$(
                                                                    "keySerializer"
                                                                )
                                                                .$("type")}
                                                        />
                                                    }

                                                    {systemForm
                                                        .$("keySerializer")
                                                        .has(
                                                            "protoDescriptor"
                                                        ) && (
                                                        <div>
                                                            <InlineInput>
                                                                <MaterialFile
                                                                    field={systemForm
                                                                        .$(
                                                                            "keySerializer"
                                                                        )
                                                                        .$(
                                                                            "protoDescriptorFile"
                                                                        )}
                                                                />
                                                            </InlineInput>
                                                            <InlineInput>
                                                                <TextField
                                                                    field={systemForm
                                                                        .$(
                                                                            "keySerializer"
                                                                        )
                                                                        .$(
                                                                            "protoDescriptor"
                                                                        )}
                                                                />
                                                            </InlineInput>
                                                        </div>
                                                    )}

                                                    {systemForm
                                                        .$("keySerializer")
                                                        .has("protoType") && (
                                                        <div>
                                                            <InlineInput>
                                                                <ProtoTypeField
                                                                    field={systemForm
                                                                        .$(
                                                                            "keySerializer"
                                                                        )
                                                                        .$(
                                                                            "protoType"
                                                                        )}
                                                                />
                                                            </InlineInput>
                                                        </div>
                                                    )}

                                                    {systemForm
                                                        .$("keySerializer")
                                                        .has("jsBuilder") && (
                                                        <div>
                                                            <InlineInput>
                                                                <EditorButton
                                                                    onClick={
                                                                        store
                                                                            .formStore
                                                                            .openKeySerializerEditor
                                                                    }
                                                                >
                                                                    type builder
                                                                </EditorButton>

                                                                <Dialog
                                                                    maxWidth="md"
                                                                    fullWidth={
                                                                        true
                                                                    }
                                                                    open={
                                                                        store
                                                                            .formStore
                                                                            .isKeySerializerEditorOpen
                                                                    }
                                                                >
                                                                    <DialogTitle
                                                                    >
                                                                        Edit
                                                                        type
                                                                        builder
                                                                        function
                                                                    </DialogTitle>
                                                                    <DialogContent
                                                                    >
                                                                        <AceEditor
                                                                            mode="javascript"
                                                                            theme="monokai"
                                                                            width="100%"
                                                                            value={
                                                                                store
                                                                                    .formStore
                                                                                    .currentKeySerializerEditorValue
                                                                            }
                                                                            name="editor"
                                                                            onChange={
                                                                                store
                                                                                    .formStore
                                                                                    .onKeySerializerEditorChange
                                                                            }
                                                                            editorProps={{
                                                                                $blockScrolling: true
                                                                            }}
                                                                        />
                                                                    </DialogContent>
                                                                    <DialogActions
                                                                    >
                                                                        <Button
                                                                            color="accent"
                                                                            onClick={
                                                                                store
                                                                                    .formStore
                                                                                    .onKeySerializerEditorCancel
                                                                            }
                                                                        >
                                                                            Cancel
                                                                        </Button>
                                                                        <Button
                                                                            color="primary"
                                                                            onClick={
                                                                                store
                                                                                    .formStore
                                                                                    .onKeySerializerEditorApply
                                                                            }
                                                                        >
                                                                            Save
                                                                        </Button>
                                                                    </DialogActions>
                                                                </Dialog>
                                                            </InlineInput>
                                                        </div>
                                                    )}
                                                </div>
                                            )}
                                    </SectionContent>
                                )}
                            </Section>
                        )}
                </PanelInnerContainer>
            </CreateEditSystemPanelLayout>
        );
    }
}
