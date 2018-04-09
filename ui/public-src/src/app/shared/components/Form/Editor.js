import React, { Component } from "react";
import { observer } from "mobx-react";
import glamorous from "glamorous";

import AceEditor from "react-ace";
import "brace/mode/javascript";
import "brace/theme/monokai";

const EditorContainer = glamorous.div({
    width: "100%",
    height: "100%",
    position: 'relative'
});

const Error = glamorous.div({
    color: "red",
    fontSize: "15px",
    position: "absolute",
    bottom: "-20px",
    left: "0",
    right: "0",
    margin: "auto",
    textAlign: "center"
});

@observer
export default class Editor extends Component {
    render() {
        const { field, ...rest } = this.props;

        let isError = field.dirty && field.hasError;

        return (
            <EditorContainer>
                <AceEditor
                    mode="javascript"
                    theme="monokai"
                    width="100%"
                    value={field.value}
                    name="editor"
                    onChange={value => field.onChange(value)}
                    editorProps={{
                        $blockScrolling: true
                    }}
                    {...rest}
                />
                {isError && <Error>{field.error}</Error>}
            </EditorContainer>
        );
    }
}
