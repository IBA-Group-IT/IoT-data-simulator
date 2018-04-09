

export default class LogEntry {

    static types = {
        status: 'session_status',
        payload: 'session_payload',
        analytics: 'session_analytics',
        error: 'session_error'
    }
    
    constructor(params) { 
        Object.assign(this, params);
    }

}