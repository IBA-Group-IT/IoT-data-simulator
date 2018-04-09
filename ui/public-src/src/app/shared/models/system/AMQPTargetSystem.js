import { observable, computed, action, extendObservable, toJS } from "mobx";
import TargetSystemEntry from './TargetSystemEntry';

export default class AmqpTargetSystem extends TargetSystemEntry {

    @observable url = null;
    @observable queue = null;

    constructor({ url, queue, ...rest }) {
        super(rest);
        this.url = url;
        this.queue = queue;
        this.headers = null;
    }

    setUrl(url) {
        this.url = url;
    }

    setQueue(queue) {
        this.queue = queue;
    }

    @computed
    get data() { 
        return {
            name: this.name,
            type: this.type,
            security: this.security,
            url: this.url,
            queue: this.queue,
            messageSerializer: toJS(this.messageSerializer)
        }
    }
}
