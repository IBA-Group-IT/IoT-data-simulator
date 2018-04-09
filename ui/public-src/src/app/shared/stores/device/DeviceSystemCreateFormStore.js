import { observable, computed, action, autorun } from "mobx";
import formConfigBuilder from "models/system/formConfigBuilder";
import SystemFormStore from '../system/SystemFormStore';

export default class CreateDeviceSystemFormStore extends SystemFormStore {
    @computed
    get formConfig() { 
        let { device } = this;
        let { targetSystems } = device;

        let availableTypes = formConfigBuilder.formConfig.extra.type;
        let filteredTypes = availableTypes.filter((type) => {
            if(device.findTargetSystemByType(type.value)){ 
                return false;
            }
            return true;
        })

        let applyValidation = false;
        return formConfigBuilder.build(this.system, this.device, filteredTypes, applyValidation)
    }
}
