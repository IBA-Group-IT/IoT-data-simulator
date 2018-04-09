let amqp = require('amqplib');
let logger = require('../common/log');

class AmqpListener {

    /**
     *
     * @param sender
     */
    constructor(senders) {

        this.senders = senders;
        this.amqpUrl = process.env.RABBITMQ_URL;
    }

    /**
     *
     * @returns {Promise.<void>}
     */
    async run() {

        try {

            let connection =  await this.connect();

            connection.on("error", (error) => {
                this.processError(error);
                this.reconnect();
            });

            connection.on("close", () => this.reconnect());

        } catch (error) {

            this.processError(error);
            this.reconnect();
        }
    }

    /**
     *
     * @returns {Promise.<*>}
     */
    async connect() {

        let connection = await amqp.connect(this.amqpUrl);
        let channel = await connection.createChannel();

        await this.senders.forEach((sender) => sender.init(channel));

        return connection;
    }

    /**
     *
     */
    processError(error) {

        logger.error(">>> An error occurred during connection to amqp broker processing: " + error.message);
        logger.error(error);
    }

    /**
     *
     */
    reconnect() {

        logger.debug("Trying to reconnect to amqp broker in 3 second.");
        setTimeout(()=> {this.run()}, 3000);
    }

}

module.exports = AmqpListener;