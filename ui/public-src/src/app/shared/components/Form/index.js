import validatorjs from "validatorjs";
import MobxReactForm from "mobx-react-form";
import bindings from "./bindings";

validatorjs.register('greather_than', function(valueStr, requirement, attribute) {
    let temp = attribute.split('.');
    let path = temp.slice(0, temp.length - 1).concat(requirement).join('.');
    let value2Str = this.validator.input[path];
    
    if(!valueStr || !value2Str) { 
        return true;
    }

    let value = parseFloat(valueStr);
    let value2 = parseFloat(value2Str);
	return value > value2;
}, `Must be greather than :greather_than`);

validatorjs.register('lower_than', function(valueStr, requirement, attribute) {
    let temp = attribute.split('.');
    let path = temp.slice(0, temp.length - 1).concat(requirement).join('.');
    let value2Str = this.validator.input[path];

    if(!valueStr || !value2Str) { 
        return true;
    }
    
    let value = parseFloat(valueStr);
    let value2 = parseFloat(value2Str);
	return value2 > value;
}, `Must be less than :lower_than`);

validatorjs.registerImplicit('required_when_has', function(valueStr, requirement, attribute) {
    let temp = attribute.split('.');
    let path = temp.slice(0, temp.length - 1).concat(requirement).join('.');
    return !this.validator.input[path];
}, `This field is required`);

validatorjs.registerImplicit('required_when_has_no', function(valueStr, requirement, attribute) {
    let temp = attribute.split('.');
    let path = temp.slice(0, temp.length - 1).concat(requirement).join('.');
    return !(!valueStr && !this.validator.input[path]);
}, `Required if :required_when_has_no is not present`);


validatorjs.register('long', function(val) {
    return String(parseInt(val, 10)) === String(val);
}, 'Must be a long value');

validatorjs.register('exclusive_min', function(val, req) {
    return val > req
}, 'Must be greather than :exclusive_min');

validatorjs.register('exclusive_max', function(val, req) {
    return val < req
}, 'Must be greather than :exclusive_max');

validatorjs.register('editor', function(val) {
    let result = false;
    try { 
        eval(val);
        result = true;
    } catch (err) {
        result = false;
    }
    finally { 
        return result; 
    } 
}, 'Not valid editor');


export default class Form extends MobxReactForm {

    bindings() {
        return bindings;
    }

    plugins() {
        return {
            dvr: {
                package: validatorjs
            }
        };
    }
}
