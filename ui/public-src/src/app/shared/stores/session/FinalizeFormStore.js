import { observable, autorun, computed, action } from "mobx";
import BaseForm from "components/Form";

export default class FinalizeFormStore {

    @observable form;
    @observable session;

    constructor(appStore) {
        autorun(() => {
            if (this.finalizeFormOptions) {
                this.form = new BaseForm(this.finalizeFormOptions);
            }
        });
    }

    @action.bound
    setSession(session) { 
        this.session = session;
    }

    @computed
    get finalizeFormOptions() {
        if (this.session) {
            let { name } = this.session;
            return {
                fields: {
                    name: {
                        label: "Session name",
                        placeholder: "Insert name",
                        rules: "required|string",
                        bindings: "MaterialTextField",
                        hooks: {
                            onChange: field => {
                                this.session.setName(field.value);
                            }
                        },
                        value: name
                    }
                }
            };
        }
    }
}
