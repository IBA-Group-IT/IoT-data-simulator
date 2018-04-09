import React from "react";
import { observer } from "mobx-react";
import TextField from "material-ui/TextField";
import glamorous from "glamorous";
import Input from "material-ui/Input";

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
    ({ field, type = "text", placeholder = null, label, style, disabled }) => (
        <InputContainer>
            <TextField
                style={style}
                InputLabelProps={{
                    shrink: true
                }}
                {...field.bind({ type, placeholder, label, disabled })}
            />
            <Error>{field.error}</Error>
        </InputContainer>
    )
);
