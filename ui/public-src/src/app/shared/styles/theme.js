import createMuiTheme from "material-ui/styles/createMuiTheme";
import red from 'material-ui/colors/red';
import lightBlue from 'material-ui/colors/lightBlue';

const theme = {
    palette: {
        primary: {
            ...lightBlue,
        },
        secondary: red
    }
};

export default {
    theme: createMuiTheme(theme)
};
