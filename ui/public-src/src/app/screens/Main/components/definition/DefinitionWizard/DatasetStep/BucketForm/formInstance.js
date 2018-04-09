
import React, { Component } from "react";
import { inject, observer } from "mobx-react";
import glamorous from "glamorous";

import BaseForm from 'components/Form';
import setup from './setup';

class BucketForm extends BaseForm { 
    constructor(props) {
        super(props);
    }
    setup() {
        return setup;
    }
}

export default BucketForm;
