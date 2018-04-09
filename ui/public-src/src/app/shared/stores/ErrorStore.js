
import { observable, action, computed } from 'mobx';
import idUtils from 'util/id';

class ErrorEntry { 

    @observable message;

    constructor({ message, id }, store) {
        this.message = message;
        this.id = id || idUtils.generate();
        this.store = store;
    }

    markAsRead() { 
        let { store } = this;
        let { errors } = this.store;

        let idx = errors.indexOf(this);
        store.errors = errors.slice(0, idx).concat(errors.slice(idx + 1));
    }
}

export default class ErrorStore { 

    @observable.shallow errors = [];

    @computed
    get lastError() { 
        return this.errors.length ? this.errors[this.errors.length - 1] : null;
    }

    @action
    addError(params) { 
        this.errors.push(new ErrorEntry(params, this));
    }

}