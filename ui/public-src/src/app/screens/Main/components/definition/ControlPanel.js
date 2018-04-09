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

import {
    filterTypes,
    filterByDatasetTypes
} from "stores/definition/DefinitionsFilterStore";

const TypeSelect = glamorous(Select, {
    withProps: {
        style: {
            width: "135px"
        }
    }
})();

@inject("store")
@observer
class DefinitionsControlPanel extends Component {
    constructor(props) {
        super(props);
    }

    setNameFilter = e => {e
        this.props.store.definitionsStore.filterStore.setFilterValue(
            filterTypes.byName,
            e.target.value
        );
    };

    setDatasetFilter = e => {
        this.props.store.definitionsStore.filterStore.setFilterValue(
            filterTypes.byDataset,
            e.target.value
        );
    };

    render() {
        let { store } = this.props;
        let { view, definitionsStore } = store;

        return (
            <ControlPanel
                addButton={
                    <AddButton onClick={view.openCreateDefinition}>
                        Create new definition
                    </AddButton>
                }
                filterPanel={[
                    <FormControl key="name-filter">
                        <SearchInput
                            value={definitionsStore.filterStore.values.byName}
                            onChange={this.setNameFilter}
                            placeholder="Filter by name"
                        />
                    </FormControl>,
                    <FormControl key="type-filter">
                        <TypeSelect
                            value={
                                definitionsStore.filterStore.values.byDataset
                            }
                            onChange={this.setDatasetFilter}
                            input={<Input id="def-type-filter" />}
                            label="by dataset availability"
                        >
                            <MenuItem value={filterByDatasetTypes.all}>
                                All
                            </MenuItem>
                            <MenuItem value={filterByDatasetTypes.hasDataset}>
                                Has dataset
                            </MenuItem>
                            <MenuItem value={filterByDatasetTypes.noDataset}>
                                No dataset
                            </MenuItem>
                        </TypeSelect>
                    </FormControl>
                ]}
            />
        );
    }
}

export default DefinitionsControlPanel;
