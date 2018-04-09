
import React, { Component } from "react";
import { inject, observer } from "mobx-react";
import glamorous from "glamorous";

import {
    DetailsTableRow,
    DetailsTableCell,
    DetailsTableTitle
} from "components/EntityList/style";


@observer
class WebsocketItem extends Component { 

    constructor(props) {
        super(props);
    }

    render() {
        let { system } = this.props;
        
        return (
            <DetailsTableRow>
                <DetailsTableTitle>URL:</DetailsTableTitle>
                <DetailsTableCell>{system.url}</DetailsTableCell>
            </DetailsTableRow>
        )
    }
    
}

export default WebsocketItem;
