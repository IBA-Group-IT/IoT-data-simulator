import React, { Component } from "react";
import { inject, observer } from "mobx-react";
import glamorous from "glamorous";
import { css } from 'glamor';

import ViewLayout from 'components/ViewLayout';
import ControlPanel from '../Main/components/device/ControlPanel';
import DevicesList from '../Main/components/device/DevicesListContainer';


export default class DevicesScreen extends Component {
    constructor(props) {
        super(props);
    }
    render() {
        return (
            <ViewLayout
                controlPanel={<ControlPanel />}
                list={<DevicesList />}
            />
        );
    }
}

