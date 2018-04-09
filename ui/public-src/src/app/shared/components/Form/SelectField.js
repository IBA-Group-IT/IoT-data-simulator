import React, { Component } from "react";
import { inject, observer } from "mobx-react";
import glamorous from "glamorous";
import Select from 'material-ui/Select';
import { MenuItem } from 'material-ui/Menu';

import Input, { InputLabel } from 'material-ui/Input';
import { FormControl, FormHelperText } from 'material-ui/Form';

const InputContainer = glamorous.div({
    position: "relative",
    display: 'inline-block',
    width: '100%'
});

const Error = glamorous.small({
    color: "red",
    display: "block",
    marginLeft: "5px",
    position: "absolute"
});

@observer
export default class Field extends Component {
    render() {
        const { field, type, placeholder, style, label, disabled } = this.props;
        let isError = (field.dirty || field.hasBeenValidated) && field.hasError;
        let value = (typeof field.value !== 'undefined' && field.value !== null) ? field.value : '';

        return (
            <InputContainer>
                <FormControl error={isError} fullWidth={true}>
                    {label && <InputLabel shrink={true}>{label}</InputLabel>}
                    <Select
                        label={label}
                        placeholder={placeholder}
                        value={value}
                        style={style}
                        onChange={e => field.onChange(e.target.value)}
                        displayEmpty
                        disabled={disabled}
                    >
                        {
                            field.options.map(({ value, label }, idx) => {
                                return <MenuItem key={idx} value={value}>{label}</MenuItem>
                            })
                        }
                    </Select>
                    <FormHelperText>{isError && field.error}</FormHelperText>
                </FormControl>

            </InputContainer>
        );
    }
}