import React, { Component } from "react";
import { inject, observer } from "mobx-react";
import glamorous from "glamorous";
import { css } from "glamor";
import DevTools from "mobx-react-devtools";

import Header from "./components/Header";
import View from "./components/View";

import CreateDefinitionModal from "./components/definition/CreateDefinitionModal";
import EditDefinitionModal from "./components/definition/EditDefinitionModal";
import DeleteDefinitionModal from "./components/definition/DeleteDefinitionModal";
import DeleteDatasetModal from './components/definition/DeleteDatasetModal';

import CreateSystemModal from "./components/system/CreateSystemModal";
import EditSystemModal from "./components/system/EditSystemModal";
import DeleteSystemModal from "./components/system/DeleteSystemModal";

import CreateDeviceModal from "./components/device/CreateDeviceModal";
import EditDeviceModal from "./components/device/EditDeviceModal";
import DeleteDeviceModal from "./components/device/DeleteDeviceModal";

import CreateDeviceSystemModal from "./components/device/CreateSystemModal";
import EditDeviceSystemModal from "./components/device/EditSystemModal";
import DeleteDeviceSystemModal from "./components/device/DeleteSystemModal";

import CreateSessionModal from "./components/session/CreateSessionModal";
import EditSessionModal from "./components/session/EditSessionModal";
import DeleteSessionModal from "./components/session/DeleteSessionModal";
import ErrorPanel from './components/ErrorPanel';


const StyledMainScreen = glamorous.div({
    display: "flex",
    justifyContent: "center",
    alignItems: "center",
    width: "100%",
    height: "100%",
    background: "white"
});

const InnerContainerLayout = glamorous.div({
    width: "100%",
    height: "100%",
    position: "relative",
    overflow: "hidden",
    display: "flex",
    flexDirection: "column"
});

@inject("store")
@observer
export default class MainScreen extends Component {
    constructor(props) {
        super(props);
    }
    render() {
        return (
            <StyledMainScreen>
                <InnerContainerLayout>
                    <Header />
                    <View />
                </InnerContainerLayout>

                 

                <CreateDefinitionModal />
                <EditDefinitionModal />
                <DeleteDefinitionModal />
                <DeleteDatasetModal />

                <CreateSystemModal />
                <EditSystemModal />
                <DeleteSystemModal />

                <CreateDeviceModal />
                <EditDeviceModal />
                <DeleteDeviceModal />

                <CreateDeviceSystemModal />
                <EditDeviceSystemModal />
                <DeleteDeviceSystemModal />

                <CreateSessionModal />
                <EditSessionModal />
                <DeleteSessionModal />

                <ErrorPanel />

                 {/* <DevTools />   */}
                
            </StyledMainScreen>
        );
    }
}
