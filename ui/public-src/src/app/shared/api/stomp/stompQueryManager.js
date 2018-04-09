import Deferred from 'es6-deferred';

/** **/
const WEBSOCKET_RESPONSE_SUCCESS = 'success';

class StompQueryManager {

    /**
     *
     * @param stompConnectionProvider
     */
    constructor(stompConnectionProvider) {

        this.connectionProvider = stompConnectionProvider;
        this.requestId = 0;
        this.deferreds = {};
        this.subscriptions = {};
    }

    /**
     *
     * @param payload
     */
    send = (params) => {

        console.log(`Going to send query to the API with the following params: ${JSON.stringify(params)}`);
        let {destinationPath, payload = {}, subscriptionPath} = params;

        let deferred = new Deferred();
        // Save destination path into deferred object for logging purposes
        deferred.destinationPath = destinationPath;

        this.connectionProvider
            .getConnection()
            .then((stompConnection) => {

                this.requestId++;
                this.deferreds[this.requestId] = deferred;
                this.subscribe(subscriptionPath, stompConnection);

                // Sending query
                console.log(`>>> Sending query to the API with the following params: ${JSON.stringify(params)}`);
                stompConnection.send(destinationPath, {requestId: this.requestId}, JSON.stringify(payload));
            })
            .catch(this.handleConnectionError.bind(this, deferred));

        return deferred.promise;
    };

    /**
     *
     * @param subscriptionPath
     * @param stompConnection
     */
    subscribe = (subscriptionPath, stompConnection) => {

        if (this.isSubscriptionNotExists(subscriptionPath)) {

            console.log(`>>> Creating new subscription for the path: ${subscriptionPath}`);
            this.subscriptions[subscriptionPath] = stompConnection.subscribe(subscriptionPath, this.handleSubscriptionMessage.bind(this, subscriptionPath));
        }
    };

    /**
     *
     * @param subscriptionPath
     * @param frame
     */
    handleSubscriptionMessage = (subscriptionPath, frame) => {

        //console.log(`>>> The following API subscription path ${subscriptionPath} response has been received: ${frame}`);

        let {body, headers} = frame;
        let response = JSON.parse(body);

        // Getting stored promise by unique requestId;
        let deferred = this.deferreds[headers.requestId];

        // Cleaning up deferred from deferreds store
        delete this.deferreds[headers.requestId];

        if (response.result === WEBSOCKET_RESPONSE_SUCCESS) {

            console.log(`>>> Resolving promise for destination path: ${deferred.destinationPath} with payload: ${JSON.stringify(response)}`);
            deferred.resolve(response);

        } else {

            console.error(`>>> Rejecting promise with the destination path: ${deferred.destinationPath}`);
            deferred.reject(response.errorMessage);
        }
    };

    /**
     *
     * @param deferred
     * @param error
     */
    handleConnectionError = (deferred, error) => {

        console.error(`>>> Handling stomp connection error...`, error);

        if (deferred.reject) {
            deferred.reject(error);
        }

        this.deferreds = {};
        this.subscriptions = {};
    };

    /**
     *
     * @param subscriptionPath
     * @returns {boolean}
     */
    isSubscriptionNotExists = (subscriptionPath) => {
        return !this.subscriptions[subscriptionPath];
    }
}

export default StompQueryManager;