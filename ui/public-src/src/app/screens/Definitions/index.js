import React, { Component } from "react";
import { inject, observer } from "mobx-react";
import glamorous from "glamorous";
import { css } from "glamor";

import DefinitionsList from "../Main/components/definition/DefinitionsList";
import ControlPanel from "../Main/components/definition/ControlPanel";
import ViewLayout from "components/ViewLayout";

@inject("store")
@observer
export default class DefinitionsScreen extends Component {
    constructor(props) {
        super(props);
    }
    render() {
        return (
            <ViewLayout
                controlPanel={<ControlPanel />}
                list={
                    <DefinitionsList
                        items={this.props.store.definitionsStore.filteredItems}
                    />
                }
            />
        );
    }
}
