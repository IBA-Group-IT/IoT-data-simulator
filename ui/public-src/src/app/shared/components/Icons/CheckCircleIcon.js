import React, { Component } from "react";
import Icon from "material-ui/Icon";

class CheckCircleIcon extends Component {
    render() {
        return (
            <Icon className="fa fa-check-circle" {...this.props} />
        );
    }
}

export default CheckCircleIcon;
