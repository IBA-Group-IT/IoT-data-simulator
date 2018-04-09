import React, { Component } from "react";
import Icon from "material-ui/Icon";

class MagnifierIcon extends Component {
    render() {
        return (
            <Icon className="fa fa-search" {...this.props} />
        );
    }
}

export default MagnifierIcon;
