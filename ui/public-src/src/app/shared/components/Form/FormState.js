import { FormState } from "formstate";

export default class FixedFormState extends FormState {
    assumeAllFieldsValid() {
        this.validatedSubFields = this.getValues();
        return this;
    }
}