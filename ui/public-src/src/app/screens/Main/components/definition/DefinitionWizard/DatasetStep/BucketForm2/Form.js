import React, { Component } from "react";
import { inject, observer } from "mobx-react";
import Button from "material-ui/Button";

import glamorous from "glamorous";
import Field from 'components/Form/Field';

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

const BucketTextField = glamorous(Field, {
    withProps: {
        style: {
            width: "250px",
            marginRight: '10px'
        }
    }
})({});

export default observer(({ form }) => {
    return (
        <form>
            <Section>
                <BucketTextField label='URL' placeholder="URL" field={form.$.url} />
                <BucketTextField label="Bucket" placeholder="Bucket" field={form.$.bucket} />
                <BucketTextField label="Object key" placeholder="Object key" field={form.$.objectKey} />
            </Section>
            <Section>
                <BucketTextField label="Secret key" placeholder="Secret key" field={form.$.secretKey} />
                <BucketTextField label="Access key" placeholder="Access key" field={form.$.accessKey} />
            </Section>
            <Section>
                <BucketTextField label="Name" placeholder="Name" field={form.$.name} />
            </Section>
        </form>
    );
});
