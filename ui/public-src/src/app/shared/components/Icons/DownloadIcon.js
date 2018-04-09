import React, { Component } from "react";
import Icon from "material-ui/Icon";

class DownloadIcon extends Component {
    render() {
        return (
            <Icon className="fa fa-download" {...this.props} />
        );
    }
}

export default DownloadIcon;
