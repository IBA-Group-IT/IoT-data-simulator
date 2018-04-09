import {observable, action, asStructure } from 'mobx';

class DimensionsStore {

    @observable.struct dimensions = this.params.dimensions;
    @observable.struct isMobile = this.params.isMobile;

    constructor() {
        this.params = this.getParams();
        this._initListener(() => {
            this.setDimensions();
        });
    }

    @action.bound
    setDimensions() {
        let params = this.getParams();
        this.dimensions = params.dimensions;
        this.isMobile = params.isMobile;
    }

    getParams() { 
        return {
            dimensions: {
                width: document.body.clientWidth,
                height: document.body.clientHeight
            },
            isMobile: document.body.clientWidth <= 768
        }
    }

    _initListener(cb) {
        if(window.attachEvent) {
            window.attachEvent('onresize', cb);
        } else if(window.addEventListener) {
            window.addEventListener('resize', cb, true);
        }
    }

}

export default DimensionsStore;