import React, { Component } from "react";
import Icon from "material-ui/Icon";

class PauseIcon extends Component {
    render() {
        return (
            <Icon className="fa fa-pause" {...this.props} />
        );
    }
}

export default PauseIcon;
