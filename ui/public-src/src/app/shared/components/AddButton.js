import React, { Component } from "react";
import Button from "material-ui/Button";

export default ({ children, ...rest}) => (
    <Button raised color="accent" {...rest}>
        {children}
    </Button>
);
