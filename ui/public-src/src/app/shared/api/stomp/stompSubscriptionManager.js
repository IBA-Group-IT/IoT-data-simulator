import Deferred from 'es6-deferred';

class StompSubscriptionManager {

    /**
     *
     * @param stompConnectionProvider
     */
    constructor(stompConnectionProvider) {

        this.connectionProvider = stompConnectionProvider;
        stompConnectionProvider.onReconnect(this.handleReconnect);

        this.subscriptions = {};
    }

    /**
     *
     * @param params
     * @returns {global.Promise}
     */
    subscribe = (params) => {

        console.log(`Subscribing to the API with the following params: ${JSON.stringify(params)}`);
        let {subscriptionPath, consumer} = params;

        let deferred = new Deferred();

        this.connectionProvider
            .getConnection()
            .then((stompConnection) => {

                if (this.isSubscriptionNotExists(subscriptionPath)) {
                    this.createSubscription(stompConnection, subscriptionPath);
                }
                this.registerConsumer(subscriptionPath, consumer);

            })
            .catch(this.handleConnectionError.bind(this, deferred));

        // Provide unsubscribe function to the clients
        deferred.resolve(this.createUnsubscription(subscriptionPath, consumer));

        return deferred.promise;
    };

    /**
     *
     * @param stompConnection
     * @param subscriptionPath
     */
    createSubscription = (stompConnection, subscriptionPath) => {

        console.log(`>>> Creating new subscription for the path: ${subscriptionPath}`);
        let unsubscribe = stompConnection.subscribe(subscriptionPath, this.handleSubscriptionMessage.bind(this, subscriptionPath)).unsubscribe;
        this.subscriptions[subscriptionPath] = {consumers: [], unsubscribe};
    };

    /**
     *
     * @param subscriptionPath
     * @param consumer
     */
    registerConsumer = (subscriptionPath, consumer) => {
        this.subscriptions[subscriptionPath].consumers.push(consumer);
    };

    /**
     *
     * @param subscriptionPath
     * @param frame
     */
    handleSubscriptionMessage = (subscriptionPath, frame) => {

        //console.log(`>>> The following API subscription path ${subscriptionPath} response has been received`, frame);
        let response = JSON.parse(frame.body);
        this.subscriptions[subscriptionPath].consumers.forEach(consumer => consumer(response));
    };

    /**
     *
     * @param subscriptionPath
     * @returns {boolean}
     */
    isSubscriptionNotExists = (subscriptionPath) => {
        return !this.subscriptions[subscriptionPath];
    };

    /**
     *
     * @param subscriptionPath
     * @param consumer
     * @returns {function()}
     */
    createUnsubscription = (subscriptionPath, consumer) => {

        return () => {

            if (this.subscriptions[subscriptionPath]) {

                console.log(`>>> Unsubscribing consumer for path: ${subscriptionPath}`);

                let consumers = this.subscriptions[subscriptionPath].consumers;
                let consumerIndex  = consumers.indexOf(consumer);
                consumers.splice(consumerIndex, 1);

                /**
                 *  In case of no alive consumers remove stomp connection subscription
                 */
                if (consumers.length === 0) {

                    console.log(`>>> Unsubscribing from the API listening for the path: ${subscriptionPath}`);
                    this.subscriptions[subscriptionPath].unsubscribe();
                    delete this.subscriptions[subscriptionPath];
                }
            }
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
    };

    /**
     *
     * @param stompConnection
     */
    handleReconnect = (stompConnection) => {

        Object.entries(this.subscriptions)
              .forEach(([subscriptionPath, subscription]) => {

                  console.log(`>>> Resubscribing STOMP API for path ${subscriptionPath}`);
                  subscription.unsubscribe = stompConnection.subscribe(subscriptionPath,
                      this.handleSubscriptionMessage.bind(this, subscriptionPath)).unsubscribe;
              });
    }
}

export default StompSubscriptionManager;
