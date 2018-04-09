
import React, { Component } from "react";
import { inject, observer } from "mobx-react";
import glamorous from "glamorous";

@observer
class DummyItem extends Component { 

    constructor(props) {
        super(props);
    }

    render() {
        let { system } = this.props;
        
        return (
            <div>
                
            </div>
        )
    }
    
}

export default DummyItem;
