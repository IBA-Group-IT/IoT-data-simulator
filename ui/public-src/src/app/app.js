import React, { Component } from "react";
import ReactDOM from "react-dom";
import axios from "axios";
import { createRouter } from "./router";
import { Provider } from "mobx-react";
import { reaction, whyRun } from "mobx";
// Polyfills
import es6Promise from "es6-promise";
es6Promise.polyfill();
import injectTapEventPlugin from "react-tap-event-plugin";
injectTapEventPlugin();

import StompConnectionProvider from 'api/stomp/stompConnectionProvider';
import AppStore from "./shared/stores/AppStore.js";
import MainScreen from "./screens/Main";

// Styles
import styles from "./shared/styles";
import MuiThemeProvider from "material-ui/styles/MuiThemeProvider";
import { ThemeProvider } from "glamorous";

window.whyRun = whyRun;

let transportLayer = axios.create();

let connectionProvider = new StompConnectionProvider({
    websocketUrl: '/ws',
    isStompTracingEnabled: false
});

let appStore = new AppStore(transportLayer, connectionProvider);

//Routing
createRouter(appStore);
reaction(
    () => appStore.view.url,
    path => {
        if (window.location.pathname !== path)
            window.history.pushState(null, null, path);
    }
);

class App extends Component {
    render() {
        return (
            <MuiThemeProvider theme={styles.theme}>
                <ThemeProvider theme={styles.theme}>
                    <Provider store={appStore}>
                        <MainScreen />
                    </Provider>
                </ThemeProvider>
            </MuiThemeProvider>
        );
    }
}

window.onpopstate = function historyChange(event) {
    if (event.type === "popstate") {
        router(window.location.pathname)
    };
};

    ReactDOM.render(<App />, document.getElementById("app"));