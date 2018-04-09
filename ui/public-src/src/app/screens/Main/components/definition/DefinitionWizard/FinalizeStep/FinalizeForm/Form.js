import React, { Component } from "react";
import { inject, observer } from "mobx-react";
import glamorous from "glamorous";
import { css } from "glamor";

import MaterialTextField from "components/Form/inputs/MaterialTextField";
import Button from "material-ui/Button";

const FormInnerContainer = glamorous.div({
    display: "flex",
    flexWrap: "wrap",
    maxWidth: '900px'
});

const NameInput = glamorous(MaterialTextField)({});
NameInput.defaultProps = {
    style: {
        margin: '5px'
    }
}

export default observer(({ form }) => {
  return (
    <form onSubmit={form.onSubmit}>
        <FormInnerContainer>
            <NameInput field={form.$("name")} />
        </FormInnerContainer>
    </form>
  )
});
