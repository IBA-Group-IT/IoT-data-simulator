import { observable, computed, action, extendObservable, toJS } from "mobx";
import idUtils from 'util/id';
import fileUtils from 'util/file';


const PROTO_SERIALIZER_DEFAULT_FUNCTION = `
/**
 * Builds selected proto type payload
 *
 * @param message
  @returns {}
 */
function build(message) {
    return {};
}
`

export default class TargetSystemEntry {
    id;
    @observable name;
    @observable type;
    @observable security = {};
    @observable headers;

    constructor({ id, name, type = "dummy", security, headers = [], messageSerializer = {} } = {}) {
        this.id = id || idUtils.generate();
        this.name = name;
        this.type = type;
        this.security = security || {};

        let _headers = headers || [];
        this.headers = _headers.map(header => {
            return {
                headerKey: header.name,
                headerValue: header.value
            };
        });

        this.messageSerializer = observable.map(messageSerializer);
    }

    addHeader(key = "", value = "") {
        this.headers.push({ headerKey: key, headerValue: value });
    }

    setHeaderKey(idx, newKey) {
        let header = this.headers[idx];
        header.headerKey = newKey;
    }

    setHeaderValue(key, newValue) {
        let header = this.findHeader(key);
        header.headerValue = newValue;
    }

    deleteHeader(key) {
        let header = this.findHeader(key);
        let idx = this.headers.indexOf(header);
        this.headers = this.headers
            .slice(0, idx)
            .concat(this.headers.slice(idx + 1));
    }

    hasHeader(key) {
        return !!this.findHeader(key);
    }

    findHeader(key) {
        let result = null;
        for (let i = 0; i < this.headers.length; i++) {
            if (this.headers[i].headerKey === key) {
                result = this.headers[i];
                break;
            }
        }
        return result;
    }

    setId(id) {
        this.id = id;
    }

    setName(name) {
        this.name = name;
    }

    setType(type) {
        this.type = type;
        if(this.security) { 
            this.setSecurity({});
        } else {
            this.setSecurity(null);
        }
    }

    setSecurity(security) {
        this.security = security;
    }

    setHeaders(headers) {
        this.headers = headers;
    }

    setSecurityType(type) {
        let options = {
            type
        };

        if (type === "certificates") {
            options.ca = this.security.ca || "";
            options.deviceCertificate = this.security.deviceCertificate || "";
            options.privateKey = this.security.privateKey || "";
        }

        this.setSecurity(options);
    }

    @action.bound
    setSecurityCaCert(file) {
        this.security.caFile = file;
        fileUtils.getBase64(
            file,
            action(result => {
                this.security.ca = result;
            })
        );
    }

    @action.bound
    setSecurityDeviceCert(file) {
        this.security.deviceCertificateFile = file;
        fileUtils.getBase64(
            file,
            action(result => {
                this.security.deviceCertificate = result;
            })
        );
    }

    @action.bound
    setSecurityPrivateKey(file) {
        this.security.privateKeyFile = file;
        fileUtils.getBase64(
            file,
            action(result => {
                this.security.privateKey = result;
            })
        );
    }

    @action
    setMessageSerializer(messageSerializer) { 
        this.messageSerializer = messageSerializer;
    }

    @action.bound
    setMessageSerializerType(type) { 
        this.messageSerializer.clear();
        this.messageSerializer.set('type', type);

        if(type === 'protobuf') { 
            this.setMessageSerializerParameter('jsBuilder', PROTO_SERIALIZER_DEFAULT_FUNCTION)
        }
    }

    @action.bound
    setMessageSerializerParameter(key, value) { 
        this.messageSerializer.set(key, value);
    }
    
    @action.bound
    setMessageSerializerEncodedFile(encodedKey, fileKey, file) { 
        this.messageSerializer.set(fileKey, file);
        fileUtils.getBase64(
            file,
            action(result => {
                this.messageSerializer.set(encodedKey, result);
            })
        )
        
    }

    @computed
    get data() {
        return {
            name: this.name,
            type: this.type,
            security: this.security,
            headers: this.headers,
            messageSerializer: toJS(this.messageSerializer)
        };
    }

    toJSON() {
        let { security, headers, messageSerializer, ...rest } = toJS(this);

        let options = {
            ...rest
        };

        if (headers) {
            options.headers = headers.map(header => ({
                name: header.headerKey,
                value: header.headerValue
            }));
        }

        if (security) {
            if (!security.type) {
                security = null;
            } else {
                options.security = security;
            }
        }

        if (messageSerializer && messageSerializer.type) { 
            options.messageSerializer = messageSerializer;
        }

        return options;
    }
}
