
import React, { Component } from "react";
import { inject, observer } from "mobx-react";
import glamorous from "glamorous";

const StyledView = glamorous.div({
    display: "flex",
    flexDirection: "column",
    justifyContent: "baseline",
    alignItems: "start",
    height: "100%",
    width: "100%",
    minHeight: "0",
    position: 'relative'
});

const ControlPanelLayout = glamorous.div({
    flex: "0 0 80px",
    width: "100%"
});

const ListLayout = glamorous.div({
    width: "100%",
    height: "100%",
    display: "flex",
    minHeight: "0",
    overflow: "auto",
    position: 'relative',
    background: '#d9d9d9'
});


class ViewLayout extends Component { 
    constructor(props) {
        super(props);
    }

    render() {
        let { controlPanel, list } = this.props;
        return (
            <StyledView>
                <ControlPanelLayout>
                    { controlPanel }
                </ControlPanelLayout>
                
                <ListLayout>
                    { list }
                </ListLayout>

                { this.props.children }
            </StyledView>
        )
    }
}

export default ViewLayout;