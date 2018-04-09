export default {

    paths: {

        getQuerySubscriptionPath: (queryDestinationPath) => `/user/queue${queryDestinationPath}`,

        /** **/
        getSessionsStatusQueryDestinationPath: () => '/sessions',

        /** **/
        getSessionQueryDestinationPath: (sessionId) => `/sessions.${sessionId}`,

        /** **/
        getSessionsSubscriptionPath: () => '/topic/sessions.#',

        /** **/
        getSessionSubscriptionPath: (sessionId) => `/topic/sessions.${sessionId}.#`,

        /** **/
        getSessionPayloadSubscriptionPath: (sessionId) => `/topic/sessions.${sessionId}.payload`,

        /** **/
        getSessionErrorsSubscriptionPath: (sessionId) => `/topic/sessions.${sessionId}.errors`,

        /** **/
        getSessionAnalyticsSubscriptionPath: (sessionId) => `/topic/sessions.${sessionId}.analytics`,

        /** **/
        getErrorsSubscriptionPath: () => '/user/queue/errors'
    }
    
}