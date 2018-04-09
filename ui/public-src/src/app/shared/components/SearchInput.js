import React from "react";
import Input, { InputLabel, InputAdornment } from "material-ui/Input";
import MagnifierIcon from 'components/Icons/MagnifierIcon';

export default props => (
    <Input
        startAdornment={
            <InputAdornment position="start">
                <MagnifierIcon />
            </InputAdornment>
        }
        {...props}
    />
);
