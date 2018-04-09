import React, { Component } from "react";
import { autorun } from "mobx";
import { inject, observer } from "mobx-react";
import glamorous from "glamorous";

import Button from "material-ui/Button";
import TextField from "material-ui/TextField";
import Select from "material-ui/Select";
import { MenuItem } from "material-ui/Menu";
import { FormControl, FormHelperText } from "material-ui/Form";
import Input, { InputLabel } from "material-ui/Input";
import IconButton from "material-ui/IconButton";
import DeleteIcon from "components/Icons/CrossIcon";

import BaseForm from "components/Form";
import MaterialTextField from "components/Form/inputs/MaterialTextField";
import MaterialSelect from "components/Form/inputs/MaterialSelect";

import Dialog, {
    DialogActions,
    DialogContent,
    DialogTitle
} from "material-ui/Dialog";

const SchemaNodeComponent = glamorous.div(
    {
        marginLeft: "20px",
        transform: "translate3d(0, 0, 0) scale(1)"
    },
    ({ showLine }) => ({
        borderLeft: showLine ? "3px solid #03a9f4" : "none"
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
    marginTop: "10px"
});

const NodeContentBody = glamorous.div({});

const ControlsLayout = glamorous.div({
    height: "100%",
    background: "white",
    padding: "15px",
    display: "flex",
    justifyContent: "space-between",
    width: "100%",
    flexDirection: "row"
});

const InlineContainer = glamorous.div({
    display: "inline-block",
    marginRight: "10px"
});

const SchemaInput = glamorous(Input, {
    withProps: {
        style: {
            fontSize: "14px",
            marginRight: "5px"
        }
    }
})({});

const SchemaRoot = glamorous.div({
    background: "rgb(233, 233, 233)",
    padding: "0 15px",
    textAlign: "center"
});

const TypeSelect = glamorous.span({
    display: "inline-block",
    verticalAlign: "top"
});

const ParametersPanel = glamorous.div({
    marginTop: "0px",
    width: "100%"
});

const ParameterTextField = glamorous(MaterialTextField, {
    withProps: ({ field }) => ({
        style: {
            fontSize: "14px",
            display: field.key === "name" ? "inline-block" : "block",
            marginRight: "10px"
        }
    })
})();

const ParameterSelectField = glamorous(MaterialSelect, {
    withProps: {
        displayEmpty: true,
        style: {
            fontSize: "14px",
            minWidth: "135px"
        }
    }
})({});

const ParameterButtonField = glamorous(Button, {
    withProps: {
        style: {
            margin: "0 0 10px 0",
            background: "rgb(230,230,230)"
        }
    }
})({});

const AddPropertyButton = glamorous(Button, {
    withProps: {
        style: {
            width: "242px",
            margin: "13px",
            background: "#e6e6e6"
        }
    }
})({});

const AddPropertyButtonLayout = glamorous.div(
    {
        background: "rgb(233,233,233)"
    },
    ({ isFilled }) => ({
        background: isFilled ? "#f4f4f4" : "inherit"
    })
);

const RemoveButtonLayout = glamorous.div({
    display: "flex",
    height: "100%",
    alignItems: "center",
    alignSelf: "center"
});

const RulesContainer = glamorous.div({
    padding: "15px",
    background: "white",
    borderLeft: "1px dashed #c4c4c4",
    width: "100%"
});

@observer
class SchemaNode extends Component {
    addProperty = e => {
        let { form } = this.props;
        form.$("properties").onAdd(e);
    };

    removeProperty = e => {
        let { form } = this.props;
        form.$hooks.onDel(e);
    };

    addItem = e => {
        let { form } = this.props;
        form.$("items").onAdd(e);
    };

    removeItem = e => {
        let { form } = this.props;
        form.$hooks.onDel(e);
    };

    isObject(type) {
        return type === "object";
    }

    isArray(type) {
        return type === "array";
    }

    renderNestedFields(nestedFields = []) {
        return nestedFields.map(field => {
            return (
                <SchemaNode
                    key={field.path}
                    form={field}
                    isRuleMode={this.props.isRuleMode}
                />
            );
        });
    }

    renderParameters(parametersField) {
        let type = parametersField.$("type").value;

        let isObject = this.isObject(type);
        let isArray = this.isArray(type);

        return (
            <ParametersPanel>
                <form>
                    {!isObject &&
                    !isArray &&
                    parametersField.has("name") && (
                        <InlineContainer>
                            <ParameterTextField
                                field={parametersField.$("name")}
                            />
                        </InlineContainer>
                    )}

                    {parametersField.has("type") && (
                        <InlineContainer>
                            <ParameterSelectField
                                field={parametersField.$("type")}
                            />
                        </InlineContainer>
                    )}

                    {parametersField.has("jsonPosition") &&
                        (((isArray || isObject) && (
                            <InlineContainer>
                                <ParameterTextField
                                    field={parametersField.$("jsonPosition")}
                                />
                            </InlineContainer>
                        )) ||
                            (!isArray && (
                                <div>
                                    <InlineContainer>
                                        <ParameterTextField
                                            field={parametersField.$(
                                                "jsonPosition"
                                            )}
                                        />
                                    </InlineContainer>
                                </div>
                            )))}

                    <div>
                        {parametersField.has("csvPosition") && (
                            <InlineContainer>
                                <ParameterTextField
                                    field={parametersField.$("csvPosition")}
                                />
                            </InlineContainer>
                        )}
                    </div>

                    <div>
                        {parametersField.has("arrayPosition") &&
                            (((isArray || isObject) && (
                                <InlineContainer>
                                    <ParameterTextField
                                        field={parametersField.$(
                                            "arrayPosition"
                                        )}
                                    />
                                </InlineContainer>
                            )) ||
                                (!isArray && (
                                    <div>
                                        <ParameterTextField
                                            field={parametersField.$(
                                                "arrayPosition"
                                            )}
                                        />
                                    </div>
                                )))}
                    </div>

                    {!isObject &&
                    !isArray && (
                        <div>
                            <div>
                                {parametersField.has("dateFormat") && (
                                    <InlineContainer>
                                        <ParameterTextField
                                            field={parametersField.$(
                                                "dateFormat"
                                            )}
                                        />
                                    </InlineContainer>
                                )}

                                {parametersField.has("timestampFormat") && (
                                    <InlineContainer>
                                        <ParameterSelectField
                                            field={parametersField.$(
                                                "timestampFormat"
                                            )}
                                        />
                                    </InlineContainer>
                                )}
                            </div>

                            <div>
                                {parametersField.has("description") && (
                                    <InlineContainer>
                                        <ParameterTextField
                                            field={parametersField.$(
                                                "description"
                                            )}
                                        />
                                    </InlineContainer>
                                )}
                            </div>
                        </div>
                    )}
                </form>
            </ParametersPanel>
        );
    }

    renderRules(rulesField) {
        return (
            <div>
                {rulesField.map(field => {
                    let type = field.$type || field.type;

                    if (type === "editor") {
                        return null;
                    }

                    if (type === "button") {
                        return (
                            <div key={field.key}>
                                <ParameterButtonField
                                    onClick={field.$hooks.onClick}
                                >
                                    {field.label}
                                </ParameterButtonField>
                            </div>
                        );
                    }

                    if (type === "select") {
                        return (
                            <div key={field.key}>
                                <ParameterSelectField
                                    field={field}
                                />
                            </div>
                        );
                    }

                    return (
                        <InlineContainer key={field.key}>
                            <ParameterTextField field={field} />
                        </InlineContainer>
                    );
                })}
            </div>
        );
    }

    render() {
        let { form, isRoot, isRuleMode } = this.props;

        let parametersField = form.has("parameters") && form.$("parameters");
        let rulesField = form.has("injectionRules") && form.$("injectionRules");
        let type = null;

        if (isRoot) {
            type = form.$("type").value;
        } else {
            type = parametersField ? parametersField.$("type").value : null;
        }

        let nestedFields = null;
        if (type === "object") {
            nestedFields =
                form.has("properties") &&
                form
                    .$("properties")
                    .fields.entries()
                    .filter(([key]) => {
                        return key !== "parameters";
                    })
                    .map(([key, field]) => field);
        } else if (type === "array") {
            nestedFields = form.has("items") && form.$("items").fields.values();
        }

        let isObject = this.isObject(type);
        let isArray = this.isArray(type);

        return (
            <SchemaNodeComponent showLine={!isRoot}>
                <NodeContainer>
                    <NodeContent>
                        {isObject &&
                        !isRuleMode && (
                            <AddPropertyButtonLayout
                                isFilled={isObject}
                                isFilled={false}
                            >
                                <AddPropertyButton onClick={this.addProperty}>
                                    Add property
                                </AddPropertyButton>
                            </AddPropertyButtonLayout>
                        )}

                        {isArray &&
                        !isRuleMode && (
                            <AddPropertyButtonLayout isFilled={false}>
                                <AddPropertyButton onClick={this.addItem}>
                                    Add item
                                </AddPropertyButton>
                            </AddPropertyButtonLayout>
                        )}

                        {!isRoot && (
                            <ControlsLayout>
                                {parametersField &&
                                    this.renderParameters(parametersField)}

                                {!isRuleMode && (
                                    <RemoveButtonLayout>
                                        <IconButton
                                            onClick={this.removeProperty}
                                        >
                                            <DeleteIcon />
                                        </IconButton>
                                    </RemoveButtonLayout>
                                )}
                            </ControlsLayout>
                        )}
                    </NodeContent>

                    {!isRoot &&
                    isRuleMode && (
                        <RulesContainer>
                            {this.renderRules(rulesField)}
                        </RulesContainer>
                    )}
                </NodeContainer>

                {nestedFields &&
                    (isObject || isRoot || isArray) &&
                    this.renderNestedFields(nestedFields)}
            </SchemaNodeComponent>
        );
    }
}

export default SchemaNode;
