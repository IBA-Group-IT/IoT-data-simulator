import React, { Component } from "react";
import { inject, observer } from "mobx-react";
import glamorous from "glamorous";

import Dialog from "material-ui/Dialog";
import Snackbar, { SnackbarContent } from "material-ui/Snackbar";
import { withStyles } from "material-ui/styles";


const styles = ({ palette }) => {
    return {
        content: {
            backgroundColor: palette.secondary['A200'],
        }
    }
};

@inject("store")
@observer
class ErrorPanel extends Component {
    render() {
        let { lastError } = this.props.store.errorStore;

        if (lastError) {
            return (
                <Dialog
                    BackdropInvisible={true}
                    open={!!lastError}
                    onRequestClose={() => lastError.markAsRead()}
                >
                    <Snackbar
                        anchorOrigin={{
                            vertical: "bottom",
                            horizontal: "left"
                        }}
                        open={!!lastError}
                        autoHideDuration={3000}
                        onRequestClose={() => lastError.markAsRead()}
                    >
                        <SnackbarContent
                            className={this.props.classes.content}
                            message={
                                <span id="message-id">
                                    {lastError.message ||
                                        "Server error occurred"}
                                </span>
                            }
                        >
                            {lastError.message || "Server error occurred"}
                        </SnackbarContent>
                    </Snackbar>
                </Dialog>
            );
        }

        return null;
    }
}

export default withStyles(styles)(ErrorPanel);
