import React from "react";
import { observer } from "mobx-react";

import Input, { InputLabel } from "material-ui/Input";
import { MenuItem } from "material-ui/Menu";
import { FormControl, FormHelperText } from "material-ui/Form";
import Select from "material-ui/Select";

import glamorous from "glamorous";

const MaterialSelectLayout = glamorous.div({
    paddingBottom: '10px'
}); 

export default observer(({ field, style={}, ...rest }) => (
    <MaterialSelectLayout>
        <FormControl>
            <InputLabel shrink={true} htmlFor={field.id}>{field.label}</InputLabel>
            <Select style={style} input={<Input id={field.id} value={field.value} />} {...rest} {...field.bind()}>
                {field.extra.map(option => (
                    <MenuItem key={option.value} value={option.value}>
                        {option.label}
                    </MenuItem>
                ))}
            </Select>
        </FormControl>
    </MaterialSelectLayout>
));
