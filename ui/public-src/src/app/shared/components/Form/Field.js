import React, { Component } from "react";
import { inject, observer } from "mobx-react";
import glamorous from "glamorous";

import Input, { InputLabel } from 'material-ui/Input';
import { FormControl, FormHelperText } from 'material-ui/Form';

const InputContainer = glamorous.div({
    position: "relative",
    display: 'inline-block'
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

        const {
            field,
            type,
            placeholder,
            style,
            inputProps,
            label,
            disabled
        } = this.props;

        // let isError = !field.isValidating &&
        //         field.hasBeenValidated &&
        //         field.dirty && field.error;

        let isError = field.dirty && field.hasError
        let value = (typeof field.value !== 'undefined' && field.value !== null) ? field.value : '';

        return (
            <InputContainer>

                <FormControl error={field.hasError}>
                    {label && <InputLabel shrink={true}>{label}</InputLabel>}
                    <Input 
                        type={type}
                        label={label}
                        placeholder={placeholder}
                        value={value}
                        style={style}
                        onChange={e => field.onChange(e.target.value)}
                        onBlur={field.enableAutoValidationAndValidate}
                        disabled={disabled === true ? true : false}
                    />
                    <FormHelperText>{field.error}</FormHelperText>
                </FormControl>

            </InputContainer>
        );
    }
}
