import React, { Component } from "react";
import { inject, observer } from "mobx-react";
import glamorous from "glamorous";

import Tabs, { Tab } from "material-ui/Tabs";
import CogsIcon from "components/Icons/CogsIcon";
import IbaLogo from "components/IbaLogo";

const StyledHeader = glamorous.div(
    {
        display: "flex",
        flex: "0 0 102px",
        justifyContent: "center",
        alignItems: "flex-end",
        width: "100%"
    },
    ({ theme }) => {
        return {
            color: theme.palette.getContrastText(theme.palette.primary[900]),
            backgroundColor: theme.palette.primary[900]
        };
    }
);

const BrandingPanel = glamorous.div({
    width: "100%",
    position: "absolute",
    top: "0",
    left: "0",
    height: "52px",
    display: "flex",
    flexDirection: "row",
    alignItems: "center",
    justifyContent: "space-between",
    padding: "0 50px",
    background: "#0066b3"
});

const BrandingIcon = glamorous(CogsIcon)({
    fontSize: "22px"
});

const StyledIbaLogo = glamorous(IbaLogo, {
    withProps: {
        style: {
            width: "54px",
            height: "auto"
        }
    }
})({});

const ApplicationTitle = glamorous.div({
    position: "absolute",
    top: "19px",
    fontSize: "14px",
    textTransform: "uppercase",
    fontFamily: "Roboto",
    letterSpacing: "3.2px",
    color: "#ffffff",
    left: "0",
    right: "0",
    textAlign: "left",
    margin: "auto",
    paddingLeft: "85px"
});

@inject("store")
@observer
export default class Header extends Component {
    tabs = [
        {
            label: "Sessions",
            page: "sessions",
            handler: function(view) {
                view.openSessionsPage();
            }
        },
        {
            label: "Data definitions",
            page: "definitions",
            handler: function(view) {
                view.openDefinitionsPage();
            }
        },
        {
            label: "Devices",
            page: "devices",
            handler: function(view) {
                view.openDevicesPage();
            }
        },
        {
            label: "Target systems",
            page: "systems",
            handler: function(view) {
                view.openSystemsPage();
            }
        }
    ];

    constructor(props) {
        super(props);
    }

    onTabChange = (event, tabIdx) => {
        let { view } = this.props.store;
        this.tabs[tabIdx].handler(view);
    };

    render() {
        return (
            <StyledHeader>
                <BrandingPanel>
                    <div>
                        <CogsIcon />
                        <ApplicationTitle>iot-data-simulator</ApplicationTitle>
                    </div>
                    <StyledIbaLogo />
                </BrandingPanel>

                <Tabs
                    value={this.props.store.view.currentTab}
                    onChange={this.onTabChange}
                >
                    {this.tabs.map((tab, index) => {
                        return (
                            <Tab
                                key={index}
                                label={tab.label}
                                value={index}
                                disabled={tab.disabled}
                            />
                        );
                    })}
                </Tabs>
            </StyledHeader>
        );
    }
}
