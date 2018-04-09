import React, { Component } from "react";
import { inject, observer } from "mobx-react";
import glamorous from "glamorous";

import ControlPanel from "components/ControlPanel";
import AddButton from "components/AddButton";

import SearchInput from "components/SearchInput";
import Input, { InputLabel } from "material-ui/Input";
import { MenuItem } from "material-ui/Menu";
import Select from "material-ui/Select";
import { FormControl, FormHelperText } from "material-ui/Form";

import IconButton from "material-ui/IconButton";
import UploadIcon from "components/Icons/UploadIcon";
import SelectFile from 'components/Form/SelectFile';

const TypeSelect = glamorous(Select, {
    withProps: {
        style: {
            width: "135px"
        }
    }
})();

const AddButtonLayout = glamorous.div({
    display: 'flex',
    height: '36px',
    alignItems: 'center'
});


const ImportSessionField = glamorous(SelectFile, {
    withProps: {
        style: {
            marginLeft: '10px'
        }
    }
})({});

const ImportSessionButton = glamorous(IconButton, {
    withProps: {
        style: {
            height: '36px',
            width: '36px',
            fontSize: '20px',
            color: '#0066b3'
        }
    }
})();


@inject("store")
@observer
class SessionsControlPanel extends Component {
    constructor(props) {
        super(props);
    }

    setNameFilter = e => {
        this.props.store.sessionsStore.setNameFilter(e.target.value);
    };

    setStateFilter = e => {
        this.props.store.sessionsStore.setStateFilter(e.target.value);
    };

    importSession = e => {
        let file = e.target.files[0];
        this.props.store.sessionsStore.import(file);
    }

    render() {
        let { store } = this.props;
        let { view, sessionsStore } = store;

        return (
            <ControlPanel
                addButton={
                    <AddButtonLayout>
                        <AddButton onClick={view.openCreateSession}>
                            Create new session
                        </AddButton>
                        <ImportSessionField onDrop={this.importSession}>
                            <ImportSessionButton>
                                <UploadIcon />
                            </ImportSessionButton>
                        </ImportSessionField>
                    </AddButtonLayout>
                }
                filterPanel={[
                    <FormControl key="name-filter">
                        <SearchInput
                            value={sessionsStore.nameFilter}
                            onChange={this.setNameFilter}
                            placeholder="Filter by name"
                        />
                    </FormControl>,
                    <FormControl key="state-filter">
                        <TypeSelect
                            value={sessionsStore.stateFilter}
                            onChange={this.setStateFilter}
                            input={<Input id="session-state-filter" />}
                            label="by dataset availability"
                        >
                            <MenuItem value={sessionsStore.filterTypes.all}>
                                All
                            </MenuItem>
                            <MenuItem value={sessionsStore.filterTypes.active}>
                                Active
                            </MenuItem>
                            <MenuItem
                                value={sessionsStore.filterTypes.notActive}
                            >
                                Not active
                            </MenuItem>
                        </TypeSelect>
                    </FormControl>
                ]}
            />
        );
    }
}

export default SessionsControlPanel;
