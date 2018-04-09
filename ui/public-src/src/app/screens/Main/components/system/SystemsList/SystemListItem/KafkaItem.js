import React, { Component } from "react";
import { inject, observer } from "mobx-react";
import glamorous from "glamorous";

import {
    DetailsTableRow,
    DetailsTableCell,
    DetailsTableTitle
} from "components/EntityList/style";

@observer
class KafkaItem extends Component {
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
                    <DetailsTableTitle>Topic:</DetailsTableTitle>
                    <DetailsTableCell>{system.topic} </DetailsTableCell>
                </DetailsTableRow>
            </div>
        );
    }
}

export default KafkaItem;
