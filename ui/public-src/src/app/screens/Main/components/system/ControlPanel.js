import React, { Component } from "react";
import { inject, observer } from "mobx-react";
import glamorous from "glamorous";

import ControlPanel from "components/ControlPanel";
import AddButton from "components/AddButton";
import systemTypes from "models/system/types";

import SearchInput from "components/SearchInput";
import Input, { InputLabel } from "material-ui/Input";
import { MenuItem } from "material-ui/Menu";
import Select from "material-ui/Select";
import { FormControl, FormHelperText } from "material-ui/Form";


const TypeSelect = glamorous(Select, {
    withProps: {
        style: {
            width: "135px"
        }
    }
})();

@inject("store")
@observer
class SystemsControlPanel extends Component {
    constructor(props) {
        super(props);
    }

    setNameFilter = e => {
        this.props.store.systemsStore.setNameFilter(e.target.value);
    };

    setTypeFilter = e => {
        this.props.store.systemsStore.setTypeFilter(e.target.value);
    };

    render() {
        let { store } = this.props;
        let { view, systemsStore } = store;

        return (
            <ControlPanel
                addButton={
                    <AddButton onClick={view.openCreateSystem}>
                        Create new target system
                    </AddButton>
                }
                filterPanel={[
                    <FormControl key="name-filter">
                        <SearchInput
                            value={systemsStore.nameFilter}
                            onChange={this.setNameFilter}
                            placeholder="Filter by name"
                        />
                    </FormControl>,
                    <FormControl key="type-filter">
                        <TypeSelect
                            value={systemsStore.typeFilter}
                            onChange={this.setTypeFilter}
                            input={<Input id="system-type-filter" />}
                        >

                            <MenuItem key={systemsStore.filterTypes.all} value={systemsStore.filterTypes.all}>
                                All
                            </MenuItem>

                            {systemTypes.map(option => {
                                return (
                                    <MenuItem
                                        key={option.value}
                                        value={option.value}
                                    >
                                        {option.label}
                                    </MenuItem>
                                );
                            })}
                        </TypeSelect>
                    </FormControl>
                ]}
            />
        );
    }
}

export default SystemsControlPanel;
