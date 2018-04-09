import React, { Component } from "react";
import { inject, observer } from "mobx-react";
import glamorous from "glamorous";
import { css } from "glamor";

import ViewLayout from "components/ViewLayout";
import SessionsList from "./components/SessionsList";
import ControlPanel from "./components/ControlPanel";
import Console from "./components/Console";

import SplitPane from "react-split-pane";

let Pane = glamorous(SplitPane)({
    "& .Resizer": {
        background: "#fff",
        opacity: ".2",
        zIndex: "1",
        MozBoxSizing: "border-box",
        WebkitBoxSizing: "border-box",
        boxSizing: "border-box",
        MozBackgroundClip: "padding",
        WebkitBackgroundClip: "padding",
        backgroundClip: "padding-box"
    },
    "& .Resizer:hover": {
        WebkitTransition: "all 2s ease",
        transition: "all 2s ease"
    },
    "& .Resizer.horizontal": {
        height: "11px",
        margin: "-5px 0",
        borderTop: "5px solid rgba(255, 255, 255, 0)",
        borderBottom: "5px solid rgba(255, 255, 255, 0)",
        cursor: "row-resize",
        width: "100%"
    },
    "& .Resizer.horizontal:hover": {
        borderTop: "5px solid rgba(255, 255, 255, 0.5)",
        borderBottom: "5px solid rgba(0, 0, 0, 0.5)"
    },
    "& .Resizer.disabled": {
        cursor: "not-allowed"
    },
    "& .Resizer.disabled:hover": {
        borderColor: "transparent"
    }
});

@inject("store")
@observer
export default class SessionsScreen extends Component {
    constructor(props) {
        super(props);
    }
    render() {
        let { sessionsScreenStore } = this.props.store;

        return (
            <ViewLayout
                controlPanel={<ControlPanel />}
                list={
                    <Pane
                        split="horizontal"
                        primary="second"
                        defaultSize={sessionsScreenStore.consoleSize}
                        size={sessionsScreenStore.consoleSize}
                        pane1Style={{ overflow: "auto" }}
                        onChange={ size => sessionsScreenStore.setConsoleSize(size) }
                        maxSize={-15}
                    >
                        <SessionsList />
                        <Console
                            expanded={sessionsScreenStore.consoleExpanded}
                        />
                    </Pane>
                }
            />
        );
    }
}
