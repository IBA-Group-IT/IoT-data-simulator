
import React, { Component } from "react";
import { inject, observer } from "mobx-react";
import glamorous from "glamorous";

import DummyItem from './DummyItem';
import WebsocketItem from './WebsocketItem';
import RestItem from './RestItem';
import MqttItem from './MqttItem';
import KafkaItem from './KafkaItem';
import S3Item from './S3Item';
import AmqpItem from './AmqpItem';

import SystemSecurity from './SystemListItemSecurity';
import SystemHeaders from './SystemListItemHeaders';


import {
    Title,
    Container,
    DetailsTable,
    DetailsTableRow,
    DetailsTableCell,
    DetailsTableTitle
} from "components/EntityList/style";

const SystemType = glamorous.div({
    color: '#01579b',
    fontSize: '14px',
    marginTop: '5px'
});

const TypeLayout = glamorous.div({
    marginTop: '10px',
    height: '60px',
    overflow: 'auto'
});

const Section = glamorous.div({
    marginTop: '10px'
});


@observer
class SystemListItem extends Component { 

    constructor(props) {
        super(props);
    }

    typeToComponentMap = {
        'dummy': DummyItem,
        'mqtt_broker': MqttItem,
        'websocket_endpoint': WebsocketItem,
        'rest_endpoint': RestItem,
        'kafka_broker': KafkaItem,
        's3': S3Item,
        'amqp_broker': AmqpItem
    }

    typeToMessageMap = {
        'mqtt_broker': 'MQTT',
        'websocket_endpoint': 'Websocket',
        'rest_endpoint': 'REST',
        'dummy': 'Dummy',
        'kafka_broker': 'Kafka',
        's3': 'Local object storage',
        'amqp_broker': 'AMQP'
    }

    getComponentByType(type) { 
        return this.typeToComponentMap[type];
    }

    renderItem = (system) => {
        let Cmp = this.getComponentByType(system.type)
        return <Cmp system={system} />
    }

    formatType(systemType) { 
        return this.typeToMessageMap[systemType];
    }

    render() {
        let { system } = this.props;
        
        return (
            <Container>
                <Title>{ system.name }</Title>
                <DetailsTable>
                    <DetailsTableRow><SystemType>{ this.formatType(system.type) }</SystemType></DetailsTableRow>
                    { this.renderItem(system) }
                    { system.security && <SystemSecurity security={system.security} /> }
                    { system.headers && <SystemHeaders headers={system.headers} /> }
                </DetailsTable>
                
            </Container>
        )
    }
    
}

export default SystemListItem;
