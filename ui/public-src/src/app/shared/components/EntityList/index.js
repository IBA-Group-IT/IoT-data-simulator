import React, { Component } from "react";
import { inject, observer } from "mobx-react";
import glamorous from "glamorous";
import { css } from "glamor";

import EntityListItem from "./EntityListItem";

const ListLayout = glamorous.div({
    display: "block",
    width: "100%",
    height: "100%",
    overflow: "auto",
    padding: "2em"
});

@observer
export default class EntityList extends Component {
    constructor(props) {
        super(props);
    }

    onSelect = (item) => {
        this.props.onSelect(item, this.props.selectedIds.indexOf(item.id) === -1);
    }

    onEdit = (item) => {
        if(this.props.onEdit) { 
            this.props.onEdit(item);
        }
    }

    onDelete = (item) => {
        if(this.props.onDelete) { 
            this.props.onDelete(item);
        }
    }

    render() {
        let {
            items = [],
            itemRenderer,
            width = "280px",
            height = "185px",
            selectable,
            selectedIds = [],
            onSelect,
            hideControls
        } = this.props;
        
        let noop = function(){};

        return (
            <ListLayout>
                {items.map((item, idx) => {
                    return (
                        <EntityListItem
                            width={width}
                            height={height}
                            key={item.id || idx}
                            onEdit={this.onEdit.bind(this, item)}
                            onDelete={this.onDelete.bind(this, item)}
                            selected={selectable ? (selectedIds.indexOf(item.id) !== -1) : false}
                            onSelect={ (selectable && onSelect) ? this.onSelect.bind(this, item) : noop}
                            selectable={selectable}
                            item={item}
                            hideControls={hideControls}
                        >
                            {itemRenderer(item)}
                        </EntityListItem>
                    );
                })}
            </ListLayout>
        );
    }
}
