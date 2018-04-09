import React, { Component } from "react";
import Icon from "material-ui/Icon";

class CrossIcon extends Component {
    render() {
        return (
            <Icon className="fa fa-times" {...this.props} />
        );
        
    }
}

export default CrossIcon;
