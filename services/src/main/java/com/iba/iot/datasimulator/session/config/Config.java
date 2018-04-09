package com.iba.iot.datasimulator.session.config;

import com.iba.iot.datasimulator.common.model.schema.SchemaRootMetadataType;
import com.iba.iot.datasimulator.common.model.schema.property.metadata.SchemaPropertyMetadataType;
import com.iba.iot.datasimulator.common.model.schema.property.rule.SchemaPropertyRuleType;
import com.iba.iot.datasimulator.session.factory.timer.TimerProcessorFactory;
import com.iba.iot.datasimulator.session.model.active.command.SessionManagementCommand;
import com.iba.iot.datasimulator.session.model.active.timer.TimerType;
import com.iba.iot.datasimulator.session.service.active.manager.processor.ActiveSessionProcessor;
import com.iba.iot.datasimulator.session.service.active.processing.generator.schema.databind.DatasetEntryDeserializer;
import com.iba.iot.datasimulator.session.service.active.processing.generator.schema.databind.DatasetEntrySerializer;
import com.iba.iot.datasimulator.session.service.active.processing.generator.schema.processor.RuleProcessor;
import com.iba.iot.datasimulator.session.service.active.processing.generator.schema.processor.time.helper.TimeRuleHelper;
import com.iba.iot.datasimulator.session.service.active.entity.ActiveSession;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Configuration
public class Config {

    @Bean("sessionProcessors")
    Map<SessionManagementCommand, ActiveSessionProcessor> getSessionProcessors(Collection<ActiveSessionProcessor> registeredSessionProcessors) {

        return registeredSessionProcessors.stream().collect(Collectors.toMap(
            ActiveSessionProcessor::getCommand,
            Function.identity()
        ));
    }

    @Bean("timerProcessorFactories")
    Map<TimerType, TimerProcessorFactory> getTimerProcessorFactories(Collection<TimerProcessorFactory> factories) {

        return factories.stream().collect(Collectors.toMap(
            TimerProcessorFactory::getType,
            Function.identity()
        ));
    }

    @Bean
    @Qualifier("sessionStore")
    Map<String, ActiveSession> getActiveSessionStore() {
        return new HashMap<>();
    }

    @Bean
    Map<SchemaRootMetadataType, DatasetEntryDeserializer> getDatasetEntryDeserializers(Collection<DatasetEntryDeserializer> deserializers) {

        return deserializers.stream().collect(Collectors.toMap(
            DatasetEntryDeserializer::getType,
            Function.identity()
        ));
    }

    @Bean
    Map<SchemaRootMetadataType, DatasetEntrySerializer> getDatasetEntrySerializers(Collection<DatasetEntrySerializer> serializers) {

        return serializers.stream().collect(Collectors.toMap(
            DatasetEntrySerializer::getType,
            Function.identity()
        ));
    }

    @Bean
    Map<SchemaPropertyRuleType, RuleProcessor> getRuleProcessors(Collection<RuleProcessor> ruleProcessors) {

        return ruleProcessors.stream().collect(Collectors.toMap(
            RuleProcessor::getType,
            Function.identity()
        ));
    }

    @Bean
    Map<SchemaPropertyMetadataType, TimeRuleHelper> getTimeRuleProcessors(Collection<TimeRuleHelper> timeRuleHelpers) {

        return timeRuleHelpers.stream().collect(Collectors.toMap(
            TimeRuleHelper::getType,
            Function.identity()
        ));
    }
}
