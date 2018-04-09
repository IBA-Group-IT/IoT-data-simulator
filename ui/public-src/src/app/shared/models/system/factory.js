import DummyTargetSystem from './DummyTargetSystem';
import RestTargetSystem from './RestTargetSystem';
import MqttTargetSystem from './MqttTargetSystem';
import WebsocketTargetSystem from './WebsocketTargetSystem';
import KafkaTargetSystem from './KafkaTargetSystem';
import S3TargetSystem from './S3TargetSystem';
import AMQPTargetSystem from './AMQPTargetSystem';

let typeToModelMap = {
    'websocket_endpoint': WebsocketTargetSystem,
    'rest_endpoint': RestTargetSystem,
    'dummy': DummyTargetSystem,
    'mqtt_broker': MqttTargetSystem,
    'kafka_broker': KafkaTargetSystem,
    's3': S3TargetSystem,
    'amqp_broker': AMQPTargetSystem
}

export default function({type, ...rest }) { 
    let Type = typeToModelMap[type];
    if(Type){
        return new Type({
            type,
            ...rest
        });
    }else{
        return new DummyTargetSystem();
    }
}