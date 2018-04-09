import React, { Component } from "react";
import { observable, action } from "mobx";
import { inject, observer } from "mobx-react";
import glamorous from "glamorous";
import Dialog, { DialogActions, DialogContent, DialogTitle } from 'material-ui/Dialog';
import Slide from "material-ui/transitions/Slide";

import AppBar from "material-ui/AppBar";
import Toolbar from "material-ui/Toolbar";
import IconButton from "material-ui/IconButton";
import CloseIcon from "material-ui-icons/Close";
import Typography from "material-ui/Typography";
import Button from "material-ui/Button";

const StyledDialog = glamorous(Dialog)();

const ContentLayout = glamorous.div({
    width: "100%",
    maxWidth: "1200px",
    margin: "20px auto",
    height: "100%",
    background: "#fafafa",
    display: "flex"
});

const ModalTopBar = glamorous(AppBar, {
    withProps: {
        color: "inherit",
        position: "static"
    }
})({
    position: "relative"
});

function Transition(props) {
    return <Slide direction="up" {...props} />;
}

@observer
class CreateUpdateModal extends Component {
    @observable isConfirmShown = false;

    @action.bound
    openConfirm = () => {
        this.isConfirmShown = true;
    }

    @action.bound
    closeConfirm = () => {
        this.isConfirmShown = false;
    }

    @action.bound
    onConfirm = () => {
        this.closeConfirm();
        this.props.onCancel();
    }

    constructor(props) {
        super(props);
    }

    render() {
        let { title, onClose, open, onCancel, ...dialogProps } = this.props;

        return (
            <StyledDialog
                fullScreen
                open={open}
                transition={Transition}
                onRequestClose={onClose}
                {...dialogProps}
            >
                <ModalTopBar>
                    <Toolbar>
                        <IconButton onClick={onClose} aria-label="Close">
                            <CloseIcon />
                        </IconButton>
                        <Typography type="title" style={{ flex: 1 }}>
                            {title}
                        </Typography>
                        {onCancel && (
                            <Button color="accent" onClick={this.openConfirm}>
                                Cancel
                            </Button>
                        )}
                    </Toolbar>
                </ModalTopBar>
                <ContentLayout>{this.props.children}</ContentLayout>

                {onCancel && (
                    <Dialog
                        ignoreBackdropClick
                        ignoreEscapeKeyUp
                        open={this.isConfirmShown}
                    >
                        <DialogTitle>Cancel confirmation</DialogTitle>
                        <DialogContent>
                            Progress will be lost. Are you sure you want to cancel creation process?
                        </DialogContent>
                        <DialogActions>
                            <Button onClick={this.closeConfirm} color="primary">
                                No
                            </Button>
                            <Button onClick={this.onConfirm} color="accent">
                                Yes
                            </Button>
                        </DialogActions>
                    </Dialog>
                )}
            </StyledDialog>
        );
    }
}

export default CreateUpdateModal;
