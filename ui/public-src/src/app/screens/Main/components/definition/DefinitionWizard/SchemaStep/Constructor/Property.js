import React, { Component } from "react";
import { autorun } from "mobx";
import { observable, computed, action } from "mobx";
import { inject, observer } from "mobx-react";
import glamorous from "glamorous";

import Button from "material-ui/Button";
import TextField from "material-ui/TextField";
import Select from "material-ui/Select";
import Input, { InputLabel } from "material-ui/Input";
import IconButton from "material-ui/IconButton";
import DeleteIcon from "components/Icons/CrossIcon";

import BaseForm from "components/Form";
import MaterialTextField from "components/Form/inputs/MaterialTextField";
import MaterialSelect from "components/Form/inputs/MaterialSelect";
import { types } from "models/schema/JsonSchemaProperty";

import AceEditor from "react-ace";
import "brace/mode/javascript";
import "brace/theme/monokai";

import Dialog, {
    DialogActions,
    DialogContent,
    DialogTitle
} from "material-ui/Dialog";

import Field from "components/Form/Field";
import SelectField from "components/Form/SelectField";
import Editor from "components/Form/Editor";
import CollapseIcon from "components/Icons/ChevronDownIcon";

const SchemaNodeComponent = glamorous.div(
    {
        marginLeft: "36px",
        marginTop: "5px",
        transform: "translate3d(0, 0, 0) scale(1)"
    },
    ({ isCollapsed }) => ({
        borderLeft: isCollapsed
            ? "3px solid rgb(115, 115, 115)"
            : "3px solid #03a9f4"
    })
);

const NodeContent = glamorous.div({
    width: "100%",
    display: "flex",
    flexDirection: "column"
});

const NodeContainer = glamorous.div({
    display: "flex",
    flexDirection: "row",
    marginTop: "5px"
});

const ControlsLayout = glamorous.div({
    background: "white",
    padding: "4px 15px",
    display: "flex",
    justifyContent: "space-between",
    width: "100%",
    flexDirection: "row",
    alignSelf: "stretch",
    justifySelf: "stretch",
    flex: "1"
});

const InlineContainer = glamorous.div({
    display: "inline-block",
    verticalAlign: "bottom",
    marginRight: "10px",
    flex: "0 1 160px"
});

const ParametersPanel = glamorous.div({
    marginTop: "0px",
    width: "100%",
    display: "flex",
    flexWrap: "wrap",
    alignItems: "center",
    alignContent: "flex-start"
});

const ParameterTextField = glamorous(Field, {
    withProps: {
        style: {
            display: "inline-block"
        },
        inputProps: {
            style: { fontSize: "14px" }
        }
    }
})();

const ParameterSelectField = glamorous(SelectField, {
    withProps: {
        style: {
            width: "100%",
            fontSize: "14px"
        },
        fullWidth: true
    }
})({});

const AddPropertyButton = glamorous(Button, {
    withProps: {
        style: {
            margin: "0 0 0 10px",
            fontSize: "13px",
            background: "rgb(233, 233, 233)"
        }
    }
})({});

const DeleteButtonLayout = glamorous.div({
    display: "flex",
    position: "absolute",
    right: "5px",
    top: "5px",
    margin: "auto",
    alignItems: "center"
});

const Filled = glamorous.div({
    width: "100%",
    height: "100%"
});

const DeleteButton = glamorous(IconButton, {
    withProps: {
        style: {
            width: "20px",
            height: "20px",
            fontSize: "18px",
            color: "#ff5252"
        }
    }
})();

const RuleLayout = glamorous.div({
    height: "100%",
    background: "white",
    borderLeft: "1px dashed black",
    width: "100%"
});

const PropertyComponentContainer = glamorous.div({
    display: "flex",
    flexDirection: "row",
    width: "100%",
    height: "100%"
});

const Flex = glamorous.div({
    display: "flex"
});

const Error = glamorous.div({
    color: "red",
    fontSize: "13px",
    position: "absolute",
    bottom: "5px",
    right: '10px'
});

const PropertyLayoutContainer = glamorous.div({
    width: "100%",
    height: "100%",
    display: "flex",
    flexDirection: "column"
});

const EditorButton = glamorous(Button, {
    withProps: {
        style: {
            background: "rgb(233, 233, 233)"
        }
    }
})();

const CollapseButton = glamorous(IconButton, {
    withProps: ({ isCollapsed }) => {
        return {
            style: {
                position: "absolute",
                left: "-35px",
                color: isCollapsed ? "rgb(115, 115, 115)" : "#03a9f4",
                width: "28px",
                height: "28px",
                minWidth: "28px",
                minHeight: "28px",
                fontSize: "15px",
                transform: isCollapsed ? "rotate(270deg)" : "rotate(360deg)",
                transition: "all .3s ease-out"
            },
            children: <CollapseIcon />
        };
    },
    filterProps: ["isCollapsed"]
})();

///////////////////////////////////////////////////////////

let renderPosition = (field, property, isRuleMode) => {
    let positionName =
        property.isInArray || property.schemaType === "csv"
            ? "position"
            : "property";

    return (
        <ParameterTextField
            field={field}
            label={positionName}
            placeholder={positionName}
            disabled={isRuleMode}
        />
    );
};

let PropertyLayout = ({ parameters }) => (
    <PropertyLayoutContainer>
        <ControlsLayout>
            <ParametersPanel>{parameters}</ParametersPanel>
        </ControlsLayout>
    </PropertyLayoutContainer>
);

let RootCmp = observer(
    ({
        node: { form, property, isCollapsed, setCollapsed, addProperty },
        isRuleMode
    }) => {
        let addButton =
            property.type === types.array ? (
                <AddPropertyButton onClick={() => addProperty()}>
                    Add item
                </AddPropertyButton>
            ) : (
                <AddPropertyButton onClick={() => addProperty()}>
                    Add property
                </AddPropertyButton>
            );

        let parameters = [
            <InlineContainer key="schemaType">
                <ParameterSelectField
                    field={form.$.parameters.$.schemaType}
                    label="schema"
                    disabled={isRuleMode}
                />
            </InlineContainer>
        ];

        if (property.schemaType !== "csv") {
            parameters.push(
                <InlineContainer key="type">
                    <ParameterSelectField
                        field={form.$.type}
                        disabled={isRuleMode}
                        label="type"
                    />
                </InlineContainer>
            );
        }

        if (!isRuleMode) {
            parameters.push(<div key="add-button">{addButton}</div>);
        }

        return <PropertyLayout parameters={parameters} />;
    }
);

let ArrayCmp = observer(
    ({
        node: {
            form,
            property,
            children,
            isCollapsed,
            setCollapsed,
            addProperty
        },
        isRuleMode
    }) => {
        let parameters = [
            <InlineContainer key="type">
                <ParameterSelectField
                    field={form.$.type}
                    label="type"
                    placeholder="type"
                    disabled={isRuleMode}
                />
            </InlineContainer>,
            <InlineContainer key="position">
                {renderPosition(
                    form.$.parameters.$.position,
                    property,
                    isRuleMode
                )}
            </InlineContainer>,
            <div key="error">
                {form.$.parameters.showFormError && <Error>{form.$.parameters.formError}</Error>}
            </div>
        ];

        if (children.length || isCollapsed) {
            parameters.unshift(
                <CollapseButton
                    isCollapsed={isCollapsed}
                    key="collapseBtn"
                    onClick={() => setCollapsed(!isCollapsed)}
                />
            );
        }

        if (!isRuleMode) {
            parameters.push(
                <div key="add-item-btn">
                    <AddPropertyButton onClick={() => addProperty()}>
                        Add item
                    </AddPropertyButton>
                </div>
            );
        }

        return <PropertyLayout parameters={parameters} />;
    }
);

let ObjectCmp = observer(
    ({
        node: {
            form,
            property,
            children,
            isCollapsed,
            setCollapsed,
            addProperty
        },
        isRuleMode
    }) => {
        let parameters = [
            <InlineContainer key="type">
                <ParameterSelectField
                    field={form.$.type}
                    label="type"
                    placeholder="type"
                    disabled={isRuleMode}
                />
            </InlineContainer>,
            <InlineContainer key="position">
                {renderPosition(
                    form.$.parameters.$.position,
                    property,
                    isRuleMode
                )}
            </InlineContainer>,
            <div key="error">
                {form.$.parameters.showFormError && <Error>{form.$.parameters.formError}</Error>}
            </div>
        ];

        if (children.length || isCollapsed) {
            parameters.unshift(
                <CollapseButton
                    isCollapsed={isCollapsed}
                    key="collapseBtn"
                    onClick={() => setCollapsed(!isCollapsed)}
                />
            );
        }

        if (!isRuleMode) {
            parameters.push(
                <div key="add-button">
                    <AddPropertyButton onClick={() => addProperty()}>
                        Add property
                    </AddPropertyButton>
                </div>
            );
        }

        return <PropertyLayout parameters={parameters} />;
    }
);

let StringCmp = observer(({ node: { form, property }, isRuleMode }) => (
    <PropertyLayout
        parameters={[
            <InlineContainer key="type">
                <ParameterSelectField
                    field={form.$.type}
                    label="type"
                    placeholder="type"
                    disabled={isRuleMode}
                />
            </InlineContainer>,
            <InlineContainer key="name">
                <ParameterTextField
                    field={form.$.parameters.$.name}
                    label="name"
                    placeholder="meaningful name"
                    disabled={isRuleMode}
                />
            </InlineContainer>,
            <InlineContainer key="position">
                {renderPosition(
                    form.$.parameters.$.position,
                    property,
                    isRuleMode
                )}
            </InlineContainer>,
            <InlineContainer key="description">
                <ParameterTextField
                    field={form.$.parameters.$.description}
                    label="description"
                    placeholder="description"
                    disabled={isRuleMode}
                />
            </InlineContainer>,
            <div key="error">
                {form.$.parameters.showFormError && <Error>{form.$.parameters.formError}</Error>}
            </div>
        ]}
    />
));

let TimestampCmp = observer(({ node: { form, property }, isRuleMode }) => (
    <PropertyLayout
        parameters={[
            <InlineContainer key="type">
                <ParameterSelectField
                    field={form.$.type}
                    label="type"
                    placeholder="type"
                    disabled={isRuleMode}
                />
            </InlineContainer>,
            <InlineContainer key="name">
                <ParameterTextField
                    field={form.$.parameters.$.name}
                    label="name"
                    placeholder="meaningful name"
                    disabled={isRuleMode}
                />
            </InlineContainer>,
            <InlineContainer key="position">
                {renderPosition(
                    form.$.parameters.$.position,
                    property,
                    isRuleMode
                )}
            </InlineContainer>,
            <InlineContainer key="format">
                <ParameterSelectField
                    field={form.$.parameters.$.format}
                    label="format"
                    disabled={isRuleMode}
                />
            </InlineContainer>,
            <InlineContainer key="description">
                <ParameterTextField
                    field={form.$.parameters.$.description}
                    label="description"
                    placeholder="description"
                    disabled={isRuleMode}
                />
            </InlineContainer>,
            <div key="error">
                {form.$.parameters.showFormError && <Error>{form.$.parameters.formError}</Error>}
            </div>
        ]}
    />
));

let DateCmp = observer(({ node: { form, property }, isRuleMode }) => (
    <PropertyLayout
        parameters={[
            <InlineContainer key="type">
                <ParameterSelectField
                    field={form.$.type}
                    label="type"
                    placeholder="type"
                    disabled={isRuleMode}
                />
            </InlineContainer>,
            <InlineContainer key="name">
                <ParameterTextField
                    field={form.$.parameters.$.name}
                    label="name"
                    placeholder="meaningful name"
                    disabled={isRuleMode}
                />
            </InlineContainer>,
            <InlineContainer key="position">
                {renderPosition(
                    form.$.parameters.$.position,
                    property,
                    isRuleMode
                )}
            </InlineContainer>,
            <InlineContainer key="format">
                <ParameterTextField
                    field={form.$.parameters.$.format}
                    label="format"
                    placeholder="format (for ex. YYYY-MM-DD)"
                    disabled={isRuleMode}
                />
            </InlineContainer>,
            <InlineContainer key="description">
                <ParameterTextField
                    field={form.$.parameters.$.description}
                    label="description"
                    placeholder="description"
                    disabled={isRuleMode}
                />
            </InlineContainer>,
            <div key="error">
                {form.$.parameters.showFormError && <Error>{form.$.parameters.formError}</Error>}
            </div>
        ]}
    />
));

let NumericCmp = observer(({ node: { form, property }, isRuleMode }) => (
    <PropertyLayout
        parameters={[
            <InlineContainer key="type">
                <ParameterSelectField
                    field={form.$.type}
                    label="type"
                    placeholder="type"
                    disabled={isRuleMode}
                />
            </InlineContainer>,
            <InlineContainer key="name">
                <ParameterTextField
                    field={form.$.parameters.$.name}
                    label="name"
                    placeholder="meaningful name"
                    disabled={isRuleMode}
                />
            </InlineContainer>,
            <InlineContainer key="position">
                {renderPosition(
                    form.$.parameters.$.position,
                    property,
                    isRuleMode
                )}
            </InlineContainer>,
            <InlineContainer key="description">
                <ParameterTextField
                    field={form.$.parameters.$.description}
                    label="description"
                    placeholder="description"
                    disabled={isRuleMode}
                />
            </InlineContainer>,
            <div key="error">
                {form.$.parameters.showFormError && <Error>{form.$.parameters.formError}</Error>}
            </div>
        ]}
    />
));

let BooleanCmp = observer(({ node: { form, property }, isRuleMode }) => (
    <PropertyLayout
        parameters={[
            <InlineContainer key="type">
                <ParameterSelectField
                    field={form.$.type}
                    label="type"
                    placeholder="type"
                    disabled={isRuleMode}
                />
            </InlineContainer>,
            <InlineContainer key="name">
                <ParameterTextField
                    field={form.$.parameters.$.name}
                    label="name"
                    placeholder="meaningful name"
                    disabled={isRuleMode}
                />
            </InlineContainer>,
            <InlineContainer key="position">
                {renderPosition(
                    form.$.parameters.$.position,
                    property,
                    isRuleMode
                )}
            </InlineContainer>,
            <InlineContainer key="description">
                <ParameterTextField
                    field={form.$.parameters.$.description}
                    label="description"
                    placeholder="description"
                    disabled={isRuleMode}
                />
            </InlineContainer>,
            <div key="error">
                {form.$.parameters.showFormError && <Error>{form.$.parameters.formError}</Error>}
            </div>
        ]}
    />
));

let TypeToComponentMap = {
    [types.array]: ArrayCmp,
    [types.object]: ObjectCmp,
    [types.string]: StringCmp,
    [types.timestamp]: TimestampCmp,
    [types.date]: DateCmp,
    [types.integer]: NumericCmp,
    [types.double]: NumericCmp,
    [types.long]: NumericCmp,
    [types.boolean]: BooleanCmp
};

///////////////////////////////////////////////////////////////////////

const AsIsRuleComponent = observer(({ node: { ruleForm: form, property } }) => {
    return (
        <PropertyLayout
            parameters={[
                <InlineContainer key="ruleType">
                    <ParameterSelectField
                        field={form.$.ruleType}
                        label="Rule type"
                    />
                </InlineContainer>
            ]}
        />
    );
});

const UUIDRuleComponent = observer(({ node: { ruleForm: form, property } }) => {
    return (
        <PropertyLayout
            parameters={[
                <InlineContainer key="ruleType">
                    <ParameterSelectField
                        field={form.$.ruleType}
                        label="Rule type"
                    />
                </InlineContainer>,
                <Flex key="uuidParams">
                    <InlineContainer key="prefix">
                        <ParameterTextField
                            field={form.$.prefix}
                            label="prefix"
                            placeholder="prefix"
                        />
                    </InlineContainer>
                    <InlineContainer key="postfix">
                        <ParameterTextField
                            field={form.$.postfix}
                            label="postfix"
                            placeholder="postfix"
                        />
                    </InlineContainer>
                </Flex>
            ]}
        />
    );
});

const LiteralStringRuleComponent = observer(
    ({ node: { ruleForm: form, property } }) => {
        return (
            <PropertyLayout
                parameters={[
                    <InlineContainer key="ruleType">
                        <ParameterSelectField
                            field={form.$.ruleType}
                            label="Rule type"
                        />
                    </InlineContainer>,
                    <InlineContainer key="value">
                        <ParameterTextField
                            field={form.$.value}
                            label="value"
                            placeholder="value"
                        />
                    </InlineContainer>
                ]}
            />
        );
    }
);

const LiteralBooleanRuleComponent = observer(
    ({ node: { ruleForm: form, property } }) => {
        return (
            <PropertyLayout
                parameters={[
                    <InlineContainer key="ruleType">
                        <ParameterSelectField
                            field={form.$.ruleType}
                            label="Rule type"
                        />
                    </InlineContainer>,
                    <InlineContainer key="value">
                        <ParameterSelectField
                            field={form.$.value}
                            label="value"
                            placeholder="value"
                        />
                    </InlineContainer>
                ]}
            />
        );
    }
);

const LiteralIntegerRuleComponent = observer(
    ({ node: { ruleForm: form, property } }) => {
        return (
            <PropertyLayout
                parameters={[
                    <InlineContainer key="ruleType">
                        <ParameterSelectField
                            field={form.$.ruleType}
                            label="Rule type"
                        />
                    </InlineContainer>,
                    <InlineContainer key="value">
                        <ParameterTextField
                            field={form.$.value}
                            label="value"
                            placeholder="value"
                        />
                    </InlineContainer>
                ]}
            />
        );
    }
);

const LiteralLongRuleComponent = observer(
    ({ node: { ruleForm: form, property } }) => {
        return (
            <PropertyLayout
                parameters={[
                    <InlineContainer key="ruleType">
                        <ParameterSelectField
                            field={form.$.ruleType}
                            label="Rule type"
                        />
                    </InlineContainer>,
                    <InlineContainer key="value">
                        <ParameterTextField
                            field={form.$.value}
                            label="value"
                            placeholder="value"
                        />
                    </InlineContainer>
                ]}
            />
        );
    }
);

const LiteralDoubleRuleComponent = observer(
    ({ node: { ruleForm: form, property } }) => {
        return (
            <PropertyLayout
                parameters={[
                    <InlineContainer key="ruleType">
                        <ParameterSelectField
                            field={form.$.ruleType}
                            label="Rule type"
                        />
                    </InlineContainer>,
                    <InlineContainer key="value">
                        <ParameterTextField
                            field={form.$.value}
                            label="value"
                            placeholder="value"
                        />
                    </InlineContainer>
                ]}
            />
        );
    }
);

const RandomIntegerRuleComponent = observer(
    ({ node: { ruleForm: form, property } }) => {
        return (
            <PropertyLayout
                parameters={[
                    <InlineContainer key="ruleType">
                        <ParameterSelectField
                            field={form.$.ruleType}
                            label="Rule type"
                        />
                    </InlineContainer>,
                    <Flex key="doubleParams">
                        <InlineContainer>
                            <ParameterTextField
                                field={form.$.min}
                                label="min"
                                placeholder="min"
                            />
                        </InlineContainer>
                        <InlineContainer>
                            <ParameterTextField
                                field={form.$.max}
                                label="max"
                                placeholder="max"
                            />
                        </InlineContainer>
                    </Flex>,
                    <div key="error">
                        {form.showFormError && <Error>{form.formError}</Error>}
                    </div>
                ]}
            />
        );
    }
);

const RandomLongRuleComponent = observer(
    ({ node: { ruleForm: form, property } }) => {
        return (
            <PropertyLayout
                parameters={[
                    <InlineContainer key="ruleType">
                        <ParameterSelectField
                            field={form.$.ruleType}
                            label="Rule type"
                        />
                    </InlineContainer>,
                    <Flex key="longParams">
                        <InlineContainer>
                            <ParameterTextField
                                field={form.$.min}
                                label="min"
                                placeholder="min"
                            />
                        </InlineContainer>
                        <InlineContainer>
                            <ParameterTextField
                                field={form.$.max}
                                label="max"
                                placeholder="max"
                            />
                        </InlineContainer>
                    </Flex>,
                    <div key="error">
                        {form.showFormError && <Error>{form.formError}</Error>}
                    </div>
                ]}
            />
        );
    }
);

const RandomDoubleRuleComponent = observer(
    ({ node: { ruleForm: form, property } }) => {
        return (
            <PropertyLayout
                parameters={[
                    <InlineContainer key="ruleType">
                        <ParameterSelectField
                            field={form.$.ruleType}
                            label="Rule type"
                        />
                    </InlineContainer>,
                    <Flex key="doubleParams">
                        <InlineContainer key="min">
                            <ParameterTextField
                                field={form.$.min}
                                label="min"
                                placeholder="min"
                            />
                        </InlineContainer>
                        <InlineContainer key="max">
                            <ParameterTextField
                                field={form.$.max}
                                label="max"
                                placeholder="max"
                            />
                        </InlineContainer>
                    </Flex>,
                    <div key="error">
                        {form.showFormError && <Error>{form.formError}</Error>}
                    </div>
                ]}
            />
        );
    }
);

const RandomBooleanRuleComponent = observer(
    ({ node: { ruleForm: form, property } }) => {
        return (
            <PropertyLayout
                parameters={[
                    <InlineContainer key="ruleType">
                        <ParameterSelectField
                            field={form.$.ruleType}
                            label="Rule type"
                        />
                    </InlineContainer>,
                    <InlineContainer key="successProbability">
                        <ParameterTextField
                            field={form.$.successProbability}
                            label="success probability"
                            placeholder="success probability"
                        />
                    </InlineContainer>
                ]}
            />
        );
    }
);

const DevicePropertyRuleComponent = observer(
    ({ node: { ruleForm: form, property } }) => {
        return (
            <PropertyLayout
                parameters={[
                    <InlineContainer key="ruleType">
                        <ParameterSelectField
                            field={form.$.ruleType}
                            label="Rule type"
                        />
                    </InlineContainer>,
                    <InlineContainer key="propertyName">
                        <ParameterSelectField
                            field={form.$.propertyName}
                            label="property name"
                        />
                    </InlineContainer>
                ]}
            />
        );
    }
);

const CurrentTimeRuleComponent = observer(
    ({ node: { ruleForm: form, property } }) => {
        return (
            <PropertyLayout
                parameters={[
                    <InlineContainer key="ruleType">
                        <ParameterSelectField
                            field={form.$.ruleType}
                            label="Rule type"
                        />
                    </InlineContainer>,
                    <Flex key="currentTime">
                        <InlineContainer key="shift">
                            <ParameterTextField
                                field={form.$.shift}
                                label="shift"
                            />
                        </InlineContainer>
                        <InlineContainer key="metric">
                            <ParameterSelectField
                                field={form.$.metric}
                                label="shift metric"
                            />
                        </InlineContainer>
                    </Flex>
                ]}
            />
        );
    }
);

const RelativeTimeRuleComponent = observer(
    ({ node: { ruleForm: form, property } }) => {
        return (
            <PropertyLayout
                parameters={[
                    <InlineContainer key="ruleType">
                        <ParameterSelectField
                            field={form.$.ruleType}
                            label="Rule type"
                        />
                    </InlineContainer>,
                    <InlineContainer key="relativePosition">
                        <ParameterSelectField
                            field={form.$.relativePosition}
                            label="position"
                        />
                    </InlineContainer>,
                    <InlineContainer key="shift">
                        <ParameterTextField
                            field={form.$.shift}
                            label="shift"
                        />
                    </InlineContainer>,
                    <InlineContainer key="metric">
                        <ParameterSelectField
                            field={form.$.metric}
                            label="shift metric"
                        />
                    </InlineContainer>
                ]}
            />
        );
    }
);

@observer
class CustomFunctionRuleComponent extends React.Component {
    @observable isEditorModalOpen = false;

    @action
    toggleEditorModal = isOpen => {
        this.isEditorModalOpen = isOpen;
    };

    @action
    onEditorSave = () => {
        let form$ = this.props.node.ruleForm;
        let editorFunctionField = form$.$.editorFunction;
        let functionField = form$.$.function;

        editorFunctionField.validate().then(({ hasError }) => {
            if (!hasError) {
                functionField.onChange(editorFunctionField.$);
                this.toggleEditorModal(false);
            }
        });
    };

    @action
    onEditorCancel = () => {
        let form$ = this.props.node.ruleForm;
        let editorFunctionField = form$.$.editorFunction;
        let functionField = form$.$.function;
        editorFunctionField.onChange(functionField.$);
        this.toggleEditorModal(false);
    };

    render() {
        let { node: { ruleForm: form, property } } = this.props;

        return (
            <div>
                <PropertyLayout
                    parameters={[
                        <InlineContainer key="ruleType">
                            <ParameterSelectField
                                field={form.$.ruleType}
                                label="Rule type"
                            />
                        </InlineContainer>,
                        <InlineContainer key="dependsOn">
                            <ParameterSelectField
                                field={form.$.dependsOn}
                                label="Depends on"
                            />
                        </InlineContainer>,
                        <InlineContainer key="editor button">
                            <EditorButton
                                onClick={this.toggleEditorModal.bind(
                                    this,
                                    true
                                )}
                            >
                                Open editor
                            </EditorButton>
                        </InlineContainer>
                    ]}
                />

                <Dialog
                    maxWidth="md"
                    fullWidth={true}
                    open={this.isEditorModalOpen}
                >
                    <DialogTitle>Edit custom function</DialogTitle>
                    <DialogContent>
                        <Editor field={form.$.editorFunction} />
                        {form.hasError && form}
                    </DialogContent>
                    <DialogActions>
                        <Button color="accent" onClick={this.onEditorCancel}>
                            Cancel
                        </Button>
                        <Button color="primary" onClick={this.onEditorSave}>
                            Save
                        </Button>
                    </DialogActions>
                </Dialog>
            </div>
        );
    }
}

let RuleTypeToComponentMap = {
    as_is: AsIsRuleComponent,
    uuid: UUIDRuleComponent,
    literal_string: LiteralStringRuleComponent,
    literal_boolean: LiteralBooleanRuleComponent,
    literal_integer: LiteralIntegerRuleComponent,
    literal_double: LiteralDoubleRuleComponent,
    literal_long: LiteralLongRuleComponent,
    random_integer: RandomIntegerRuleComponent,
    random_double: RandomDoubleRuleComponent,
    random_long: RandomLongRuleComponent,
    random_boolean: RandomBooleanRuleComponent,
    device_property: DevicePropertyRuleComponent,
    current_time: CurrentTimeRuleComponent,
    relative_time: RelativeTimeRuleComponent,
    custom_function: CustomFunctionRuleComponent
};

@observer
class PropertyNode extends Component {
    renderRules() {
        let { value } = this.props;
        let { property, ruleForm } = value;

        let ruleType = ruleForm.$.ruleType.$;
        let RuleComponent = RuleTypeToComponentMap[ruleType];

        if (RuleComponent) {
            return (
                <RuleLayout>
                    {RuleComponent && <RuleComponent node={value} />}
                </RuleLayout>
            );
        }
        return null;
    }

    renderChildren() {
        let { value, isRuleMode } = this.props;
        let { form, property, children } = value;
        let { isRoot } = property;

        return children.map((child, i) => {
            return (
                <Property
                    key={child.property.id}
                    value={child}
                    isRuleMode={isRuleMode}
                />
            );
        });
    }

    render() {
        let { value, isRuleMode } = this.props;

        let { form, property } = value;
        let { isRoot } = property;

        let type$ = form.$.type;
        let PropertyCmp = isRoot ? RootCmp : TypeToComponentMap[property.type];

        let isArray = property.type === "array";
        let isObject = property.type === "object";

        return (
            <NodeContent>
                <PropertyComponentContainer>
                    <PropertyCmp node={value} isRuleMode={isRuleMode} />
                    {!isRoot && isRuleMode && this.renderRules()}
                </PropertyComponentContainer>

                {!isRoot &&
                !isRuleMode && (
                    <DeleteButtonLayout>
                        <DeleteButton onClick={() => property.delete()}>
                            <DeleteIcon />
                        </DeleteButton>
                    </DeleteButtonLayout>
                )}

                {(isRoot || isArray || isObject) && this.renderChildren()}
            </NodeContent>
        );
    }
}

@observer
class Property extends Component {
    render() {
        let { value, isRuleMode = false } = this.props;
        return (
            <SchemaNodeComponent isCollapsed={value.isCollapsed}>
                <NodeContainer>
                    <PropertyNode value={value} isRuleMode={isRuleMode} />
                </NodeContainer>
            </SchemaNodeComponent>
        );
    }
}

export default Property;
