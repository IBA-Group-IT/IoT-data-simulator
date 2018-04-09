
import React, { Component } from "react";
import { inject, observer } from "mobx-react";
import glamorous from "glamorous";

import SessionListItem from './SessionListItem';
import EntityList from 'components/EntityList';

@inject('store')
@observer
class SessionsList extends Component { 

    constructor(props) {
        super(props);
    }

    onEdit = (session) => {
        let { view } = this.props.store;
        view.openEditSession(session)
    }

    onDelete = (session) => {
        let { view } = this.props.store;
        view.openDeleteSession(session);
    }

    render() {
        let { sessionsStore } = this.props.store;
        let { items, onDelete, onEdit, width, height } = this.props;
        return (
            <EntityList
                items={sessionsStore.filteredItems}
                itemRenderer={(item) => <SessionListItem title={item.name} session={item} />}
                onEdit={this.onEdit}
                onDelete={this.onDelete}
                width={'300px'}
                height={'280px'}
            ></EntityList>
        )
    }
}


export default SessionsList;
