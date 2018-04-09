
import React, { Component } from "react";
import { inject, observer } from "mobx-react";
import CreateEditSystemPanel from 'components/CreateEditSystemPanel';

@inject("store")
@observer
class CreateEditSystemPanelContainer extends Component {

    onSubmit = () => {
        let { store } = this.props;
        let { systemsStore, systemsScreenStore } = store;
        let { system } = systemsScreenStore;
        let json = system.toJSON();


        //TODO move logic 
        if(systemsScreenStore.isEditing) {
            systemsStore.update(json.id, json).then(() => {
                systemsScreenStore.closeEdit();
                systemsScreenStore.load();
            });
        }else{
            systemsStore.create(json).then(() => {
                systemsScreenStore.closeCreate();
                systemsScreenStore.clearCreateSystem();
                systemsScreenStore.load();
            })
        }
    }

    render() { 
        let {
            store: {
                systemsScreenStore: {
                    formStore
                }
            } 
        } = this.props;

        return (
            <CreateEditSystemPanel
                store={formStore}
                onSubmit={this.onSubmit}
            />
        )
    }

}






export default CreateEditSystemPanelContainer;
