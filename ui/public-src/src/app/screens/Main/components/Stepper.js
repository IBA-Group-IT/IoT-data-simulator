import React, { Component } from "react";
import { inject, observer } from "mobx-react";
import glamorous from "glamorous";
import MobileStepper from "material-ui/MobileStepper";
import { css } from 'glamor';

const StyledStepper = glamorous(MobileStepper, {
    withProps: (props) => {
        return {
            style: {
                disply: 'flex',
                justifyContent: 'space-between',
            },
            classes: {
                progress: css({
                    //Increase specifity
                    '&&': {
                        width: '62%',
                    }
                }).toString()
            }
        }
    }
})();


@observer
export default class Stepper extends React.Component {

    render() {
        return <StyledStepper type="progress" position="static" {...this.props} />;
    }
}