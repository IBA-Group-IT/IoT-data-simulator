import { observable, computed, action } from "mobx";
import { createViewModel } from 'mobx-utils';

export default class ModalStore {
    appStore;

    @observable isCreateModalOpen = false;
    @observable isEditModalOpen = false;
    @observable isDeleteModalOpen = false;

    constructor(appStore) {
        this.appStore = appStore;
    }

    openCreate() {
        this.isCreateModalOpen = true;
    }

    closeCreate() { 
        this.isCreateModalOpen = false;
    }

    openEdit() { 
        this.isEditModalOpen = true;
    }

    closeEdit() { 
        this.isEditModalOpen = false;
    }

    openDelete() { 
        this.isDeleteModalOpen = true;
    }

    closeDelete() { 
        this.isDeleteModalOpen = false;
    }

}