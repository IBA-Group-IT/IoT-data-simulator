
import React, { Component } from "react";
import { inject, observer } from "mobx-react";
import glamorous from "glamorous";
import SystemsList from './SystemsList';


@inject("store") @observer
class SystemsScreenSystemsList extends Component { 

    constructor(props) {
        super(props);
    }

    onDelete = (system) => {
        let { view } = this.props.store;
        view.openDeleteSystem(system);
    }

    onEdit = (system) => {
        let { view } = this.props.store;
        view.openEditSystem(system.id);
    }

    render() {
        let { systemsStore } = this.props.store;
        let { selectable, selectedIds, onSelect } = this.props;

        return (
            <SystemsList
                items={systemsStore.filteredItems}
                onEdit={this.onEdit}
                onDelete={this.onDelete}
                width={'295px'}
                height={'225px'}
                selectable={selectable}
                selectedIds={selectedIds}
                onSelect={onSelect}
            />
        )
    }
}


export default SystemsScreenSystemsList;
