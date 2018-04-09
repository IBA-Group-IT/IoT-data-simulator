import React, { Component } from "react";
import { inject, observer } from "mobx-react";
import glamorous from "glamorous";
import { css } from 'glamor';

import ViewLayout from 'components/ViewLayout';
import ControlPanel from '../Main/components/system/ControlPanel';
import SystemsList from '../Main/components/system/SystemsListContainer';

export default class SystemsScreen extends Component {
    constructor(props) {
        super(props);
    }
    render() {
        return (
            <ViewLayout
                controlPanel={<ControlPanel />}
                list={<SystemsList />}
            />
        )
    }
}
