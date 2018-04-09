import React, { Component } from "react";
import Icon from "material-ui/Icon";

class PlusIcon extends Component {
    render() {
        return (
            <Icon className="fa fa-plus" {...this.props} />
        );
    }
}

export default PlusIcon;
