import { computed } from 'mobx';
import UpdateDefinitionStore from './UpdateDefinitionStore';

export default class EditDefinitionStore extends UpdateDefinitionStore { 

    @computed
    get definition() { 
        return this.definitionManagementStore.editingEntity;
    }

}