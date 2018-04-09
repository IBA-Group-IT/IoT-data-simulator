import React, { Component } from "react";
import Icon from "material-ui/Icon";

import logoUrl from 'images/iba-group.png';

class IbaLogo extends Component {
    render() {
        return (
            <img src={logoUrl} {...this.props} />
        );
    }
}

export default IbaLogo;
