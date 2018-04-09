const build =  device => {

    let fields = [
        "name",
        "properties[]",
        'properties[].id',
        "properties[].name",
        "properties[].type",
        "properties[].stringVal",
        "properties[].numberVal",
        "properties[].boolVal"
    ];

    let labels = {
        "name": 'name',
        "properties[].name": 'name',
        "properties[].type": 'type',
        "properties[].stringVal": 'value',
        "properties[].numberVal": 'value',
        "properties[].boolVal": "value"
    };

    let placeholders = {
        "name": 'name',
        "properties[].name": 'name',
        "properties[].type": 'type',
        "properties[].stringVal": 'value',
        "properties[].numberVal": 'value'
    };

    let rules = {
        "name": 'required|string',
        "properties[].name": 'required|string',
        "properties[].type": 'required|string',
        "properties[].stringVal": 'required|string',
        "properties[].numberVal": 'required|numeric',
        "properties[].boolVal": "required"
    };

    let bindings = {
        "name": 'MaterialTextField',
        "properties[].name": 'MaterialTextField',
        "properties[].type": 'MaterialSelect',
        "properties[].stringVal": 'MaterialTextField',
        "properties[].numberVal": 'MaterialTextField',
        "properties[].boolVal": "MaterialSelect"
    };

    let extra = {
        "properties[].type": [
            { label: "Boolean", value: 'boolean' },
            { label: "String", value: 'string' },
            { label: 'Number', value: 'number'}
        ],
        "properties[].boolVal": [
            { label: "true", value: true },
            { label: "false", value: false },
        ]
    };

    let types = {
        "name": 'text',
        "properties[].name": 'text',
        "properties[].type": 'select',
        "properties[].stringVal": 'text',
        "properties[].numberVal": 'text',
        "properties[].boolVal": "select"
    };

    return {
        fields,
        labels,
        placeholders,
        rules,
        bindings,
        extra,
        types
    };
};

let getPropValueField = (property) => {
    switch(property.type) { 
        case 'string':
            return 'stringVal';
        case 'number': 
            return 'numberVal';
        case 'boolean':
            return 'boolVal'
    }
}

export default { 
    build,
    getPropValueField
}
