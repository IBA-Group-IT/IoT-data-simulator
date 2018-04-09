import { observable, autorun, computed } from "mobx";
import BaseForm from "components/Form";

export default class FinalizeFormStore {
    
    @observable form;
    dispose;
    state;

    constructor(state) {
        this.state = state;
        this.initReactions();
    }

    @computed
    get definition() { 
        return this.state.definition;
    }

    @computed
    get finalizeFormOptions() {
        if (this.definition) {
            let { name } = this.definition;
            return {
                fields: {
                    name: {
                        label: "Definition name",
                        placeholder: "Insert name",
                        rules: "required|string",
                        bindings: "MaterialTextField",
                        hooks: {
                            onChange: field => {
                                this.definition.setName(field.value);
                            }
                        },
                        value: name
                    }
                }
            };
        }
    }

    initReactions() {
        this.dispose = autorun(() => {
            if (this.finalizeFormOptions) {
                this.form = new BaseForm(this.finalizeFormOptions);
            }
        });
    }
  
    disposeReactions() {
        this.dispose();
    }
}
