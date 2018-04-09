import { observable, computed, action, extendObservable, toJS } from "mobx";
import TargetSystemEntry from './TargetSystemEntry';

export default class WebsocketTargetSystem extends TargetSystemEntry {

    @observable url;

    constructor({ url, ...rest }) {
        super(rest);
        this.url = url;
    }


    @computed
    get data() { 
        return {
            name: this.name,
            type: this.type,
            security: this.security,
            headers: this.headers,
            url: this.url,
            messageSerializer: toJS(this.messageSerializer)
        }
    }

    setUrl(url) {
        this.url = url;
    }
}
