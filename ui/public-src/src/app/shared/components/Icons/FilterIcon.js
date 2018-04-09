import React, { Component } from "react";
import Icon from "material-ui/Icon";

class FilterIcon extends Component {
    render() {
        return (
            <Icon className="fa fa-filter" {...this.props} />
        );
    }
}

export default FilterIcon;
