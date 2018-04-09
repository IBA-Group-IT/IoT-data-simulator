import { computed } from 'mobx';
import UpdateDefinitionStore from './UpdateDefinitionStore';

export default class CreateDefinitionStore extends UpdateDefinitionStore { 

    @computed
    get definition() { 
        return this.definitionManagementStore.creationEntity;
    }

}