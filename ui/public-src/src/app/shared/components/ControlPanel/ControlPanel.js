
import React, { Component } from "react";
import { inject, observer } from "mobx-react";
import glamorous from "glamorous";

const StyledControlPanel = glamorous.div({
    height: '100%',
    width: '100%',
    background: '#E9E9E9',
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'space-between',
    padding: '0 40px',
    boxShadow: '0 0 1px 1px rgba(0, 0, 0, 0.16)',
    position: 'relative',
    zIndex: '1'
});

const AddButtonLayout = glamorous.div({});

const FilterPanelLayout = glamorous.div({
    display: "flex",
    alignItems: "center",
    "& > *:first-child": {
        marginRight: "10px"
    }
});

class ControlPanel extends Component { 
    constructor(props) {
        super(props);
    }

    render() {
        return (
            <StyledControlPanel>
                <AddButtonLayout>
                    { this.props.addButton }
                </AddButtonLayout>
                <FilterPanelLayout>
                    { this.props.filterPanel }
                </FilterPanelLayout>
            </StyledControlPanel>
        )
    }
}

export default ControlPanel;
