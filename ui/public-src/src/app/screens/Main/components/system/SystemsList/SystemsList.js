
import React, { Component } from "react";
import { inject, observer } from "mobx-react";
import glamorous from "glamorous";

import SystemListItem from './SystemListItem';

import EntityList from 'components/EntityList';


@observer
class SystemsList extends Component { 

    constructor(props) {
        super(props);
    }

    render() {
        let { items, onEdit, onDelete, width, height, selectable, selectedIds, onSelect } = this.props;
        return (
            <EntityList
                items={items}
                itemRenderer={(item) => <SystemListItem system={item} />}
                onEdit={onEdit}
                onDelete={onDelete}
                width={width}
                height={height}
                selectable={selectable}
                selectedIds={selectedIds}
                onSelect={onSelect}
            ></EntityList>
        )
    }
}


export default SystemsList;
