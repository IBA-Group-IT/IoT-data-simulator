import React, { Component } from "react";
import { inject, observer } from "mobx-react";
import MaterialTextField from "components/Form/inputs/MaterialTextField";
import Button from "material-ui/Button";

import glamorous from "glamorous";

const FormInnerContainer = glamorous.div({
    display: "flex",
    flexWrap: "wrap",
    maxWidth: "900px"
});

const Section = glamorous.div({
    marginTop: "0px",
    display: "flex",
    flexWrap: "wrap",
    flexDirection: "row"
});

const BucketTextField = glamorous(MaterialTextField, { 
    withProps: {
        style: {
            margin: '5px',
            width: '250px'
        }
    }
})({});

export default observer(({ form, onSubmit }) => {
    return (
        <form onSubmit={event => onSubmit(event, form)}>
            <Section>
                <BucketTextField
                    field={form.$("url")}
                />
                <BucketTextField
                    field={form.$("bucket")}
                />
                <BucketTextField
                    field={form.$("objectKey")}
                />
            </Section>
            <Section>
                <BucketTextField
                    field={form.$("secretKey")}
                />
                <BucketTextField
                    field={form.$("accessKey")}
                />
            </Section>
            <Section>
                <BucketTextField
                    field={form.$("name")}
                />
            </Section>
        </form>
    );
});
