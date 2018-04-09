import SockJS from 'sockjs-client';
import Stomp from 'stompjs';

class StompConnectionProvider {

    /**
     *
     */
    constructor(config) {
        this.connection = null;
        this.reconnectionListeners = [];
        this.config = config;
    }

    /**
     *
     * @returns {null}
     */
    getConnection = () => {

        if (!this.connection) {
            this.openConnection();
        }

        return this.connection;
    };

    /**
     *
     * @param listener
     */
    onReconnect = (listener) => {

        this.reconnectionListeners.push(listener);

        // return unsubscription handler
        return () => {
            let listenerIndex  = this.reconnectionListeners.indexOf(listener);
            this.reconnectionListeners.splice(listenerIndex, 1);
        }
    };

    /**
     *
     */
    openConnection = () => {

        this.connection = new Promise((resolve, reject) => {

            let socket = new SockJS(this.config.websocketUrl, null, {transports: "websocket"});
            let stompConnection = Stomp.over(socket);
            if (!this.config.isStompTracingEnabled) {
                stompConnection.debug = null;
            }

            console.log(`>>> Opening connection to the ${this.config.websocketUrl} API via Websocket & STOMP protocol`);
            stompConnection.connect(

                {},

                (frame) => {
                    console.log(`>>> Successfully connected to the API via websockets & STOMP: ${frame}`);
                    resolve(stompConnection);
                },

                (error) => {

                    console.error(`>>> Websockets & STOMP connection error: ${error}`);
                    this.connection = null;
                    stompConnection = null;

                    if (reject) {
                        reject(error);
                    }

                    this.reconnect();
                }
            );

        });
    };

    /**
     *
     */
    reconnect = () => {

        setTimeout(() => {

            console.log(">>> Trying to re-connect to websocket STOMP API.");
            this.getConnection()
                .then((stompConnection) => {
                    this.reconnectionListeners.forEach((listener) => listener(stompConnection));
                });

        }, this.config.reconnectionInterval);
    }
}

export default StompConnectionProvider;