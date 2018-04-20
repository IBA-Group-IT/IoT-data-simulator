import React from "react";
import { observer } from "mobx-react";
import glamorous from "glamorous";

import Checkbox from "material-ui/Checkbox";
import { InputLabel } from "material-ui/Input";
import { FormControl, FormControlLabel } from "material-ui/Form";

const InputContainer = glamorous.div({
    paddingBottom: "15px",
    position: "relative"
});
const Error = glamorous.small({
    color: "red",
    display: "block",
    marginLeft: "5px",
    position: "absolute"
});

export default observer(
    ({ field, placeholder = null, label, style, disabled, checked, value }) => (
        <InputContainer>
            <FormControl>
                <FormControlLabel
                    control={
                        <Checkbox
                            style={style}
                            {...field.bind({
                                checked: field.value,
                                value: field.key
                            })}
                        />
                    }
                    label={field.label}
                />
            </FormControl>
            <Error>{field.error}</Error>
        </InputContainer>
    )
);
