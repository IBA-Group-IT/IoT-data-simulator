import React, { Component } from "react";
import Icon from "material-ui/Icon";

class DeleteIcon extends Component {
    render() {
        return (
            <Icon className="fa fa-trash" {...this.props} />
        );
    }
}

export default DeleteIcon;
