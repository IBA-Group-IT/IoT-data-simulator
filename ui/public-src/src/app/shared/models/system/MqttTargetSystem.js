import { observable, computed, action, extendObservable, toJS } from "mobx";
import TargetSystemEntry from './TargetSystemEntry';

export default class MqttTargetSystem extends TargetSystemEntry {

    @observable url = null;
    @observable topic = null;

    constructor({ url, topic, ...rest }) {
        super(rest);
        this.url = url;
        this.topic = topic;
        this.headers = null;
    }

    setUrl(url) {
        this.url = url;
    }

    setTopic(topic) {
        this.topic = topic;
    }

    @computed
    get data() {
        return {
            name: this.name,
            type: this.type,
            security: this.security,
            url: this.url,
            topic: this.topic,
            messageSerializer: toJS(this.messageSerializer)
        }
    }
}
