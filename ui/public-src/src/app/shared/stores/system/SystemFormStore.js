import { observable, computed, action, autorun } from "mobx";
import formConfigBuilder from "models/system/formConfigBuilder";
import targetSystemFactory from "models/system/factory";

import BaseForm from "components/Form";

export default class SystemFormStore {
    @observable systemForm = null;
    @observable isKeyFunctionOpen = false;
    @observable currentKeyFunctionValue = "";

    @observable isMessageSerializerEditorOpen = false;
    @observable isKeySerializerEditorOpen = false;
    @observable currentMessageSerializerEditorValue = "";
    @observable currentKeySerializerEditorValue = "";

    @observable isAdvancedOptionsOpen = false;

    constructor(systemManagementStore, deviceManagementStore) {
        this.systemManagementStore = systemManagementStore;
        this.deviceManagementStore = deviceManagementStore;

        let onPropChange = key => {
            let method = `set${key.charAt(0).toUpperCase() + key.slice(1)}`;
            return field => {
                this.system[method](field.value);
            };
        };

        let onSecurityChange = key => {
            return field => {
                this.system.security[key] = field.value;
            };
        };

        this.hooks = {
            name: { onChange: onPropChange("name") },
            url: { onChange: onPropChange("url") },
            topic: { onChange: onPropChange("topic") },
            method: { onChange: onPropChange("method") },
            avroSchemaRegistryUrl: {
                onChange: onPropChange("avroSchemaRegistryUrl")
            },
            bucket: { onChange: onPropChange("bucket") },
            dataset: { onChange: onPropChange("dataset") },
            queue: { onChange: onPropChange("queue") },
            keyFunctionButton: {
                onClick: () => {
                    this.openKeyFunctionEditor();
                }
            },
            "security.type": { onChange: onSecurityChange("type") },
            "security.username": { onChange: onSecurityChange("username") },
            "security.password": { onChange: onSecurityChange("password") },
            "security.token": { onChange: onSecurityChange("token") },
            "security.ca": { onChange: onSecurityChange("ca") },
            "security.accessKey": { onChange: onSecurityChange("accessKey") },
            "security.secretKey": { onChange: onSecurityChange("secretKey") },
            "security.deviceCertificate": {
                onChange: onSecurityChange("deviceCertificate")
            },
            "security.privateKey": { onChange: onSecurityChange("privateKey") },
            "security.authHost": { onChange: onSecurityChange("authHost") },
            "security.apiKey": { onChange: onSecurityChange("apiKey") },
            "security.restKey": { onChange: onSecurityChange("restKey") },
            "security.tenant": { onChange: onSecurityChange("tenant") },

            "security.caFile": {
                onDrop: field => {
                    this.system.setSecurityCaCert(field.files[0]);
                }
            },
            "security.deviceCertificateFile": {
                onDrop: field => {
                    this.system.setSecurityDeviceCert(field.files[0]);
                }
            },
            "security.privateKeyFile": {
                onDrop: field => {
                    this.system.setSecurityPrivateKey(field.files[0]);
                }
            },

            headers: {
                onAdd: field => {
                    this.system.addHeader();
                }
            },
            "headers[]": {
                onDel: field => {
                    let key = field.value.headerKey;
                    this.system.deleteHeader(key);
                }
            },
            "headers[].headerValue": {
                onChange: field => {
                    let key = field.container().value.headerKey;
                    this.system.setHeaderValue(key, field.value);
                }
            },
            type: {
                onChange: field => {
                    if (this.systemType !== field.value) {
                        this.setSystemType(field.value);
                    }
                }
            },

            "security.type": {
                onChange: field => {
                    let type = field.value;
                    if (!type || type === "none") {
                        this.system.setSecurity({});
                    } else {
                        this.system.setSecurityType(field.value);
                    }
                }
            },

            "messageSerializer.type": {
                onChange: field => {
                    this.system.setMessageSerializerType(field.value);
                }
            },
            "messageSerializer.protoType": {
                onChange: field => {
                    this.system.setMessageSerializerParameter(
                        "protoType",
                        field.value
                    );
                }
            },
            "messageSerializer.protoDescriptor": {
                onChange: field => {
                    this.system.setMessageSerializerParameter(
                        "protoDescriptor",
                        field.value
                    );
                }
            },
            "messageSerializer.protoDescriptorFile": {
                onDrop: field => {
                    this.system.setMessageSerializerEncodedFile(
                        "protoDescriptor",
                        "protoDescriptorFile",
                        field.files[0]
                    );
                }
            },
            "messageSerializer.jsBuilder": {
                onChange: field => {
                    this.system.setMessageSerializerParameter(
                        "jsBuilder",
                        field.value
                    );
                }
            },

            "keySerializer.type": {
                onChange: field => {
                    this.system.setKeySerializerType(field.value);
                }
            },
            "keySerializer.protoType": {
                onChange: field => {
                    this.system.setKeySerializerParameter(
                        "protoType",
                        field.value
                    );
                }
            },
            "keySerializer.protoDescriptor": {
                onChange: field => {
                    this.system.setKeySerializerParameter(
                        "protoDescriptor",
                        field.value
                    );
                }
            },

            "keySerializer.protoDescriptorFile": {
                onDrop: field => {
                    this.system.setKeySerializerEncodedFile(
                        "protoDescriptor",
                        "protoDescriptorFile",
                        field.files[0]
                    );
                }
            },

            "keySerializer.jsBuilder": {
                onChange: field => {
                    this.system.setKeySerializerParameter(
                        "jsBuilder",
                        field.value
                    );
                }
            }
        };

        this.handlers = {
            "headers[].headerKey": {
                onChange: field => e => {
                    let idx = field.container().path.split(".")[1];
                    let newValue = e.target.value;
                    if (!this.system.hasHeader(newValue)) {
                        this.system.setHeaderKey(idx, newValue);
                    } else {
                        field.invalidate("Header keys must differ");
                    }
                    field.set("value", newValue);
                }
            }
        };

        autorun(() => {
            if (this.systemFormOptions) {
                this.systemForm = new BaseForm(this.systemFormOptions);
            }
        });

        autorun(() => {
            let advancedShouldBeOpened =
                this.system.messageSerializer.get("type") ||
                ( this.system.keySerializer && this.system.keySerializer.get("type") );

            this.toggleAdvancedOptions(advancedShouldBeOpened);
        });
    }

    @action.bound
    setSystemType(type) {
        let { systemManagementStore } = this;
        let system = systemManagementStore.entity;

        if (this.systemManagementStore.isEditing) {
            systemManagementStore.setEditingEntity(
                targetSystemFactory({
                    ...system.toJSON(),
                    type
                })
            );
        } else {
            systemManagementStore.setCreationEntity(
                targetSystemFactory({
                    ...system.toJSON(),
                    type
                })
            );
        }
        this.system.setType(type);
    }

    @action.bound
    openKeyFunctionEditor() {
        this.isKeyFunctionOpen = true;
        this.currentKeyFunctionValue = this.system.keyFunction;
    }

    @action.bound
    onKeyFunctionCancel() {
        this.closeKeyFunctionEditor();
        this.currentKeyFunctionValue = this.system.keyFunction;
    }

    @action.bound
    closeKeyFunctionEditor() {
        this.isKeyFunctionOpen = false;
    }

    @action.bound
    onKeyFunctionChange(value) {
        this.currentKeyFunctionValue = value;
    }

    @action.bound
    onKeyFunctionApply() {
        try {
            eval(`"use strict"; ${this.currentKeyFunctionValue}`);
            this.system.setKeyFunction(this.currentKeyFunctionValue);
            this.closeKeyFunctionEditor();
        } catch (e) {
            this.appStore.errorStore.addError({
                message: "Function is not valid"
            });
        }
    }

    @action.bound
    openMessageSerializerEditor() {
        this.isMessageSerializerEditorOpen = true;
        this.currentMessageSerializerEditorValue = this.system.messageSerializer.get(
            "jsBuilder"
        );
    }

    @action.bound
    closeMessageSerializerEditor() {
        this.isMessageSerializerEditorOpen = false;
    }

    @action.bound
    onMessageSerializerEditorChange(value) {
        this.currentMessageSerializerEditorValue = value;
    }

    @action.bound
    onMessageSerializerEditorCancel() {
        this.closeMessageSerializerEditor();
        this.currentMessageSerializerEditorValue = this.system.messageSerializer.get(
            "jsBuilder"
        );
    }

    @action.bound
    onMessageSerializerEditorApply() {
        try {
            eval(`"use strict"; ${this.currentMessageSerializerEditorValue}`);
            this.system.setMessageSerializerParameter(
                "jsBuilder",
                this.currentMessageSerializerEditorValue
            );
            this.closeMessageSerializerEditor();
        } catch (e) {
            this.appStore.errorStore.addError({
                message: "Function is not valid"
            });
        }
    }

    @action.bound
    openKeySerializerEditor() {
        this.isKeySerializerEditorOpen = true;
        this.currentKeySerializerEditorValue = this.system.keySerializer.get(
            "jsBuilder"
        );
    }

    @action.bound
    closeKeySerializerEditor() {
        this.isKeySerializerEditorOpen = false;
    }

    @action.bound
    onKeySerializerEditorChange(value) {
        this.currentKeySerializerEditorValue = value;
    }

    @action.bound
    onKeySerializerEditorCancel() {
        this.closeKeySerializerEditor();
        this.currentKeySerializerEditorValue = this.system.keySerializer.get(
            "jsBuilder"
        );
    }

    @action.bound
    onKeySerializerEditorApply() {
        try {
            eval(`"use strict"; ${this.currentMessageSerializerEditorValue}`);
            this.system.setKeySerializerParameter(
                "jsBuilder",
                this.currentKeySerializerEditorValue
            );
            this.closeKeySerializerEditor();
        } catch (e) {
            this.appStore.errorStore.addError({
                message: "Function is not valid"
            });
        }
    }

    @action.bound
    toggleAdvancedOptions(isShown) {
        this.isAdvancedOptionsOpen = isShown;
    }

    @computed
    get system() {
        return this.systemManagementStore.entity;
    }

    @computed
    get device() {
        return this.deviceManagementStore.entity;
    }

    configure({ hooks, handlers }) {
        this.hooks = hooks;
        this.handlers = handlers;
    }

    get systemFormOptions() {
        if (this.system) {
            let values = this.system.data;
            let config = this.formConfig;
            let types = config.extra.type;

            let options = {
                ...config,
                hooks: this.hooks,
                handlers: this.handlers,
                values: {
                    ...values
                }
            };

            if (this.system.security && !this.system.security.type) {
                options.values.security = {
                    type: "none"
                };
            }

            return options;
        }
    }

    @computed
    get formConfig() {
        let shouldFilter = false;
        return formConfigBuilder.build(this.system, this.device, shouldFilter);
    }

    @computed
    get formIsValid() {
        return this.systemForm.isValid;
    }
}
