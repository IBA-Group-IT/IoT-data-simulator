import React, { Component } from "react";
import { computed } from "mobx";
import { inject, observer } from "mobx-react";
import glamorous from "glamorous";

import MaterialTextField from "components/Form/inputs/MaterialTextField";
import MaterialSelect from "components/Form/inputs/MaterialSelect";
import MaterialFile from "components/Form/inputs/MaterialFile";

import Button from "material-ui/Button";
import IconButton from "material-ui/IconButton";
import DeleteIcon from "components/Icons/DeleteIcon";
import KeyboardArrowLeft from "material-ui-icons/KeyboardArrowLeft";
import KeyboardArrowRight from "material-ui-icons/KeyboardArrowRight";

import ViewLayout from "components/ViewLayout";
import ControlPanel from "components/ControlPanel";
import TargetSystemsList from "./TargetSystemsList";
import Stepper from '../../Stepper';

const CreateEditDevicePanelLayout = glamorous.div({
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

const SubmitButton = glamorous(Button)({});

const SelectComponent = glamorous(MaterialSelect, {
    withProps: {
        style: {
            width: "100px"
        }
    }
})();

const TextValueComponent = glamorous(MaterialTextField, {
    withProps: {
        style: {
            width: "170px"
        }
    }
})();

const SelectValueComponent = glamorous(MaterialSelect, {
    withProps: {
        style: {
            width: "170px"
        }
    }
})();

@observer
class CreateEditDevicePanel extends Component {
    @computed
    get nextHandler() {
        let { store, onComplete } = this.props;
        let { deviceForm, wizardStore } = store;
        let { step } = wizardStore;

        if (step === 0) {
            return wizardStore.handleNextStep;
        }
        return onComplete;
    }

    @computed
    get backHandler() {
        let { store } = this.props;
        let { wizardStore } = store;
        return wizardStore.handlePreviousStep;
    }

    @computed
    get isNextActive() {
        let { store } = this.props;
        let { deviceForm, wizardStore: { step } } = store;

        if (step === 0 && !deviceForm.isValid) {
            return false;
        }
        return true;
    }

    @computed
    get isBackActive() {
        let { store } = this.props;
        let { wizardStore: { step } } = store;
        if (step === 0) {
            return false;
        }
        return true;
    }

    render() {
        let { store } = this.props;
        let { deviceForm, deviceFormIsValid, wizardStore } = store;
        let { step } = wizardStore;

        return (
            <CreateEditDevicePanelLayout>
                <PanelInnerContainer>
                    <Stepper
                        steps={2}
                        activeStep={step}
                        nextButton={
                            <Button
                                dense
                                onClick={this.nextHandler}
                                disabled={!this.isNextActive}
                            >
                                Proceed
                                {<KeyboardArrowRight />}
                            </Button>
                        }
                        backButton={
                            <Button
                                dense
                                onClick={this.backHandler}
                                disabled={!this.isBackActive}
                            >
                                {<KeyboardArrowLeft />}
                                Back
                            </Button>
                        }
                    />

                    {step === 0 && (
                        <div>
                            <Section>
                                <SectionTitle>1. Populate name</SectionTitle>
                                <SectionContent>
                                    <MaterialTextField
                                        field={deviceForm.$("name")}
                                    />
                                </SectionContent>
                            </Section>
                            <Section>
                                <SectionTitle>
                                    2. Populate properties
                                </SectionTitle>
                                <SectionContent>
                                    <AddPropertyButton
                                        onClick={
                                            deviceForm.$("properties").onAdd
                                        }
                                    >
                                        Add property
                                    </AddPropertyButton>

                                    {deviceForm
                                        .$("properties")
                                        .map(deviceField => {
                                            let nameField = deviceField.$(
                                                "name"
                                            );
                                            let typeField = deviceField.$(
                                                "type"
                                            );
                                            let _valProp = store.getPropValueField(
                                                typeField.value
                                            );
                                            let valueField = deviceField.$(
                                                _valProp
                                            );

                                            return (
                                                <div key={deviceField.key}>
                                                    <InlineInput>
                                                        <MaterialTextField
                                                            field={nameField}
                                                        />
                                                    </InlineInput>
                                                    <InlineInput>
                                                        <SelectComponent
                                                            field={typeField}
                                                        />
                                                    </InlineInput>
                                                    <InlineInput>
                                                        {valueField.type ===
                                                            "text" && (
                                                            <TextValueComponent
                                                                field={
                                                                    valueField
                                                                }
                                                            />
                                                        )}
                                                        {valueField.type ===
                                                            "select" && (
                                                            <SelectValueComponent
                                                                field={
                                                                    valueField
                                                                }
                                                            />
                                                        )}
                                                    </InlineInput>
                                                    <InlineInput>
                                                        <IconButton
                                                            onClick={() => {
                                                                deviceField.del();
                                                                deviceField.$hooks.onDel(
                                                                    deviceField
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
                        </div>
                    )}

                    {step === 1 && (
                        <ViewLayout
                            controlPanel={
                                <ControlPanel
                                    addButton={
                                        <Button
                                            disabled={
                                                store.device.targetSystems.length === 4
                                            }
                                            raised
                                            onClick={
                                                store.appStore.view
                                                    .openCreateDeviceSystem
                                            }
                                            color="accent"
                                        >
                                            Create device target system
                                        </Button>
                                    }
                                />
                            }
                            list={<TargetSystemsList store={store} />}
                        />
                    )}
                </PanelInnerContainer>
            </CreateEditDevicePanelLayout>
        );
    }
}

export default CreateEditDevicePanel;
