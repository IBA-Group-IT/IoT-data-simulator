
import React, { Component } from "react";
import glamorous from "glamorous";
import ArrowDownIcon from "material-ui-icons/ArrowDropDown";

const StyledAdvancedLink = glamorous.div({
    color: "#0066b3",
    textDecoration: "underline",
    cursor: "pointer",
    display: 'flex',
    alignItems: 'center',
    fontSize: '15px',
    fontWeight: 'bold'
});

const AdvancedLinkIcon = glamorous(ArrowDownIcon, {
    filterProps: ['isExpanded']
})(({ isExpanded }) => {
    return {
        transform: isExpanded ? 'rotate(0)' : 'rotate(270deg)'
    }
})

class AdvancedLink extends Component { 
    constructor(props) {
        super(props);
    }
    render() {
        let { isExpanded, children, ...rest } = this.props

        return (
            <StyledAdvancedLink {...rest}>
                <AdvancedLinkIcon isExpanded={isExpanded} />
                { children }
            </StyledAdvancedLink>
        )
    }
}

export default AdvancedLink;
