import { observable, computed, action } from "mobx";
import { fromPromise } from 'mobx-utils';

export default class WizardStore {

    @observable step = 0;
    
    constructor(state) {}

    @action.bound
    setStep(step) {
        this.step = step;
    }

    @action.bound
    handleNextStep() {
        this.setStep(this.step + 1);
    }

    @action.bound
    handlePreviousStep() {
        if (this.step > 0) {
            this.setStep(this.step - 1);
        }
    }

    reset() {
        this.setStep(0);
    }
  
}   