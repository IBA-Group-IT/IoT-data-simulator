import React, { Component } from "react";
import { computed } from "mobx";
import { inject, observer } from "mobx-react";
import glamorous from "glamorous";

import Button from "material-ui/Button";
import { FormLabel, FormControl, FormControlLabel } from "material-ui/Form";

import AceEditor from "react-ace";
import "brace/mode/javascript";
import "brace/theme/monokai";

import Stepper from "../../Stepper";
import KeyboardArrowLeft from "material-ui-icons/KeyboardArrowLeft";
import KeyboardArrowRight from "material-ui-icons/KeyboardArrowRight";
import ViewLayout from "components/ViewLayout";

import Dialog, {
    DialogActions,
    DialogContent,
    DialogTitle
} from "material-ui/Dialog";

import DefinitionControlPanel from "../../definition/ControlPanel";
import DefinitionsList from "../../definition/DefinitionsList";

import DevicesControlPanel from "../../device/ControlPanel";
import DevicesList from "../../device/DevicesListContainer";

import SystemsControlPanel from "../../system/ControlPanel";
import SystemsList from "../../system/SystemsListContainer";

import MaterialTextField from "components/Form/inputs/MaterialTextField";
import MaterialSelect from "components/Form/inputs/MaterialSelect";
import MaterialCheckbox from 'components/Form/inputs/MaterialCheckbox';
import EditorField from "components/Form/Editor";

import SchemaNode from "../../definition/DefinitionWizard/SchemaStep/SchemaConstructor/SchemaNode";
import Property from "../../definition/DefinitionWizard/SchemaStep/Constructor/Property";

import FormstateField from "components/Form/Field";
import FormstateSelectField from "components/Form/SelectField";
import FormstateEditorField from "components/Form/Editor";
import AdvancedLink from "components/AdvancedLink";

//////////////////////////////////////////////////////////////////////
const SessionWizardLayout = glamorous.div({
    display: "flex",
    flexDirection: "column",
    minHeight: "0",
    height: "100%",
    width: "100%"
});

const StepOuterContainer = glamorous.div({
    display: "flex",
    minHeight: "0",
    height: "100%",
    overflow: "auto"
});

const NextButton = glamorous(Button)({});
NextButton.defaultProps = {
    raised: true,
    style: {
        marginRight: "15px"
    }
};

const StepContainer = glamorous.div({
    padding: "10px 0",
    width: "100%",
    display: "flex",
    flexDirection: "column"
});

const StepActionsContainer = glamorous.div({
    margin: "15px 0"
});

const StepInnerContainer = glamorous.div({
    padding: "10px 30px",
    height: "100%"
});

const SelectField = glamorous(MaterialSelect, {
    withProps: {
        style: {
            fontSize: "14px",
            width: "160px"
        }
    }
})();

const TextField = glamorous(MaterialTextField, {
    withProps: ({ field } = {}) => ({
        style: {
            fontSize: "14px",
            marginTop: "5px",
            display: "inline-block",
            marginRight: "10px",
            marginRight: "10px",
            width: "160px"
        }
    })
})();

const NameField = glamorous(TextField, {
    withProps: {
        style: {
            width: "220px"
        }
    }
})();

const StepLabel = glamorous(FormLabel, {
    withProps: {
        style: {
            padding: "10px 30px"
        }
    }
})();

const InjectionFunctionEditor = glamorous(EditorField, {
    withProps: {
        style: {
            height: "100%",
            minHeight: "500px"
        }
    }
})();

const SubTitle = glamorous.p({
    fontSize: "15px",
    fontWeight: "bold",
    color: "#212121",
    "& ~ &": {
        marginTop: "30px"
    }
});

const DatasetFilterContainer = glamorous.div({});

const FilterTypeSelect = glamorous(FormstateSelectField, {
    withProps: {
        style: {
            width: "160px"
        }
    }
})();

@observer
export default class SessionWizard extends React.Component {
    @computed
    get nextLabel() {
        return "Next";
    }

    nextHandler = () => {
        let { store, onComplete } = this.props;
        let { step, stepsCount, nextHandler } = store;
        nextHandler();
        if (step === stepsCount - 1) {
            onComplete && onComplete();
        }
    };

    render() {
        let { store, onComplete } = this.props;
        let {
            step,
            stepsCount,
            nextHandler,
            backHandler,
            session,
            currentStepId,
            steps,
            isNextActive,
            isBackActive,
            injectRulesStore,
            nextLabel,
            datasetFilterStore
        } = store;
        let stepNumber = step + 1;

        return (
            <SessionWizardLayout>
                <Stepper
                    steps={stepsCount}
                    activeStep={step}
                    nextButton={
                        <Button
                            dense
                            onClick={this.nextHandler}
                            disabled={!isNextActive}
                        >
                            {nextLabel}
                            {<KeyboardArrowRight />}
                        </Button>
                    }
                    backButton={
                        <Button
                            dense
                            onClick={backHandler}
                            disabled={!isBackActive}
                        >
                            {<KeyboardArrowLeft />}
                            Back
                        </Button>
                    }
                />

                <StepOuterContainer>
                    {currentStepId === steps.definition && (
                        <StepContainer>
                            <StepLabel component="legend">
                                <b>
                                    Step {stepNumber} of {stepsCount}. Select
                                    definition
                                </b>
                            </StepLabel>
                            <ViewLayout
                                controlPanel={<DefinitionControlPanel />}
                                list={
                                    <DefinitionsList
                                        items={
                                            store.appStore.definitionsStore
                                                .filteredItems
                                        }
                                        selectable={true}
                                        selectedIds={
                                            session.hasDefinition
                                                ? [session.dataDefinition.id]
                                                : []
                                        }
                                        onSelect={store.setDefinition}
                                    />
                                }
                            />
                        </StepContainer>
                    )}

                    {currentStepId === steps.timer && (
                        <StepContainer>
                            <StepLabel component="legend">
                                <b>
                                    Step {stepNumber} of {stepsCount}. Data
                                    producing options
                                </b>
                            </StepLabel>
                            <StepInnerContainer>
                                <SubTitle>Timer</SubTitle>
                                {store.timerStore.form && (
                                    <div>
                                        {store.timerStore.form.map(field => {
                                            let type =
                                                field.$type || field.type;

                                            if (field.key === "ticksNumber" || field.key === 'isReplayLooped')
                                                return;

                                            if (type === "select") {
                                                return (
                                                    <SelectField
                                                        key={field.key}
                                                        field={field}
                                                    />
                                                );
                                            }
                                            if (type === "editor") {
                                                return (
                                                    <AceEditor
                                                        key={field.key}
                                                        mode="javascript"
                                                        theme="monokai"
                                                        width="100%"
                                                        value={field.value}
                                                        name="editor"
                                                        onChange={
                                                            field.onChange
                                                        }
                                                        editorProps={{
                                                            $blockScrolling: true
                                                        }}
                                                    />
                                                );
                                            }
                                            return (
                                                <TextField
                                                    key={field.key}
                                                    field={field}
                                                />
                                            );
                                        })}
                                    </div>
                                )}

                                <br />

                                <AdvancedLink
                                    onClick={() =>
                                        store.timerStore.toggleAdvancedPanel()
                                    }
                                    isExpanded={
                                        store.timerStore.isAdvancedPanelOpen
                                    }
                                >
                                    <span>Advanced options</span>
                                </AdvancedLink>

                                {store.timerStore.isAdvancedPanelOpen && (
                                    <div>
                                        <br />
                                        <MaterialCheckbox
                                            field={store.timerStore.form.$('isReplayLooped')}
                                        />
                                        <TextField
                                            field={store.timerStore.form.$(
                                                "ticksNumber"
                                            )}
                                        />

                                        {datasetFilterStore.shouldShowDatasetFilter && (
                                            <div>
                                                {
                                                    <div>
                                                        <SubTitle>
                                                            Dataset filter
                                                        </SubTitle>
                                                        <DatasetFilterContainer>
                                                            <FilterTypeSelect
                                                                field={
                                                                    datasetFilterStore
                                                                        .form.$
                                                                        .type
                                                                }
                                                            />
                                                            {datasetFilterStore.isCustomFunctionSelected && (
                                                                <div>
                                                                    <FormstateEditorField
                                                                        field={
                                                                            datasetFilterStore
                                                                                .form
                                                                                .$
                                                                                .jsFunction
                                                                        }
                                                                    />
                                                                </div>
                                                            )}
                                                            {datasetFilterStore.isDatasetEntrySelected && (
                                                                <div>
                                                                    <FilterTypeSelect
                                                                        field={
                                                                            datasetFilterStore
                                                                                .form
                                                                                .$
                                                                                .position
                                                                        }
                                                                        label="position"
                                                                    />
                                                                    <FormstateField
                                                                        field={
                                                                            datasetFilterStore
                                                                                .form
                                                                                .$
                                                                                .value
                                                                        }
                                                                        label="value"
                                                                    />
                                                                </div>
                                                            )}
                                                        </DatasetFilterContainer>
                                                    </div>
                                                }
                                            </div>
                                        )}
                                    </div>
                                )}
                            </StepInnerContainer>
                        </StepContainer>
                    )}

                    {currentStepId === steps.devices && (
                        <StepContainer>
                            <StepLabel component="legend">
                                <b>
                                    Step {stepNumber} of {stepsCount}. Select
                                    devices
                                </b>
                            </StepLabel>
                            {
                                <ViewLayout
                                    controlPanel={<DevicesControlPanel />}
                                    list={
                                        <DevicesList
                                            selectable={true}
                                            selectedIds={
                                                store.session.devicesIds
                                            }
                                            onSelect={store.setDevice}
                                        />
                                    }
                                />
                            }
                        </StepContainer>
                    )}

                    {currentStepId === steps.injector && (
                        <StepContainer>
                            <StepLabel component="legend">
                                <b>
                                    Step {stepNumber} of {stepsCount}. Select
                                    device injection rule
                                </b>
                            </StepLabel>
                            <StepInnerContainer>
                                {store.devicesPopulationStore.form && (
                                    <div>
                                        {store.devicesPopulationStore.form.map(
                                            field => {
                                                let type =
                                                    field.$type || field.type;
                                                if (type === "select") {
                                                    return (
                                                        <SelectField
                                                            key={field.key}
                                                            field={field}
                                                        />
                                                    );
                                                }
                                                return (
                                                    <TextField
                                                        key={field.key}
                                                        field={field}
                                                    />
                                                );
                                            }
                                        )}
                                    </div>
                                )}
                            </StepInnerContainer>
                        </StepContainer>
                    )}

                    {currentStepId === steps.rules && (
                        <StepContainer>
                            <StepLabel component="legend">
                                <b>
                                    Step {stepNumber} of {stepsCount}. Data
                                    processing rules
                                </b>
                            </StepLabel>
                            <StepInnerContainer>
                                {injectRulesStore.shouldShowSchema && (
                                    <Property
                                        value={injectRulesStore.displaySchema}
                                        isRuleMode={true}
                                    />
                                )}

                                {!injectRulesStore.shouldShowSchema && (
                                    <InjectionFunctionEditor
                                        field={
                                            injectRulesStore.functionForm.$
                                                .function
                                        }
                                    />
                                )}

                                {/* <SchemaNode
                                    form={injectRulesStore.form}
                                    isRoot={true}
                                    isRuleMode={true}
                                /> */}

                                <Dialog
                                    maxWidth="md"
                                    fullWidth={true}
                                    open={injectRulesStore.isEditorModalOpen}
                                >
                                    <DialogTitle>
                                        Edit custom function
                                    </DialogTitle>
                                    <DialogContent>
                                        {injectRulesStore.currentEditorProperty && (
                                            <AceEditor
                                                mode="javascript"
                                                theme="monokai"
                                                width="100%"
                                                value={injectRulesStore.currentEditorProperty.rule.getParameter(
                                                    "function"
                                                )}
                                                name="editor"
                                                onChange={
                                                    store.injectRulesStore
                                                        .onEditorChange
                                                }
                                                editorProps={{
                                                    $blockScrolling: true
                                                }}
                                            />
                                        )}
                                    </DialogContent>
                                    <DialogActions>
                                        <Button
                                            color="accent"
                                            onClick={
                                                store.injectRulesStore
                                                    .onEditorCancel
                                            }
                                        >
                                            Cancel
                                        </Button>
                                        <Button
                                            color="primary"
                                            onClick={
                                                store.injectRulesStore
                                                    .onEditorSave
                                            }
                                        >
                                            Save
                                        </Button>
                                    </DialogActions>
                                </Dialog>
                            </StepInnerContainer>
                        </StepContainer>
                    )}

                    {currentStepId === steps.system && (
                        <StepContainer>
                            <StepLabel component="legend">
                                <b>
                                    Step {stepNumber} of {stepsCount}. Select
                                    target system
                                </b>
                            </StepLabel>
                            <ViewLayout
                                controlPanel={<SystemsControlPanel />}
                                list={
                                    <SystemsList
                                        selectable={true}
                                        selectedIds={[session.targetSystem.id]}
                                        onSelect={session.setSystem}
                                    />
                                }
                            />
                        </StepContainer>
                    )}

                    {currentStepId === steps.finalize && (
                        <StepContainer>
                            <StepLabel component="legend">
                                <b>
                                    Step {stepNumber} of {stepsCount}. Finalize
                                    form
                                </b>
                            </StepLabel>
                            <StepInnerContainer>
                                {
                                    <NameField
                                        field={store.finalizeFormStore.form.$(
                                            "name"
                                        )}
                                    />
                                }
                            </StepInnerContainer>
                        </StepContainer>
                    )}
                </StepOuterContainer>
            </SessionWizardLayout>
        );
    }
}
