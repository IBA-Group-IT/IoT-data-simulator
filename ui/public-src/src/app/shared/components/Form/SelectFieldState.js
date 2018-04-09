import { observable } from 'mobx';
import { FieldState } from 'formstate';


class SelectFieldState extends FieldState {

    @observable.shallow options = [];

    constructor(_initValue, options) { 
        super(_initValue);
        this.options = options;
    }
}

export default SelectFieldState;