package com.iba.iot.datasimulator.common.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.iba.iot.datasimulator.common.json.converter.DataDefinitionCreateUpdateRequestMessageConverter;
import com.iba.iot.datasimulator.common.json.SchemaPropertyDeserializer;
import com.iba.iot.datasimulator.common.model.schema.SchemaRootMetadataType;
import com.iba.iot.datasimulator.common.model.schema.property.SchemaProperty;
import com.iba.iot.datasimulator.common.service.dataset.parser.entry.DatasetEntryTypedParser;
import com.iba.iot.datasimulator.common.service.dataset.schema.DatasetSchemaDeriver;
import com.iba.iot.datasimulator.definition.model.DatasetType;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Configuration
public class CommonConfig {

    @Bean
    public Tika getTika() {
        return new Tika();
    }

    @Bean
    Map<SchemaRootMetadataType, DatasetEntryTypedParser> getDatasetEntryParsers(Collection<DatasetEntryTypedParser> datasetEntryParsers) {
        return datasetEntryParsers.stream().collect(Collectors.toMap(
            DatasetEntryTypedParser::getType,
            Function.identity()
        ));
    }

    @Bean
    Map<DatasetType, DatasetSchemaDeriver> getDatasetSchemaDerivers(Collection<DatasetSchemaDeriver> schemaFactories) {
        return schemaFactories.stream().collect(Collectors.toMap(
            DatasetSchemaDeriver::getType,
            Function.identity()
        ));
    }

    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        return new MethodValidationPostProcessor();
    }

    @Bean
    @Primary
    @Qualifier("common")
    public ObjectMapper getCommonObjectMapper() {
        return new ObjectMapper();
    }

    @JsonDeserialize(using = SchemaPropertyDeserializer.class)
    interface SchemaPropertyMixin{}

    @Bean
    @Qualifier("dataDefinitionMapper")
    public ObjectMapper getDataDefinitionMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.addMixIn(SchemaProperty.class, SchemaPropertyMixin.class);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper;
    }

    @Bean
    public HttpMessageConverters customConverters() {

        return new HttpMessageConverters(true,
                Collections.singletonList(new DataDefinitionCreateUpdateRequestMessageConverter(getDataDefinitionMapper())));
    }

}
