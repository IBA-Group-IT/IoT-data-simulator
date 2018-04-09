import { observable, computed, action, extendObservable, toJS } from "mobx";
import TargetSystemEntry from "./TargetSystemEntry";
import fileUtils from "util/file";

const KEY_FUNCTION_DEFAULT = `
/**
 * Generates kafka key (UUID v4 string by default).
 * 
 * @param {Object} payload Kafka message
 * @param {String} topic Kafka topic name
 * @return {String} 
 */
function generateKey(payload, topic) { 
    var uuid = "";
    var i;
    var random;
    
    for (i = 0; i < 32; i++) {
        random = Math.random() * 16 | 0;
        if (i == 8 || i == 12 || i == 16 || i == 20) {
            uuid += "-"
        }
        uuid += (i == 12 ? 4 : (i == 16 ? (random & 3 | 8) : random)).toString(16);
    }
    
    return uuid;
}
`;

const PROTO_SERIALIZER_KEY_TEMPLATE = `
/**
 * Builds selected proto type payload
 *
 * @param message
  @returns {}
 */
function build(key) {
    return {};
}
`;

export default class KafkaTargetSystem extends TargetSystemEntry {
    @observable url = null;
    @observable topic = null;

    constructor({ url, topic, keyFunction, keySerializer = {}, ...rest }) {
        super(rest);
        this.url = url;
        this.topic = topic;
        this.keyFunction = keyFunction || KEY_FUNCTION_DEFAULT;
        this.headers = null;
        this.keySerializer = observable.map(keySerializer);
    }

    @action.bound
    setUrl(url) {
        this.url = url;
    }

    @action.bound
    setTopic(topic) {
        this.topic = topic;
    }

    @action.bound
    setKeyFunction(keyFunction) {
        this.keyFunction = keyFunction;
    }

    @action.bound
    setKeySerializer(keySerializer) {
        this.keySerializer = keySerializer;
    }

    @action.bound
    setKeySerializerType(type) {
        this.keySerializer.clear();
        this.keySerializer.set("type", type);

        if (type === "protobuf") {
            this.setKeySerializerParameter("jsBuilder", PROTO_SERIALIZER_KEY_TEMPLATE);
        }
    }

    @action.bound
    setKeySerializerParameter(key, value) {
        this.keySerializer.set(key, value);
    }

    @action.bound
    setKeySerializerEncodedFile(encodedKey, fileKey, file) {
        this.keySerializer.set(fileKey, file);
        fileUtils.getBase64(
            file,
            action(result => {
                this.keySerializer.set(encodedKey, result);
            })
        );
    }

    @computed
    get data() {
        return {
            name: this.name,
            type: this.type,
            security: this.security,
            url: this.url,
            topic: this.topic,
            keyFunction: this.keyFunction,
            messageSerializer: toJS(this.messageSerializer),
            keySerializer: toJS(this.keySerializer)
        };
    }

    toJSON() {
        let { security, messageSerializer, keySerializer, ...rest } = toJS(
            this
        );

        let options = { ...rest };

        if (security && security.type) {
            options.security = security;
        }

        if (messageSerializer && messageSerializer.type) {
            options.messageSerializer = messageSerializer;
        }

        if (keySerializer && keySerializer.type) {
            options.keySerializer = keySerializer;
        }

        return options;
    }
}
