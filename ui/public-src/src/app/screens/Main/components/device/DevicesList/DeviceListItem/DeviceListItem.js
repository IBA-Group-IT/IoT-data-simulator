import React, { Component } from "react";
import { inject, observer } from "mobx-react";
import glamorous from "glamorous";

import {
    Title,
    Container,
    DetailsTable,
    DetailsTableRow,
    DetailsTableCell,
    DetailsTableTitle
} from "components/EntityList/style";

const DeviceType = glamorous.div({
    color: "#01579b",
    fontSize: "14px",
    marginTop: "5px"
});

const TypeLayout = glamorous.div({
    marginTop: "10px",
    height: "60px",
    overflow: "auto"
});

const Section = glamorous.div({
    marginTop: "15px"
});

@observer
class DeviceListItem extends Component {
    constructor(props) {
        super(props);
    }

    render() {
        let { device } = this.props;

        return (
            <Container>
                <Title>{device.name}</Title>
                <DetailsTable>
                    {device.properties && (
                        <DetailsTableRow>
                            <DetailsTableTitle>
                                Properties:
                            </DetailsTableTitle>
                            <DetailsTableCell>{device.properties.length}</DetailsTableCell>
                        </DetailsTableRow>
                    )}
                    {device.targetSystems && (
                        <DetailsTableRow>
                            <DetailsTableTitle>
                                Target systems:
                            </DetailsTableTitle>
                            <DetailsTableCell>{device.targetSystems.length}</DetailsTableCell>
                        </DetailsTableRow>
                    )}
                </DetailsTable>
            </Container>
        );
    }
}

export default DeviceListItem;
