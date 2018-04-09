package com.iba.iot.datasimulator.common.config;

import com.mongodb.MongoClient;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.filter.AnnotationTypeFilter;

@Configuration
public class MorphiaConfig {

    @Autowired
    private MongoClient mongoClient;

    @Value("${mongodb.simulator.database}")
    private String databaseName;

    @Bean
    public Datastore datastore() throws ClassNotFoundException {

        Morphia morphia = new Morphia();

        // map entities, there is maybe a better way to find and map all entities
        ClassPathScanningCandidateComponentProvider entityScanner = new ClassPathScanningCandidateComponentProvider(false);
        entityScanner.addIncludeFilter(new AnnotationTypeFilter(Entity.class));
        entityScanner.addIncludeFilter(new AnnotationTypeFilter(Embedded.class));
        for (BeanDefinition candidate : entityScanner.findCandidateComponents("com.iba.iot.datasimulator")) { // from properties?
            morphia.map(Class.forName(candidate.getBeanClassName()));
        }

        return morphia.createDatastore(mongoClient, databaseName);
    }

}
