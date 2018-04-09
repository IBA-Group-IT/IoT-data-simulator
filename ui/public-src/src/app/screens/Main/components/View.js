import React, { Component } from "react";
import { inject, observer } from "mobx-react";
import glamorous from "glamorous";
import { css } from 'glamor';

import { withStyles } from 'material-ui/styles';
import Tabs from 'material-ui/Tabs';
import Tab from 'material-ui/Tabs/Tab';

import SessionsScreen from '../../Sessions';
import DefinitionsScreen from '../../Definitions';
import DevicesScreen from '../../Devices';
import SystemsScreen from '../../Systems';

const StyledView = glamorous.div({
    display: 'flex',
    height: '100%',
    minHeight: '0',
    width: '100%',
    padding: '0',
    alignItems: 'baseline',
    backgroundColor: '#d9d9d9'
});

@inject("store") @observer
export default class View extends Component {

    constructor(props) {
        super(props);
    }

    renderPage() {
        let { view } = this.props.store;
        switch (view.page){
            case 'sessions':
                return <SessionsScreen />
            case 'definitions':
                return <DefinitionsScreen />
            case 'devices':
                return <DevicesScreen />
            case 'systems': 
                return <SystemsScreen />
            default:
                return <SessionsScreen />
        }
    
    }

    render() {
        return (
           <StyledView>
               { this.renderPage() }
           </StyledView>
        )
    }
}
