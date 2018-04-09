import React, { Component } from "react";
import { observable, action, computed } from "mobx";
import { inject, observer } from "mobx-react";
import glamorous from "glamorous";
import { css} from 'glamor'

import Button from "material-ui/Button";
import Radio, { RadioGroup } from "material-ui/Radio";
import TextField from "material-ui/TextField";
import { FormLabel, FormControl, FormControlLabel } from "material-ui/Form";
import KeyboardArrowLeft from "material-ui-icons/KeyboardArrowLeft";
import KeyboardArrowRight from "material-ui-icons/KeyboardArrowRight";
import Dialog, {
    DialogActions,
    DialogContent,
    DialogTitle
} from "material-ui/Dialog";

import Stepper from "../../Stepper";
import DatasetStep from "./DatasetStep";
import SchemaStep from "./SchemaStep";
import FinalizeStep from "./FinalizeStep";

import JsonSchema from "models/schema/JsonSchema";
import { throttle } from "util/function";
import SelectSchemaModal from './SchemaStep/SelectSchemaModal';

//////////////////////////////////////////////////////////////////////
const DefinitionWizardLayout = glamorous.div({
    display: "flex",
    flexDirection: "column",
    minHeight: "0",
    height: "100%",
    width: "100%",
    position: "relative"
});

const StepOuterContainer = glamorous.div({
    display: "flex",
    minHeight: "0",
    height: "100%",
    overflow: "auto",
    transform: "translate3d(0, 0, 0) scale(1)"
});

const NextButton = glamorous(Button)({});
NextButton.defaultProps = {
    raised: true,
    style: {
        marginRight: "15px"
    }
};

const StepContainer = glamorous.div({
    padding: "30px",
    width: "100%"
});

const StepActionsContainer = glamorous.div({
    margin: "15px 0"
});

const StepTitle = glamorous(FormLabel, {
    withProps: {
        style: {
            padding: "10px 0"
        }
    }
})({});

const CopyFromButtonLayout = glamorous.span({});
const CopyFromButton = glamorous(Button, {
    withProps: {
        style: {
            fontSize: "13px",
            background: "rgb(230, 230, 230)"
        }
    }
})();

const SchemaSkipSection = glamorous.div({
    display: 'flex',
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'flex-end',
    flex: '0 0 auto'
});

const OrSpan = glamorous.span({
    fontSize: '13px',
    marginRight: '10px'
});

const BackButton = glamorous(Button, {
    withProps: { 
        style: {
            flex: '0 0 85px'
        }
    }
})()

@observer
export default class DefinitionWizard extends React.Component {

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

    render() {
        let { store, onComplete } = this.props;
        let {
            setSelectionMethod,
            selectionMethod,
            fileToUpload,
            datasets,
            wizardCurrentStep: step,
            definition,
            isSchemaSkipAllowed
        } = store;

        return (
            <DefinitionWizardLayout>
                
                <Stepper
                    steps={3}
                    activeStep={step}
                    nextButton={
                        <SchemaSkipSection>
                            {isSchemaSkipAllowed && (
                                <span>
                                    <Button onClick={store.onSchemaSkip} dense>Skip</Button>
                                    <OrSpan> or </OrSpan>
                                </span>
                            )}
                            <Button
                                dense
                                onClick={store.nextHandler}
                                disabled={!store.isNextActive}
                            >
                                {store.nextLabel}
                                {<KeyboardArrowRight />}
                            </Button>
                        </SchemaSkipSection>
                    }
                    backButton={
                        <BackButton
                            dense
                            onClick={store.backHandler}
                            disabled={!store.isBackActive}
                        >
                            {<KeyboardArrowLeft />}
                            Back
                        </BackButton>
                    }
                />

                <StepOuterContainer>
                    {step === 0 && (
                        <StepContainer>
                            <StepTitle component="legend">
                                <b>1. Select/Create dataset</b>
                            </StepTitle>
                            <DatasetStep store={store} />
                        </StepContainer>
                    )}

                    {step === 1 && (
                        <StepContainer>
                            <StepTitle component="legend">
                                <b>
                                    2. Create schema
                                    {!store.definition.id && (
                                        <span>
                                            <span> or </span>
                                            <CopyFromButtonLayout>
                                                <CopyFromButton
                                                    onClick={this.openConfirm}
                                                >
                                                    Copy from definition
                                                </CopyFromButton>
                                            </CopyFromButtonLayout>
                                        </span>
                                    )}
                                </b>
                            </StepTitle>

                            <SchemaStep store={store} />

                            {!definition.id && (
                                <Dialog
                                    ignoreBackdropClick
                                    ignoreEscapeKeyUp
                                    open={this.isConfirmShown}
                                >
                                    <DialogTitle>Warning</DialogTitle>
                                    <DialogContent>
                                        When definition selected, all your
                                        current schema changes will be lost. Do
                                        you want to proceed?
                                    </DialogContent>
                                    <DialogActions>
                                        <Button
                                            onClick={this.closeConfirm}
                                            color="primary"
                                        >
                                            No
                                        </Button>
                                        <Button
                                            onClick={this.onConfirm}
                                            color="accent"
                                        >
                                            Yes
                                        </Button>
                                    </DialogActions>
                                </Dialog>
                            )}

                            <SelectSchemaModal key="select-schema-modal" store={store} />
                        </StepContainer>
                    )}

                    {step === 2 && (
                        <StepContainer>
                            <StepTitle component="legend">
                                <b>3. Finalize form</b>
                            </StepTitle>
                            <FinalizeStep store={store} />
                        </StepContainer>
                    )}
                </StepOuterContainer>
            </DefinitionWizardLayout>
        );
    }
}
