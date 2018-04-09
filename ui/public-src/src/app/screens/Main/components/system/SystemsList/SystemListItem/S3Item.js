import React, { Component } from "react";
import { inject, observer } from "mobx-react";
import glamorous from "glamorous";

import {
    DetailsTableRow,
    DetailsTableCell,
    DetailsTableTitle
} from "components/EntityList/style";

@observer
class S3Item extends Component {
    constructor(props) {
        super(props);
    }

    render() {
        let { system } = this.props;

        return (
            <div>
                <DetailsTableRow>
                    <DetailsTableTitle>Dataset:</DetailsTableTitle>
                    <DetailsTableCell>{system.dataset}</DetailsTableCell>
                </DetailsTableRow>
            </div>
        );
    }
}

export default S3Item;
