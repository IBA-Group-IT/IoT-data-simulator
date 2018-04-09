import React, { Component } from "react";
import { observable } from 'mobx';
import { inject, observer } from "mobx-react";

import fileUtils from 'util/file';

import form from "./formInstance";
import Fields from "./Fields";
import Snackbar from "material-ui/Snackbar";

@observer
class UploadFormComponent extends Component {
    @observable isErrorShown = false;
    @observable error = null;

    constructor(props) {
        super(props);
    }

    showError = error => {
        this.isErrorShown = true;
        this.error = error;
        setTimeout(() => {
            this.isErrorShown = false;
            this.error = null;
        }, 3000);
    };

    hideError = () => {
        this.isErrorShown = false;
        this.error = null;
    }

    onFileDrop = event => {
        let file = event.target.files[0];

        let extension = fileUtils.getFileExtension(file)
        if (extension !== 'csv' && extension !== 'json') {
            this.showError("Only .csv and .json extensions are supported. Please, select another file");
            return;
        }

        if(this.props.onDrop) {
            this.props.onDrop(file)
        }
    };

    render() {
        let { file } = this.props;
        return (
            <div>
                <Fields
                    onDrop={this.onFileDrop}
                    form={form}
                    file={file}
                />
                <Snackbar
                    anchorOrigin={{
                        vertical: "bottom",
                        horizontal: "left"
                    }}
                    open={this.isErrorShown}
                    autoHideDuration={3000}
                    onRequestClose={this.handleRequestClose}
                    message={
                        <span id="message-id">{this.error}</span>
                    }
                />
            </div>
        );
    }
}

export default UploadFormComponent;
