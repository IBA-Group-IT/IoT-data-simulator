import { observable, computed, action, autorun } from "mobx";
import formConfigBuilder from "models/system/formConfigBuilder";
import SystemFormStore from '../system/SystemFormStore';

export default class EditDeviceSystemFormStore extends SystemFormStore {
    @computed
    get formConfig() { 
        let { device, system } = this;
        let { targetSystems } = device;

        let availableTypes = formConfigBuilder.formConfig.extra.type;
        let filteredTypes = availableTypes.filter((option) => {
            if(device.findTargetSystemByType(option.value) && system.type !== option.value){ 
                return false;
            }
            return true;
        })

        let applyValidation = false;
        return formConfigBuilder.build(this.system, this.device, filteredTypes, applyValidation)
    }
}
