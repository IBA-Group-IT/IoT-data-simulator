import React, { Component } from "react";
import Icon from "material-ui/Icon";

class UploadIcon extends Component {
    render() {
        return (
            <Icon className="fa fa-upload" {...this.props} />
        );
    }
}

export default UploadIcon;
