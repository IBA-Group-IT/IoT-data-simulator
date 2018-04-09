import systemTypes from "./types";

let fields = [
    "name",
    "type",
    "url",
    "topic",
    "avroSchemaRegistryUrl",
    "method",
    "queue",
    "headers",
    "keyFunctionButton",
    "keyFunction",
    "headers[].headerKey",
    "headers[].headerValue",

    "security.type",
    "security.username",
    "security.password",
    "security.token",
    "security.ca",
    "security.caFile",
    "security.deviceCertificate",
    "security.deviceCertificateFile",
    "security.privateKey",
    "security.privateKeyFile",
    "security.accessKey",
    "security.secretKey",
    "security.authHost",
    "security.apiKey",
    "security.tenant",

    "messageSerializer.type",
    "messageSerializer.protoType",
    "messageSerializer.protoDescriptor",
    "messageSerializer.protoDescriptorFile",
    "messageSerializer.jsBuilder",

    "keySerializer.type",
    "keySerializer.protoType",
    "keySerializer.protoDescriptor",
    "keySerializer.protoDescriptorFile",
    "keySerializer.jsBuilder"
];

let labels = {
    name: "name",
    type: "type",
    url: "URL",
    topic: "topic",
    method: "method",
    headers: "headers",
    queue: "queue",
    avroSchemaRegistryUrl: "avro schema URL",
    bucket: "bucket",
    dataset: "dataset",
    keyFunctionButton: "Key function",
    "headers[].headerKey": "key",
    "headers[].headerValue": "value",
    "security.type": "type",
    "security.username": "username",
    "security.password": "password",
    "security.token": "token",
    "security.ca": ".ca certificate",
    "security.deviceCertificate": "device certificate",
    "security.privateKey": "private key",
    "security.accessKey": "access key",
    "security.secretKey": "secret key",
    "security.authHost": "auth host",
    "security.apiKey": "api key",
    "security.restKey": "rest key",
    "security.tenant": "tenant",

    "messageSerializer.type": "Serializer type",
    "messageSerializer.protoType": "Proto type",
    "messageSerializer.protoDescriptor": ".proto file",
    "messageSerializer.jsBuilder": "js builder",

    "keySerializer.type": "Serializer type",
    "keySerializer.protoType": "Proto type",
    "keySerializer.protoDescriptor": ".proto file",
    "keySerializer.jsBuilder": "js builder"
};

let placeholders = {
    name: "name",
    type: "type",
    url: "URL",
    bucket: "bucket",
    dataset: "dataset",
    topic: "topic",
    method: "method",
    headers: "headers",
    avroSchemaRegistryUrl: "avro schema URL",
    queue: "queue",
    "headers[].headerKey": "key",
    "headers[].headerValue": "value",
    "security.type": "type",
    "security.username": "username",
    "security.password": "password",
    "security.token": "token",
    "security.ca": "certificate",
    "security.deviceCertificate": "device certificate",
    "security.privateKey": "private key",
    "security.accessKey": "access key",
    "security.secretKey": "secret key",
    "security.authHost": "auth host",
    "security.apiKey": "api key",
    "security.restKey": "rest key",
    "security.tenant": "tenant",

    "messageSerializer.type": "serializer type",
    "messageSerializer.protoType": "proto type",
    "messageSerializer.protoDescriptor": ".proto file base64 encoded",
    "messageSerializer.jsBuilder": "js builder",

    "keySerializer.type": "serializer type",
    "keySerializer.protoType": "proto type",
    "keySerializer.protoDescriptor": ".proto file base 64 encoded",
    "keySerializer.jsBuilder": "js builder"
};

let rules = {
    name: "required|string",
    type: "required",
    url: "required|string",
    topic: "required|string",
    method: "required",
    bucket: "required|string",
    dataset: "required|string",
    queue: "required|string",
    "headers[].headerKey": "required|string",
    "headers[].headerValue": "required|string",
    "security.type": "required",
    "security.username": "required|string",
    "security.password": "required|string",
    "security.token": "required|string",
    "security.ca": "string",
    "security.deviceCertificate": "required|string",
    "security.privateKey": "required|string",
    avroSchemaRegistryUrl: "string",
    "security.accessKey": "required|string",
    "security.secretKey": "required|string",
    "security.authHost": "required",
    "security.apiKey": "required_when_has_no:restKey|string",
    "security.restKey": "required_when_has_no:apiKey|string",
    "security.tenant": "required",

    "messageSerializer.type": "string",
    "messageSerializer.protoType": "required|string",
    "messageSerializer.protoDescriptor": "required|string",
    "messageSerializer.jsBuilder": "required|string",

    "keySerializer.type": "string",
    "keySerializer.protoType": "required|string",
    "keySerializer.protoDescriptor": "required|string",
    "keySerializer.jsBuilder": "required|string"
};

let bindings = {
    name: "MaterialTextField",
    type: "MaterialSelect",
    url: "MaterialTextField",
    topic: "MaterialTextField",
    method: "MaterialSelect",
    bucket: "MaterialTextField",
    dataset: "MaterialTextField",
    avroSchemaRegistryUrl: "MaterialTextField",
    queue: "MaterialTextField",
    "headers[].headerKey": "MaterialTextField",
    "headers[].headerValue": "MaterialTextField",
    "security.type": "MaterialSelect",
    "security.username": "MaterialTextField",
    "security.password": "MaterialTextField",
    "security.token": "MaterialTextField",
    "security.ca": "MaterialTextField",
    "security.deviceCertificate": "MaterialTextField",
    "security.privateKey": "MaterialTextField",
    "security.accessKey": "MaterialTextField",
    "security.secretKey": "MaterialTextField",
    "security.authHost": "MaterialTextField",
    "security.apiKey": "MaterialTextField",
    "security.restKey": "MaterialTextField",
    "security.tenant": "MaterialTextField",

    "messageSerializer.type": "MaterialSelect",
    "messageSerializer.protoType": "MaterialTextField",
    "messageSerializer.protoDescriptor": "MaterialTextField",
    "messageSerializer.jsBuilder": "MaterialTextField",

    "keySerializer.type": "MaterialSelect",
    "keySerializer.protoType": "MaterialTextField",
    "keySerializer.protoDescriptor": "MaterialTextField",
    "keySerializer.jsBuilder": "MaterialTextField"
};

let extra = {
    type: systemTypes,
    method: [{ label: "POST", value: "post" }, { label: "PUT", value: "put" }],
    "security.type": [
        { label: "None", value: "none" },
        { label: "Access keys", value: "access_keys" },
        { label: "Access token", value: "access_token" },
        { label: "Credentials", value: "credentials" },
        { label: "Certificates", value: "certificates" }
    ],
    "messageSerializer.type": [
        { label: "", value: "" },
        { label: "Protobuf", value: "protobuf" }
    ],
    "keySerializer.type": [
        { label: "", value: "" },
        { label: "Protobuf", value: "protobuf" }
    ]
};

let types = {
    name: "text",
    type: "select",
    url: "text",
    topic: "text",
    method: "select",
    bucket: "text",
    dataset: "text",
    avroSchemaRegistryUrl: "text",
    keyFunctionButton: "button",
    "headers[].headerKey": "text",
    "headers[].headerValue": "text",
    "security.type": "select",
    "security.username": "text",
    "security.password": "text",
    "security.token": "text",
    "security.ca": "text",
    "security.deviceCertificate": "text",
    "security.privateKey": "text",
    "security.privateKeyFile": "file",
    "security.caFile": "file",
    "security.deviceCertificateFile": "file",
    "security.accessKey": "text",
    "security.secretKey": "text",
    "security.authHost": "text",
    "security.apiKey": "text",
    "security.restKey": "text",
    "security.tenant": "text",

    "messageSerializer.type": "select",
    "messageSerializer.protoType": "text",
    "messageSerializer.protoDescriptor": "text",
    "messageSerializer.protoDescriptorFile": "file",
    "messageSerializer.jsBuilder": "text",

    "keySerializer.type": "select",
    "keySerializer.protoType": "text",
    "keySerializer.protoDescriptor": "text",
    "keySerializer.protoDescriptorFile": "file",
    "keySerializer.jsBuilder": "text"
};

let formConfig = {
    fields,
    labels,
    placeholders,
    rules,
    bindings,
    extra,
    types
};

function build(system, device, availableTypes, applyValidation = true) {
    let commonFields = ["name", "type"];
    let specificFields = [];
    let securityFields = [];
    let availableSecurityTypes = [];

    if (system.type !== "dummy" && system.type !== 's3') {
        commonFields.push("messageSerializer.type");
        if (system.messageSerializer.get('type') === "protobuf") {
            commonFields.push.apply(commonFields, [
                "messageSerializer.protoType",
                "messageSerializer.protoDescriptor",
                "messageSerializer.protoDescriptorFile",
                "messageSerializer.jsBuilder"
            ]);
        }
    }

    if (system.type === "mqtt_broker") {
        specificFields = ["topic", "url"];
        availableSecurityTypes = [
            "none",
            "access_token",
            "certificates",
            "credentials",
            "access_keys",
        ];
    } else if (system.type === "rest_endpoint") {
        specificFields = [
            "method",
            "url",
            "headers",
            "headers[].headerKey",
            "headers[].headerValue"
        ];
        availableSecurityTypes = ["none", "certificates", "credentials"];
    } else if (system.type === "websocket_endpoint") {
        specificFields = [
            "url",
            "headers",
            "headers[].headerKey",
            "headers[].headerValue"
        ];
        availableSecurityTypes = ["none", "certificates", "credentials"];
    } else if (system.type === "kafka_broker") {

        specificFields = [
            "topic",
            "url",
            "keyFunctionButton",
            "keySerializer.type"
        ];

        if (system.keySerializer.get('type') === "protobuf") {
            commonFields.push.apply(commonFields, [
                "keySerializer.protoType",
                "keySerializer.protoDescriptor",
                "keySerializer.protoDescriptorFile",
                "keySerializer.jsBuilder"
            ]);
        }

        availableSecurityTypes = ["none", "certificates"];

    } else if (system.type === "s3") {
        specificFields = ["dataset"];
    } else if (system.type === "amqp_broker") {
        specificFields = ["url", "queue"];
        availableSecurityTypes = ["none", "certificates", "credentials"];
    }

    if (system.security) {
        let securityType = system.security.type;
        if (system.type === "s3") {
            securityFields = [];
        } else if (!securityType || securityType === "none") {
            securityFields = ["security.type"];
        } else if (securityType === "credentials") {
            securityFields = [
                "security.type",
                "security.username",
                "security.password"
            ];
        } else if (securityType === "access_token") {
            securityFields = ["security.type", "security.token"];
        } else if (securityType === "access_keys") {
            securityFields = ["security.accessKey", "security.secretKey"];
        } else if (securityType === "certificates") {
            securityFields = [
                "security.type",
                "security.ca",
                "security.caFile",
                "security.deviceCertificateFile",
                "security.deviceCertificate",
                "security.privateKey",
                "security.privateKeyFile"
            ];
        }
    }

    let systemFields = commonFields
        .concat(specificFields)
        .concat(securityFields);

    let appliedSystemTypes = [];
    let systemTypeOptions = extra.type;
    if (availableTypes) {
        availableTypes.forEach(type => {
            for (let i = 0; i < systemTypeOptions.length; i++) {
                if (systemTypeOptions[i].value === type.value) {
                    appliedSystemTypes.push(systemTypeOptions[i]);
                }
            }
        });
    }

    let combinedExtra = {
        ...extra,
        type: availableTypes ? appliedSystemTypes : systemTypeOptions,
        "security.type": extra["security.type"].filter(({ value }) => {
            return availableSecurityTypes.indexOf(value) !== -1;
        })
    };

    let sortedExtra = Object.keys(
        combinedExtra
    ).reduce((prev, curr, idx, arr) => {
        if (curr === "security.type") {
            return {
                ...prev,
                [curr]: combinedExtra[curr].sort((a, b) => {
                    if (a.value === "none") {
                        return false;
                    }
                    if (b.value === "none") {
                        return true;
                    }
                })
            };
        }
        return {
            ...prev,
            [curr]: combinedExtra[curr].sort((a, b) =>
                a.label.localeCompare(b.label)
            )
        };
    }, {});

    return {
        ...formConfig,
        fields: systemFields,
        rules: applyValidation
            ? rules
            : {
                  name: rules.name,
                  type: rules.type
              },
        extra: sortedExtra
    };
}

export default {
    formConfig,
    build
};
