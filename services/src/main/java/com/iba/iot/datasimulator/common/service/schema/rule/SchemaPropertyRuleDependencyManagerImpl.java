package com.iba.iot.datasimulator.common.service.schema.rule;

import com.iba.iot.datasimulator.common.model.schema.property.SessionSchemaProperty;
import com.iba.iot.datasimulator.common.model.schema.property.rule.Dependable;
import com.iba.iot.datasimulator.common.model.schema.property.rule.SchemaPropertyRule;
import com.iba.iot.datasimulator.session.model.unfold.UnfoldedSessionSchemaProperty;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class SchemaPropertyRuleDependencyManagerImpl implements SchemaPropertyRuleDependencyManager {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(SchemaPropertyRuleDependencyManagerImpl.class);

    @Override
    public Collection<Collection<UnfoldedSessionSchemaProperty>> findCircularRuleDependencies(Collection<UnfoldedSessionSchemaProperty> unfoldedSessionSchemaProperties) {

        logger.debug(">>> Looking for circular dependencies...");
        PropertiesRuleDependencies parsingResult = parsePropertyRuleDependencies(unfoldedSessionSchemaProperties);
        return parsingResult.getCyclicDependencies();
    }

    @Override
    public Collection<UnfoldedSessionSchemaProperty> orderByRuleDependencies(Collection<UnfoldedSessionSchemaProperty> unfoldedSessionSchemaProperties) {

        PropertiesRuleDependencies parsingResult = parsePropertyRuleDependencies(unfoldedSessionSchemaProperties);
        validateParsingResult(parsingResult);

        LinkedHashSet<UnfoldedSessionSchemaProperty> result = new LinkedHashSet<>(parsingResult.getIndependentProperties());
        for (Collection<UnfoldedSessionSchemaProperty> dependencySequence : parsingResult.getAcyclicDependencies()) {

            List<UnfoldedSessionSchemaProperty> reversedSequence = new ArrayList<>(dependencySequence);
            Collections.reverse(reversedSequence);
            result.addAll(reversedSequence);
        }

        return result;
    }

    /**
     *
     * @param parsingResult
     */
    private void validateParsingResult(PropertiesRuleDependencies parsingResult) {

        Collection<Collection<UnfoldedSessionSchemaProperty>> cyclicDependencySequences = parsingResult.getCyclicDependencies();
        if (!cyclicDependencySequences.isEmpty()) {

            logger.error(">>> The following cycling dependencies has been detected: {}", cyclicDependencySequences);
            throw new RuntimeException("Cyclic schema properties rule dependencies processing error.");
        }
    }

    /**
     *
     * @param unfoldedSchemaProperties
     * @return
     */
    private PropertiesRuleDependencies parsePropertyRuleDependencies(Collection<UnfoldedSessionSchemaProperty> unfoldedSchemaProperties) {

        Map<String, UnfoldedSessionSchemaProperty> propertiesByPosition =
                unfoldedSchemaProperties.stream()
                                        .collect(Collectors.toMap(UnfoldedSessionSchemaProperty::getPosition,
                                                                  Function.identity()));

        PropertiesRuleDependencies result = new PropertiesRuleDependencies();
        for (UnfoldedSessionSchemaProperty unfoldedSchemaProperty : unfoldedSchemaProperties) {

            String dependsOn = getRuleDependency(unfoldedSchemaProperty);
            if (StringUtils.isBlank(dependsOn)) {
                result.getIndependentProperties().add(unfoldedSchemaProperty);
            } else {
                processDependantProperty(propertiesByPosition, result, unfoldedSchemaProperty);
            }
        }

        return result;
    }

    /**
     *
     * @param propertiesByPosition
     * @param result
     * @param unfoldedSchemaProperty
     */
    private void processDependantProperty(Map<String, UnfoldedSessionSchemaProperty> propertiesByPosition, PropertiesRuleDependencies result, UnfoldedSessionSchemaProperty unfoldedSchemaProperty) {

        PropertyRuleDependencySequence dependencySequence = getPropertyRuleDependencies(null, unfoldedSchemaProperty, propertiesByPosition);

        Collection<UnfoldedSessionSchemaProperty> sequence = dependencySequence.getSequence();
        if (dependencySequence.isCyclic()) {
            result.getCyclicDependencies().add(sequence);
        } else {
            result.getAcyclicDependencies().add(sequence);
        }
    }

    /**
     *
     * @param dependencySequence
     * @param unfoldedSessionProperty
     * @param propertiesByPosition
     * @return
     */
    private PropertyRuleDependencySequence getPropertyRuleDependencies(PropertyRuleDependencySequence dependencySequence,
                                                                       UnfoldedSessionSchemaProperty unfoldedSessionProperty,
                                                                       Map<String, UnfoldedSessionSchemaProperty> propertiesByPosition) {

        logger.debug(">>> Looking for property {} rule dependency.", unfoldedSessionProperty);

        if (dependencySequence == null) {
            dependencySequence = new PropertyRuleDependencySequence();
            dependencySequence.setSequence(new LinkedHashSet<>());
        }

        Collection<UnfoldedSessionSchemaProperty> sequence = dependencySequence.getSequence();
        if (sequence.contains(unfoldedSessionProperty)) {

            dependencySequence.setCyclic(true);

            // Put into result full properties cycle
            List<UnfoldedSessionSchemaProperty> cyclicSequence = new ArrayList<>(sequence);
            cyclicSequence.add(unfoldedSessionProperty);
            dependencySequence.setSequence(cyclicSequence);

        } else {

            sequence.add(unfoldedSessionProperty);

            String dependsOn = getRuleDependency(unfoldedSessionProperty);
            if (StringUtils.isNoneBlank(dependsOn)) {

                UnfoldedSessionSchemaProperty nextUnfoldedSessionProperty = propertiesByPosition.get(dependsOn);
                if (nextUnfoldedSessionProperty != null) {
                    getPropertyRuleDependencies(dependencySequence, nextUnfoldedSessionProperty, propertiesByPosition);
                } else {

                    logger.error(">>> Property {} rule depends on non-existing session schema property {}", unfoldedSessionProperty, dependsOn);
                    throw new RuntimeException("Rule dependency to non existing schema property error.");
                }
            }
        }

        return dependencySequence;
    }

    /**
     *
     * @param schemaProperty
     * @return
     */
    private String getRuleDependency(UnfoldedSessionSchemaProperty schemaProperty) {

        SessionSchemaProperty property = schemaProperty.getProperty();
        SchemaPropertyRule rule = property.getRule();
        if (rule instanceof Dependable) {
            return ((Dependable) rule).getDependsOn();
        }

        return null;
    }
}
