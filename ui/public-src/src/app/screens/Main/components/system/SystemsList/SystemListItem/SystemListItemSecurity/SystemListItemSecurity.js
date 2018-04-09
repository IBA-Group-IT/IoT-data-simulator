
import React, { Component } from "react";
import { inject, observer } from "mobx-react";
import glamorous from "glamorous";

import {
    DetailsTableRow,
    DetailsTableCell,
    DetailsTableTitle
} from "components/EntityList/style";

@observer
class SystemListItemSecurity extends Component { 

    constructor(props) {
        super(props);
    }


    render() {
        let { security } = this.props;
        
        return (
            <DetailsTableRow>
                {
                    security.type && (
                        <div>
                            <DetailsTableTitle>Security type: </DetailsTableTitle>
                            <DetailsTableCell>{ security.type }</DetailsTableCell>
                        </div>
                    )
                }
            </DetailsTableRow>
        )
    }
    
}

export default SystemListItemSecurity;
