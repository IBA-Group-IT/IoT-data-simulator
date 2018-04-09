export function throttle(fn, delay) {
    
    let isThrottled = false;
    let _ctx, _args;

    return function wrapper() {
        
        if(isThrottled) { 
            _ctx = this;
            _args = arguments;
            return
        }

        fn.apply(this, arguments);
        isThrottled = true;

        setTimeout(function(){
            isThrottled = false;
            if(_args) { 
                wrapper.apply(_ctx, _args);
                _ctx = null;
                _args = null;
            }
        }, delay)
    };
}

export function noop() {}

export default { 
    throttle,
    noop
}