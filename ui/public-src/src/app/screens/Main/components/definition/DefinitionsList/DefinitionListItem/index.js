import React, { Component } from "react";
import { inject, observer } from "mobx-react";
import glamorous from "glamorous";

import themeConfig from "styles/theme";

import {
    Title,
    Container,
    DetailsTable,
    DetailsTableRow,
    DetailsTableCell,
    DetailsTableTitle
} from "components/EntityList/style";

@observer
class DefinitionItem extends Component {
    constructor(props) {
        super(props);
    }

    render() {
        let { definition } = this.props;

        return (
            <Container>
                <Title>{definition.name}</Title>
                <DetailsTable>
                    {definition.dataset.id && (
                        <DetailsTableRow>
                            <DetailsTableTitle>Dataset:</DetailsTableTitle>
                            <DetailsTableCell>
                                {definition.dataset.name}
                            </DetailsTableCell>
                        </DetailsTableRow>
                    )}
                    {!definition.dataset.id && (
                        <DetailsTableRow>
                            <DetailsTableCell>
                                No dataset selected
                            </DetailsTableCell>
                        </DetailsTableRow>
                    )}
                    {definition.schema && (
                        <DetailsTableRow>
                            <DetailsTableTitle>Schema type:</DetailsTableTitle>
                            <DetailsTableCell>
                                {definition.schema.schemaType}
                            </DetailsTableCell>
                        </DetailsTableRow>
                    )}
                </DetailsTable>
            </Container>
        );
    }
}

export default DefinitionItem;
