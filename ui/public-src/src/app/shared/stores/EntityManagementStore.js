import { observable, computed, action } from "mobx";

export default class EntityManagementStore {

    @observable editingEntityId;
    @observable.ref creationEntity;
    @observable.ref editingEntity;
    @observable deletingEntity;

    constructor(entityFactory, handlers) {
        this.entityFactory = entityFactory.bind(this);
        this.creationEntity = this.entityFactory();
        this.editingEntity = this.entityFactory();
    }

    setEditingEntityId(id) { 
        this.editingEntityId = id;
    }

    setEditingEntity(entity) { 
        this.editingEntity = entity;
    }

    setCreationEntity(entity) {
        this.creationEntity = entity;
    }

    setDeletingEntity(entity) {
        this.deletingEntity = entity;
    }
 
    reset() { 
        this.creationEntity = this.entityFactory();
        this.editingEntity = this.entityFactory();
    }

    resetEditing() {
        this.editingEntity = this.entityFactory();
    }

    @computed
    get isEditing() { 
        return !!this.editingEntityId;
    }

    @computed
    get entity() {
        if(this.editingEntityId) { 
            return this.editingEntity;
        }
        return this.creationEntity;
    }

}