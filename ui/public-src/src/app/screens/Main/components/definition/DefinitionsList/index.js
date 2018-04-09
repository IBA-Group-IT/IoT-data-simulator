
import React, { Component } from "react";
import { inject, observer } from "mobx-react";
import glamorous from "glamorous";

import DefinitionListItem from './DefinitionListItem';
import EntityList from 'components/EntityList';


@inject("store") @observer
class DefinitionsList extends Component { 

    constructor(props) {
        super(props);
    }

    onDelete = (definition) => {
        let { view } = this.props.store;
        view.openDeleteDefinition(definition);
    }

    onEdit = (definition) => {
        let { view } = this.props.store;
        view.openEditDefinition(definition.id);
    }

    render() {
        let { items, selectable, selectedIds, onSelect, hideControls } = this.props;

        return (
            <EntityList
                items={items}
                itemRenderer={(item) => <DefinitionListItem title={item.name} definition={item} />}
                onEdit={this.onEdit}
                onDelete={this.onDelete}
                width={'340px'}
                height={'135px'}
                selectable={selectable}
                selectedIds={selectedIds}
                onSelect={onSelect}
                hideControls={hideControls}
            ></EntityList>
        )
    }
}


export default DefinitionsList;
