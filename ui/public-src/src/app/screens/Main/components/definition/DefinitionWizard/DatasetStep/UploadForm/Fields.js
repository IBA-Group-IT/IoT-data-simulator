import React, { Component } from "react";
import { inject, observer } from "mobx-react";
import Button from "material-ui/Button";

import glamorous from "glamorous";
import { css } from "glamor";
import MaterialTextField from "components/Form/inputs/MaterialTextField";
import MaterialFile from 'components/Form/inputs/MaterialFile';

const FormInnerContainer = glamorous.div({
    display: "flex",
    flexWrap: "wrap"
});

const textInputStyle = {
    margin: "5px"
};

const submitBtnStyle = {
    marginTop: "20px"
};

const ControlsLayout = glamorous.div({
    padding: '10px 1px'
});

export default observer(({ form, onSubmit, onDrop, file }) => (
    <form onSubmit={(event) => onSubmit(event, form)}>
        <ControlsLayout>
            <MaterialFile 
                label="Select file"
                field={form.$("datasetUpload")} 
                onDrop={onDrop} 
                file={file} 
            />
        </ControlsLayout>
    </form>
));
