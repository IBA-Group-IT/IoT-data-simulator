import fnUtils from 'util/function'

export function throttle(delay) {
    return function(target, name, descriptor) {
        descriptor.value = fnUtils.throttle(descriptor.value, delay);
        return descriptor;
    };
}

export default {
    throttle
};
