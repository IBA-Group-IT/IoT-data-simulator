
import React, { Component } from "react";
import { inject, observer } from "mobx-react";
import glamorous from "glamorous";

import ControlPanel from 'components/ControlPanel';
import AddButton from 'components/AddButton';

import SearchInput from "components/SearchInput";
import Input, { InputLabel } from "material-ui/Input";
import { MenuItem } from "material-ui/Menu";
import Select from "material-ui/Select";
import { FormControl, FormHelperText } from "material-ui/Form";

@inject("store") @observer
class DevicesControlPanel extends Component { 
    constructor(props) {
        super(props);
    }

    setNameFilter = e => {
        this.props.store.devicesStore.setNameFilter(e.target.value);
    };

    render() {
        let { store } = this.props;
        let { view, devicesStore } = store;

        return (
            <ControlPanel
                addButton={
                    <AddButton onClick={view.openCreateDevice}>
                        Create new device
                    </AddButton>
                }
                filterPanel={
                    <FormControl key="device-name-filter">
                        <SearchInput
                            value={devicesStore.nameFilter}
                            onChange={this.setNameFilter}
                            placeholder="Filter by name"
                        />
                    </FormControl>
                }
            />
        )
    }
}

export default DevicesControlPanel;
