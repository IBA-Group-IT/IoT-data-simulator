import { observable, computed, action, extendObservable, toJS } from "mobx";
import TargetSystemEntry from './TargetSystemEntry';

export default class RestTargetSystem extends TargetSystemEntry {

    @observable url;
    @observable method;

    constructor({ url, method = 'post', ...rest }) {
        super(rest);
        this.url = url;
        this.method = method;
    }

    setUrl(url) {
        this.url = url;
    }

    setMethod(method) {
        this.method = method;
    }


    @computed
    get data() { 
        return {
            name: this.name,
            type: this.type,
            security: this.security,
            headers: this.headers,
            url: this.url,
            method: this.method,
            messageSerializer: toJS(this.messageSerializer)
        }
    }

}
