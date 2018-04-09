import React, { Component } from "react";
import { observer } from "mobx-react";
import TextField from "material-ui/TextField";
import Button from 'material-ui/Button'

import glamorous from "glamorous";
import { css } from "glamor";

const FileName = glamorous.div({
    display: 'inline-block',
    marginLeft: '10px'
});

@observer
export default class MaterialFile extends Component {
    constructor(props) {
        super(props);
    }

    _openFileDialog = () => {
        var fileUploadDom = this.refs.fileUpload;
        
        /**
         * Resetting input value to allow possibility
         * to select same file
         */
        fileUploadDom.value = '';

        fileUploadDom.click();
    }

    render() {
        let { props } = this;
        let { onDrop, field, file, children, style, ...rest } = props;
        return (
            <div style={style}>
                <div onClick={this._openFileDialog}>
                    {children}
                </div>
                <input
                    onChange={onDrop}
                    ref="fileUpload"
                    type="file"
                    style={{ display: "none" }}
                />
            </div>
        );
    }
}