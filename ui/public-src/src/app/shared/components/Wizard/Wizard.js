

import React, { Component } from "react";
import { inject, observer } from "mobx-react";

import glamorous from "glamorous";

const WizardContainer = glamorous.div({});


//// NOT IMPLEMENTED !!!

class Wizard extends Component { 
    constructor(props) {
        super(props);
    }

    render() {
        let { controlPanel, list } = this.props;
        return (
            <WizardContainer>
                {
                    React.Children.map(this.props.children, (Step) => {
                        
                    })
                }
            </WizardContainer>
        )
    }
}

export default Wizard;