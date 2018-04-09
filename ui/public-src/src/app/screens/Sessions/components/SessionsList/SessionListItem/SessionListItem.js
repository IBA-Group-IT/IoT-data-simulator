import React, { Component } from "react";
import { inject, observer } from "mobx-react";
import glamorous from "glamorous";
import { css } from "glamor";

import IconButton from "material-ui/IconButton";

import PlayIcon from "components/Icons/PlayIcon";
import RepeatIcon from "components/Icons/RepeatIcon";
import StopIcon from "components/Icons/StopIcon";
import PauseIcon from "components/Icons/PauseIcon";
import DownloadIcon from "components/Icons/DownloadIcon";

import {
    Title,
    Container,
    DetailsTable,
    DetailsTableRow,
    DetailsTableCell,
    DetailsTableTitle
} from "components/EntityList/style";

const SmallIconButton = glamorous(IconButton, {
    withProps: {
        style: {
            fontSize: "16px",
            width: "36px",
            height: "44px"
        }
    }
})();

const SessionStateWrapper = glamorous.div({
    padding: "0px 15px",
    background: "rgb(244,244,244)",
    position: "absolute",
    bottom: "0px",
    width: "100%",
    display: "flex",
    justifyContent: "space-between"
});

const StateControls = glamorous.div({});

const State = glamorous.div({
    display: "flex",
    margin: "auto 0"
});

const StateLabel = glamorous.div({
    margin: "auto 0px",
    color: "#707070",
    padding: "5px",
    textTransform: "capitalize"
});

const blinkAnimation = (color, radius = "16px") => {
    return css
        .keyframes({
            "0%": {
                boxShadow: `0 0 0 ${color}`
            },
            "5%": {
                boxShadow: `0 0 ${radius} ${color}`
            }
        })
        .toString();
};

const stateColorMap = {
    default: { backgroundColor: "#aaaaa7" },
    failed: { backgroundColor: "#de1010" },
    running: {
        backgroundColor: "#01579b",
        animation: `${blinkAnimation("#01579b", "18px")} 2s linear infinite`
    },
    completed: { backgroundColor: "#0c9e0c" },
    paused: { backgroundColor: "#f9bd14" }
};

const Indicator = glamorous.div(
    {
        width: "16px",
        height: "16px",
        display: "inline-block",
        borderRadius: "50%",
        backgroundColor: "#aaaaa7",
        margin: "auto 5px"
    },
    props =>
        stateColorMap[props.state]
            ? stateColorMap[props.state]
            : stateColorMap.default
);

const ExportButtonLayout = glamorous.div({
    position: "absolute",
    right: "44px",
    top: "4px"
});

const ExportLink = glamorous.a({
    color: "inherit",
    textDecoration: "none"
});

@inject("store")
@observer
class SessionListItem extends Component {
    constructor(props) {
        super(props);
    }

    onStart = () => {
        let { session } = this.props;
        let { sessionsStore } = this.props.store;
        sessionsStore.startSession(session.id);
    };

    onResume = () => {
        let { session } = this.props;
        let { sessionsStore } = this.props.store;
        sessionsStore.resumeSession(session.id);
    };

    onPause = () => {
        let { session } = this.props;
        let { sessionsStore } = this.props.store;
        sessionsStore.pauseSession(session.id);
    };

    onStop = () => {
        let { session } = this.props;
        let { sessionsStore } = this.props.store;
        sessionsStore.stopSession(session.id);
    };

    onRestart = () => {
        let { session } = this.props;
        let { sessionsStore } = this.props.store;
        sessionsStore.restartSession(session.id);
    };

    renderPlayAction() {
        let { session } = this.props;
        let button = null;
        if (session.canRun) {
            button = (
                <SmallIconButton onClick={this.onStart}>
                    <PlayIcon />
                </SmallIconButton>
            );
        } else if (session.canPause) {
            button = (
                <SmallIconButton onClick={this.onPause}>
                    <PauseIcon />
                </SmallIconButton>
            );
        } else if (session.canResume) {
            button = (
                <SmallIconButton onClick={this.onResume}>
                    <PlayIcon />
                </SmallIconButton>
            );
        }
        return button;
    }

    render() {
        let { session } = this.props;
        let state = session.state || "Not active";

        return (
            <Container>
                <Title>{session.name}</Title>

                <ExportButtonLayout>
                    <ExportLink
                        href={`/api/sessions/${session.id}/export`}
                        target="_blank"
                    >
                        <SmallIconButton>
                            <DownloadIcon />
                        </SmallIconButton>
                    </ExportLink>
                </ExportButtonLayout>
                <DetailsTable>
                    {session.hasDefinition && (
                        <div>
                            <DetailsTableRow>
                                <DetailsTableTitle>
                                    Data definition:
                                </DetailsTableTitle>
                                <DetailsTableCell>
                                    {session.dataDefinition.name}
                                </DetailsTableCell>
                            </DetailsTableRow>
                            {session.dataDefinition.hasDataset && (
                                <DetailsTableRow>
                                    <DetailsTableTitle>
                                        Dataset:
                                    </DetailsTableTitle>
                                    <DetailsTableCell>
                                        {session.dataDefinition.dataset.name}
                                    </DetailsTableCell>
                                </DetailsTableRow>
                            )}
                        </div>
                    )}

                    <DetailsTableRow>
                        <DetailsTableTitle>Timer:</DetailsTableTitle>
                        <DetailsTableCell>
                            {session.timer.type === "interval" &&
                                `${session.timer.getParameter(
                                    "value"
                                )} ${session.timer.getParameter("metric")}`}
                            {session.timer.type === "dataset_provided" &&
                                session.timer.getParameter("datePosition")}
                            {session.timer.type === "random" &&
                                `Random: ${session.timer.getParameter(
                                    "min"
                                )} - ${session.timer.getParameter(
                                    "max"
                                )} ${session.timer.getParameter("metric")}`}
                            {session.timer.type === "custom_function" &&
                                `Custom function`}
                        </DetailsTableCell>
                    </DetailsTableRow>
                    <DetailsTableRow>
                        <DetailsTableTitle>Devices:</DetailsTableTitle>
                        <DetailsTableCell>
                            {session.devices.length}
                        </DetailsTableCell>
                    </DetailsTableRow>
                    {!!session.devices.length && (
                        <DetailsTableRow>
                            <DetailsTableTitle>
                                Injection rule:
                            </DetailsTableTitle>
                            <DetailsTableCell>
                                {session.deviceInjector &&
                                    session.deviceInjector.rule}
                            </DetailsTableCell>
                        </DetailsTableRow>
                    )}
                    <DetailsTableRow>
                        <DetailsTableTitle>Target system:</DetailsTableTitle>
                        <DetailsTableCell>
                            {session.targetSystem.name} ({session.targetSystem.type})
                        </DetailsTableCell>
                    </DetailsTableRow>
                </DetailsTable>
                <SessionStateWrapper>
                    <State>
                        <Indicator state={state} />
                        <StateLabel>{state}</StateLabel>
                    </State>
                    <StateControls>
                        {this.renderPlayAction()}
                        <SmallIconButton
                            disabled={!session.canStop}
                            onClick={this.onStop}
                        >
                            <StopIcon />
                        </SmallIconButton>
                        <SmallIconButton
                            disabled={!session.canRestart}
                            onClick={this.onRestart}
                        >
                            <RepeatIcon />
                        </SmallIconButton>
                    </StateControls>
                </SessionStateWrapper>
            </Container>
        );
    }
}

export default SessionListItem;
