
import React, { Component } from "react";
import { inject, observer } from "mobx-react";
import glamorous from "glamorous";

import {
    DetailsTableRow,
    DetailsTableCell,
    DetailsTableTitle
} from "components/EntityList/style";

@observer
class RestItem extends Component { 

    constructor(props) {
        super(props);
    }

    render() {
        let { system } = this.props;
        
        return (
            <div>
                <DetailsTableRow>
                    <DetailsTableTitle>URL:</DetailsTableTitle>
                    <DetailsTableCell>{system.url} </DetailsTableCell>
                </DetailsTableRow>
                <DetailsTableRow>
                    <DetailsTableTitle>Method:</DetailsTableTitle>
                    <DetailsTableCell>{system.method} </DetailsTableCell>
                </DetailsTableRow>
            </div>
        )
    }
    
}

export default RestItem;
