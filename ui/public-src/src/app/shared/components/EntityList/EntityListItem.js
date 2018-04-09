import React, { Component } from "react";
import { observable } from "mobx";
import { inject, observer } from "mobx-react";
import Paper from "material-ui/Paper";

import Menu from "material-ui/Menu";
import MenuItem from "material-ui/Menu/MenuItem";
import IconButton from "material-ui/IconButton";
import Icon from "material-ui/Icon";

import MoreVertIcon from "material-ui-icons/MoreVert";
import DeleteIcon from "material-ui-icons/Delete";
import CheckCircleIcon from "components/Icons/CheckCircleIcon";

import glamorous from "glamorous";
import { css } from "glamor";
import themeConfig from "styles/theme";

import ListItemIcon from "material-ui/List/ListItemIcon";
import ListItemText from "material-ui/List/ListItemText";

const StyledEntity = glamorous(Paper, {
    filterProps: ["selectable", "selected"],
    withProps: ({ selected }) => ({
        elevation: selected ? 4 : 1
    })
})(
    {
        padding: "0",
        float: "left",
        margin: "6px",
        position: "relative",
        transition: "all .5s ease-out",
        border: "0px solid green"
    },
    ({ theme, width, height, selectable, selected }) => ({
        color: theme.palette.textColor,
        width,
        height,
        cursor: selectable ? "pointer" : "initial",
        transform: selected ? "scale(.9)" : "scale(1)"
    })
);

const MoreSectionLayout = glamorous.div({
    position: "absolute",
    top: "1px",
    right: "10px"
});

const StyledMoreButton = glamorous(IconButton)({});

const MoreIcon = glamorous(Icon, {
    withProps: {
        color: "action",
        style: {
            fontSize: "15px"
        }
    }
})({});

const StyledDeleteIcon = glamorous(Icon, {
    withProps: {
        color: "action"
    }
})({});

const SelectedIndicator = glamorous(IconButton, {
    withProps: {
        style: {
            position: "absolute",
            left: "0",
            top: "0",
            fontSize: "40px",
            left: "-22px",
            top: "-25px",
            color: "rgb(0, 141, 205)"
        }
    }
})();

@observer
class EntityListItem extends Component {
    @observable
    menu = {
        open: false,
        anchorEl: null
    };

    handleRequestOpen = e => {
        if (!this.props.children.props.disableControls) {
            this.menu.open = true;
            this.menu.anchorEl = e.currentTarget;
        }
        if (this.props.selectable) {
            e.stopPropagation();
        }
    };

    handleRequestClose = e => {
        this.menu.open = false;
        this.menu.anchorEl = null;
    };

    onEdit = () => {
        this.props.onEdit();
        this.handleRequestClose();
    };

    onDelete = () => {
        this.props.onDelete();
        this.handleRequestClose();
    };

    onSelect = e => {
        this.props.onSelect();
    };

    constructor(props) {
        super(props);
    }

    render() {
        let {
            width,
            height,
            selected,
            onSelect,
            selectable,
            hideControls
        } = this.props;

        return (
            <StyledEntity
                width={width}
                height={height}
                selectable={selectable}
                selected={selected}
                onClick={this.onSelect}
            >
                {this.props.children}

                {!hideControls && (
                    <MoreSectionLayout>
                        <StyledMoreButton onClick={this.handleRequestOpen}>
                            <MoreIcon className="fa fa-ellipsis-v" />
                        </StyledMoreButton>
                        <Menu
                            id="more"
                            open={this.menu.open}
                            anchorEl={this.menu.anchorEl}
                            onRequestClose={this.handleRequestClose}
                        >
                            <MenuItem
                                onClick={this.onEdit}
                                button={true}
                                selected={false}
                            >
                                <ListItemIcon>
                                    <Icon className="fa fa-pencil" />
                                </ListItemIcon>
                                <ListItemText primary="Update" />
                            </MenuItem>
                            <MenuItem onClick={this.onDelete} selected={false}>
                                <ListItemIcon>
                                    <StyledDeleteIcon className="fa fa-trash" />
                                </ListItemIcon>
                                <ListItemText primary="Delete" />
                            </MenuItem>
                        </Menu>
                    </MoreSectionLayout>
                )}

                {selected ? (
                    <SelectedIndicator>
                        <CheckCircleIcon />
                    </SelectedIndicator>
                ) : null}
            </StyledEntity>
        );
    }
}

export default EntityListItem;
