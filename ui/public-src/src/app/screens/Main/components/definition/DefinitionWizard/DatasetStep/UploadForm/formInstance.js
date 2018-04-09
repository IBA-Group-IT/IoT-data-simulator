
import React, { Component } from "react";
import { inject, observer } from "mobx-react";
import glamorous from "glamorous";
import { css } from 'glamor';

import BaseForm from 'components/Form';
import setup from './setup';

class UploadForm extends BaseForm { 
    constructor(props) {
        super(props);
    }
    setup() {
        return setup;
    }
}

const form = new UploadForm();
export default form;
