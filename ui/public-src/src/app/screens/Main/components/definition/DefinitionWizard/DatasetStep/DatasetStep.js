import React, { Component } from "react";
import { computed } from "mobx";
import { inject, observer } from "mobx-react";
import glamorous from "glamorous";

import Radio, { RadioGroup } from "material-ui/Radio";
import TextField from "material-ui/TextField";
import { FormLabel, FormControl, FormControlLabel } from "material-ui/Form";
import CircularProgress from 'material-ui/Progress/CircularProgress';


import DatasetsList from "./DatasetsList";
import BucketForm from "./BucketForm2";
import UploadForm from "./UploadForm";

import { DatasetSelectionMethods } from 'constants/definitionWizard';

//////////////////////////////////////////////////////////////////////

const PROGRESS_SIZE = 80;
const PROGRESS_THICKNESS = 2.6;

const ProgressLayout = glamorous.div({
    position: 'absolute',
    margin: 'auto',
    top: '0',
    right: '0',
    bottom: '0',
    left: '0',
    zIndex: '99',
    
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'center',

    background: 'rgba(0, 48, 70, 0.16)',

    '& > *': {
        position: 'absolute'
    }
});

const PercentIndicator = glamorous.div({
    position: 'absolute',
    margin: 'auto'
});

const CircularIndicator = glamorous(CircularProgress)({
    '& circle': {
        transition: 'all 100ms cubic-bezier(0.4, 0, 0.2, 1) 0ms !important'
    }
})

const CircularIndicatorPlaceholder = glamorous(CircularProgress, {
    withProps: {
        style: { color: 'rgba(0, 0, 0, 0.1)'},
    }
})(({ size }) => ({
    width: size + 'px',
    height: size + 'px'
}))

const MethodSelectionRadioGroup = glamorous(RadioGroup)({});
MethodSelectionRadioGroup.defaultProps = {
    style: {
        margin: "20px 0 0 0",
        display: "flex",
        flexDirection: "row"
    }
};

const MethodSelectionRadioButton = glamorous(Radio)({});
MethodSelectionRadioButton.defaultProps = {
    style: {
        width: "auto",
        marginRight: "15px"
    }
};

const DatasetsListLayout = glamorous.div({
    marginTop: "20px"
});

@observer
export default class DatasetStep extends React.Component {
    
    render() {
        let { store } = this.props;

        let {
            setSelectionMethod,
            selectionMethod,
            fileToUpload,
            datasets,
            selectedDataset,
            isDatasetUploading,
            uploadingProgress,
            deleteDataset,
            setFileToUpload
        } = store;

        return (
            <div>
                { 
                    isDatasetUploading && (
                        <ProgressLayout>
                            <PercentIndicator>
                            {
                                `${uploadingProgress}%`
                            }
                            </PercentIndicator>
                            <CircularIndicatorPlaceholder thickness={PROGRESS_THICKNESS} size={PROGRESS_SIZE} mode="determinate" value={100} /> 
                            <CircularIndicator thickness={PROGRESS_THICKNESS} size={PROGRESS_SIZE} mode="determinate" value={uploadingProgress} />
                        </ProgressLayout>
                    )
                } 

                <FormControl component="fieldset" required>
                    <MethodSelectionRadioGroup
                        name="datasetSelectionMethod"
                        onChange={(event, value) => {
                            setSelectionMethod(value);
                            // Fix for modal window resizing
                            setTimeout(() => {
                                window.dispatchEvent(new Event("resize"));
                            }, 0);
                        }}
                        value={selectionMethod}
                    >
                        <FormControlLabel
                            value={DatasetSelectionMethods.existing}
                            control={<Radio />}
                            label="Existing dataset"
                        />
                        <FormControlLabel
                            value={DatasetSelectionMethods.bucket}
                            control={<Radio />}
                            label="S3 bucket"
                        />
                        <FormControlLabel
                            value={DatasetSelectionMethods.upload}
                            control={<Radio />}
                            label="Upload new"
                        />
                    </MethodSelectionRadioGroup>
                </FormControl>

                {selectionMethod === DatasetSelectionMethods.existing && (
                    <div>
                        <DatasetsListLayout>
                            <DatasetsList
                                items={datasets}
                                onRowSelection={store.onDatasetSelection}
                                selectedItemId={selectedDataset && selectedDataset.id}
                                onDelete={deleteDataset}
                            />
                        </DatasetsListLayout>
                    </div>
                )}

                {selectionMethod === DatasetSelectionMethods.bucket && (
                    <div>
                        <BucketForm store={store} />
                    </div>
                )}

                {selectionMethod === DatasetSelectionMethods.upload && (
                    <div>
                        <UploadForm
                            file={fileToUpload}
                            onDrop={setFileToUpload}
                        />
                    </div>
                )}
            </div>
        );
    }
}
