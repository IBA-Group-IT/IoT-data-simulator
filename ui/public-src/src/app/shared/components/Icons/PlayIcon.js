import React, { Component } from "react";
import Icon from "material-ui/Icon";

class PlayIcon extends Component {
    render() {
        return (
            <Icon className="fa fa-play" {...this.props} />
        );
    }
}

export default PlayIcon;
