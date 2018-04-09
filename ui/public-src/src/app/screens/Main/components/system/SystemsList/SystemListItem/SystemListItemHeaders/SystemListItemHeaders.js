import React, { Component } from "react";
import { inject, observer } from "mobx-react";
import glamorous from "glamorous";

import {
    DetailsTableRow,
    DetailsTableCell,
    DetailsTableTitle
} from "components/EntityList/style";

@observer
class SystemListItemHeaders extends Component {
    constructor(props) {
        super(props);
    }

    render() {
        let { headers = [] } = this.props;

        return (
            <DetailsTableRow>
                <DetailsTableTitle>Headers:</DetailsTableTitle>
                <DetailsTableCell>{headers.length}</DetailsTableCell>
            </DetailsTableRow>
        );
    }
}

export default SystemListItemHeaders;
