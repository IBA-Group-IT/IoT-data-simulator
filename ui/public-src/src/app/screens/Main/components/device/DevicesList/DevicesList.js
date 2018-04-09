import React, { Component } from "react";
import { inject, observer } from "mobx-react";
import glamorous from "glamorous";

import DeviceListItem from "./DeviceListItem";
import EntityList from "components/EntityList";

@observer
class DevicesList extends Component {
    constructor(props) {
        super(props);
    }

    render() {
        let {
            items,
            onDelete,
            onEdit,
            width,
            height,
            selectable,
            selectedIds = [],
            onSelect
        } = this.props;
        
        return (
            <EntityList
                items={items}
                itemRenderer={item => <DeviceListItem device={item} />}
                onEdit={onEdit}
                onDelete={onDelete}
                width={width}
                height={height}
                selectable={selectable}
                selectedIds={selectedIds}
                onSelect={onSelect}
            />
        );
    }
}

export default DevicesList;
