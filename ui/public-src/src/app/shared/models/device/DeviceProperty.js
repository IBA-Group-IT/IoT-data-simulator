import { observable, computed, action, toJS } from "mobx";

export default class DeviceProperty { 

    id
    @observable name;
    @observable type;
    @observable val;

    constructor({ id = Math.random() * 1000, name = '', type, value = '' } = {}) { 
        this.id = id;
        this.name = name;
        this.val = value;
        
        let _type = typeof this.val;
        if(_type === 'boolean'){
            this.setType('boolean');
        }else if (_type === 'number') {
            this.setType('number');
        }else {
            this.setType('string');
        }
    }

    @action.bound
    setName(name) {
        this.name = name;
    }

    @action.bound
    setType(type) {
        if(this.type === 'boolean'){
            this.val = '';
        }
        if(type === 'boolean') { 
            this.val = !!this.val;
        }
        this.type = type;
    }

    @action.bound
    setVal(val) { 
        this.val = val;
    }

    @computed
    get data() { 
        return toJS({
            id: this.id,
            name: this.name,
            type: this.type,
            val: this.val
        })
    }

    toJSON() {
        let options = {
            ...toJS(this),
            value: this.type === 'number' ? parseFloat(this.val) : this.val
        }
        delete options.val;
        return options;
    }
}