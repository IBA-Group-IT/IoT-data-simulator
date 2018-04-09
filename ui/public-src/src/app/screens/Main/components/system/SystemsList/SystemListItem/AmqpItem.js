import React, { Component } from "react";
import { inject, observer } from "mobx-react";
import glamorous from "glamorous";

import {
    DetailsTableRow,
    DetailsTableCell,
    DetailsTableTitle
} from "components/EntityList/style";

@observer
class AmqpItem extends Component {
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
                    <DetailsTableTitle>Queue:</DetailsTableTitle>
                    <DetailsTableCell>{system.queue} </DetailsTableCell>
                </DetailsTableRow>
            </div>
        );
    }
}

export default AmqpItem;
