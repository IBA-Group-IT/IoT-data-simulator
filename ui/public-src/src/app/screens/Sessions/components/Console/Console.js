import React, { Component } from "react";
import ReactDOM from "react-dom";
import { observable, toJS } from "mobx";
import { observer, inject } from "mobx-react";

import glamorous from "glamorous";
import { css } from "glamor";
import DeleteIcon from "components/Icons/DeleteIcon";

import IconButton from "material-ui/IconButton";
import ChevronUpIcon from "components/Icons/ChevronUpIcon";
import ChevronDownIcon from "components/Icons/ChevronDownIcon";
import EraserIcon from "components/Icons/EraserIcon";
import FilterIcon from "components/Icons/FilterIcon";

import LogEntry from "models/session/LogEntry";
let LogTypes = LogEntry.types;

import Select from "material-ui/Select";
import MenuItem from "material-ui/Menu/MenuItem";
import TextField from "material-ui/TextField";
import Input, { InputLabel } from "material-ui/Input";
import { FormControl, FormGroup, FormControlLabel } from "material-ui/Form";
import Checkbox from "material-ui/Checkbox";

const StyledConsole = glamorous.div({
    background: "#000000",
    padding: "0",
    margin: "0",
    opacity: ".8",
    fontFamily: "monospace",
    fontSize: "16px",
    overflow: "auto",
    width: "100%",
    maxHeight: "100%",
    height: "auto",
    color: "white",

    display: "flex",
    flexDirection: "column",
    flex: "1 1 0%"
});

const logTimestampStyles = {
    color: "#53f300",
    marginRight: "20px"
};

const logIdStyles = {
    color: "#dee335",
    marginRight: "20px"
};

const getLogMessageStyle = log => {
    let common = {
        wordBreak: "break-all"
    };

    let getMessageColor = log => {
        switch (log.type) {
            case LogTypes.analytics: {
                return "#3155ff";
            }
            case LogTypes.error: {
                return "#ff3838";
            }
            default:
                return 'white'
        }
    };

    return {
        ...common,
        color: getMessageColor(log)
    }
};

const logsContainerStyles = {
    maxHeight: "100%",
    height: "auto",
    overflow: "auto",
    padding: "3px 10px"
}


const ControlPanel = glamorous.div({
    padding: "3px 10px",
    height: "30px",
    flex: "0 0 30px",
    display: "flex",
    alignItems: "center",
    justifyContent: "space-between",
    background: "#2b2b2b"
});

const Controls = glamorous.div({
    height: "28px",
    background: "transparent",
    top: "10px",
    right: "20px",
    padding: "0 10px"
});

const ControlButton = glamorous(IconButton, {
    filterProps: ["active"],
    withProps: ({ active }) => ({
        style: {
            color: active ? "#405ce3" : "white",
            width: "28px",
            height: "28px",
            fontSize: "15px",
            margin: "0 5px"
        }
    })
})({});

const Indicator = glamorous.div({
    display: "inline-block",
    width: "18px",
    height: "18px",
    position: "relative"
});

const Title = glamorous.div({});

const FilterControlPanel = glamorous.div({
    padding: "3px 10px",
    width: "100%",
    height: "30px",
    background: "#a8a8a8",
    color: "#2b2b2b",
    display: "flex",
    alignItems: "center",
    justifyContent: "left",
    flex: "0 0 30px",

    "& > *": {
        display: "flex",
        alignItems: "center",
        margin: "0 auto",
        width: "100%"
    }
});

const FilterOptionLabel = glamorous.span(
    {
        fontSize: "15px"
    },
    ({ selected }) => ({
        fontWeight: selected ? "500" : "400"
    })
);

const FilterTitle = glamorous.span({
    paddingBottom: "2px",
    marginRight: "5px"
});

const SessionsFilter = glamorous.div({});

const TypeFilter = glamorous.div({
    justifyContent: "flex-end"
});

const FilterSelect = glamorous(Select, {
    withProps: {
        style: {
            fontFamily: "monospace",
            maxWidth: "200px"
        }
    }
})({});

const TypeFilterLabel = glamorous(FormControlLabel)({}, ({ type }) => {
    let color = "grey";
    if (type === LogTypes.error) {
        color = "#ee0808";
    } else if (type === LogTypes.analytics) {
        color = "#1a3de6";
    } else if (type === LogTypes.payload) {
        color = "white";
    }
    return {
        "& p": {
            color,
            fontFamily: "monospace"
        }
    };
});

const TypeFilterCheckbox = glamorous(Checkbox, {
    withProps: {
        style: {
            color: "#2b2b2b"
        }
    }
})();

const tagToMessageMap = {
    session_started: message => `Session has been started`,
    session_completed: message => `Session has been completed`,
    session_paused: message => `Session has been paused`,
    session_resumed: message => `Session has been resumed`,
    session_failed: message => `Session  has been failed`
};

const logTypeToMessageMap = {
    [LogTypes.payload]: "payload",
    [LogTypes.analytics]: "analytics",
    [LogTypes.error]: "error"
};

class LogsList extends Component {
    componentDidUpdate() {
        this.scrollBottom();
    }

    scrollBottom() {
        let node = ReactDOM.findDOMNode(this.refs.logsEnd);
        node.scrollIntoView({ behavior: "smooth" });
    }

    formatNum(number) {
        if (number < 10) {
            return `0${number}`;
        }
        return number;
    }

    formatTimestamp(ts) {
        let date = new Date(ts);
        let year = date.getFullYear();
        let month = this.formatNum(date.getMonth() + 1);
        let day = this.formatNum(date.getDate());
        let hours = this.formatNum(date.getHours());
        let minutes = this.formatNum(date.getMinutes());
        let seconds = this.formatNum(date.getSeconds());
        return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
    }

    getLogMessage(log) { 
        if (log.type === LogTypes.analytics) {
            return tagToMessageMap[log.tag](log);
        }
        return log.message;
    }

    getKey(log) {
        return `${log.sessionId}_${log.timestamp}_${JSON.stringify(
            log.message
        )}`;
    }

    render() {
        return (
            <div style={logsContainerStyles}>
                {this.props.logs.map((log, idx) => {
                    let key = idx;
                    return (
                        <div key={key}>
                            <div>
                                <span style={logTimestampStyles}>{this.formatTimestamp(log.timestamp)}</span>
                                <span style={logIdStyles}>{log.sessionName}</span>
                                <span style={getLogMessageStyle(log)}>{this.getLogMessage(log)}</span>
                            </div>
                        </div>
                    );
                })}
                <div ref="logsEnd" />
            </div>
        );
    }
}

@inject("store")
@observer
class LogsListContainer extends Component {
    render() {
        return <LogsList logs={this.props.store.sessionsScreenStore.logs} />;
    }
}

@inject("store")
@observer
export default class Console extends Component {
    generateId() {
        return Math.round(Math.random() * 1000);
    }

    setSessionsFilter = e => {
        let store = this.props.store.sessionsScreenStore;
        let ids = e.target.value;
        store.setSessionsFilter(ids);
    };

    setTypeFilter = e => {
        let store = this.props.store.sessionsScreenStore;
        let ids = e.target.value;
        store.setTypeFilter(ids);
    };

    getSessionsFilterTextByIds(ids) {
        let { sessionsStore } = this.props.store;

        let text = sessionsStore.items
            .filter(session => {
                return ids.indexOf(session.id) !== -1;
            })
            .map(session => session.name)
            .join(",");

        return text;
    }

    getTypeFilterTextByIds(ids) {
        return ids
            .map(id => {
                return logTypeToMessageMap[id] || id;
            })
            .join(",");
    }

    toggleTypeFilter(id) {
        let store = this.props.store.sessionsScreenStore;
        let { typeFilter } = store;
        let idx = typeFilter.indexOf(id);
        if (idx !== -1) {
            store.setTypeFilter(
                typeFilter.slice(0, idx).concat(typeFilter.slice(idx + 1))
            );
        } else {
            store.setTypeFilter(typeFilter.concat(id));
        }
    }

    render() {
        let { expanded } = this.props;
        let {
            dimensionsStore,
            sessionsStore,
            sessionsScreenStore
        } = this.props.store;

        let { width, height } = dimensionsStore.dimensions;
        let {
            isFilterOptionsExpanded,
            typeFilter,
            sessionsFilter,
            logsFilterApplied
        } = sessionsScreenStore;

        return (
            <StyledConsole>
                <ControlPanel>
                    <Title>Sessions console</Title>
                    <Controls>
                        <ControlButton
                            active={logsFilterApplied}
                            onClick={() =>
                                sessionsScreenStore.setFilterOptionsExpand(
                                    !isFilterOptionsExpanded
                                )
                            }
                        >
                            <FilterIcon />
                        </ControlButton>
                        <ControlButton onClick={sessionsStore.clearLogs}>
                            <EraserIcon />
                        </ControlButton>
                        <ControlButton
                            onClick={sessionsScreenStore.toggleConsole}
                        >
                            {expanded ? <ChevronDownIcon /> : <ChevronUpIcon />}
                        </ControlButton>
                    </Controls>
                </ControlPanel>

                {isFilterOptionsExpanded && (
                    <FilterControlPanel>
                        <SessionsFilter>
                            <FilterTitle>Filter by session:</FilterTitle>
                            <FilterSelect
                                multiple
                                value={toJS(sessionsFilter)}
                                onChange={this.setSessionsFilter}
                                input={<Input id="sessions-filter-select" />}
                                renderValue={value =>
                                    this.getSessionsFilterTextByIds(value)
                                }
                            >
                                {sessionsStore.items.map(session => {
                                    return (
                                        <MenuItem
                                            key={session.id}
                                            value={session.id}
                                        >
                                            <FilterOptionLabel
                                                selected={
                                                    sessionsFilter.indexOf(
                                                        session.id
                                                    ) !== -1
                                                }
                                            >
                                                {session.name}
                                            </FilterOptionLabel>
                                        </MenuItem>
                                    );
                                })}
                            </FilterSelect>
                        </SessionsFilter>

                        <TypeFilter>
                            <FormControl>
                                <FormGroup row>
                                    <TypeFilterLabel
                                        control={
                                            <TypeFilterCheckbox
                                                checked={
                                                    typeFilter.indexOf(
                                                        LogTypes.analytics
                                                    ) !== -1
                                                }
                                                onChange={() =>
                                                    this.toggleTypeFilter(
                                                        LogTypes.analytics
                                                    )
                                                }
                                                value={LogTypes.analytics}
                                            />
                                        }
                                        label="Analytics"
                                        type={LogTypes.analytics}
                                    />
                                    <TypeFilterLabel
                                        control={
                                            <TypeFilterCheckbox
                                                checked={
                                                    typeFilter.indexOf(
                                                        LogTypes.payload
                                                    ) !== -1
                                                }
                                                onChange={() =>
                                                    this.toggleTypeFilter(
                                                        LogTypes.payload
                                                    )
                                                }
                                                value={LogTypes.payload}
                                            />
                                        }
                                        label="Payload"
                                        type={LogTypes.payload}
                                    />
                                    <TypeFilterLabel
                                        control={
                                            <TypeFilterCheckbox
                                                checked={
                                                    typeFilter.indexOf(
                                                        LogTypes.error
                                                    ) !== -1
                                                }
                                                onChange={() =>
                                                    this.toggleTypeFilter(
                                                        LogTypes.error
                                                    )
                                                }
                                                value={LogTypes.error}
                                            />
                                        }
                                        label="Error"
                                        type={LogTypes.error}
                                    />
                                </FormGroup>
                            </FormControl>
                        </TypeFilter>
                    </FilterControlPanel>
                )}

                <LogsListContainer />
            </StyledConsole>
        );
    }
}
