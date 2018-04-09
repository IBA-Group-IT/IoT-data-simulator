package com.iba.iot.datasimulator.common.factory.schema.property.rule;

import com.iba.iot.datasimulator.common.model.schema.property.rule.*;
import com.iba.iot.datasimulator.common.model.schema.property.rule.random.BooleanRandomSchemaPropertyRule;
import com.iba.iot.datasimulator.common.model.schema.property.rule.random.DoubleRandomSchemaPropertyRule;
import com.iba.iot.datasimulator.common.model.schema.property.rule.random.IntegerRandomSchemaPropertyRule;
import com.iba.iot.datasimulator.common.model.schema.property.rule.random.LongRandomSchemaPropertyRule;
import com.iba.iot.datasimulator.common.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Service
public class SchemaPropertyRuleBuilderImpl implements SchemaPropertyRuleBuilder {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(SchemaPropertyRuleBuilderImpl.class);

    @Value("${schema.default.random.boolean.rule.success.probability}")
    private double randomBooleanSuccessProbability;

    @Value("${schema.default.random.integer.rule.min}")
    private int minRandomInteger;

    @Value("${schema.default.random.integer.rule.max}")
    private int maxRandomInteger;

    @Value("${schema.default.random.long.rule.min}")
    private long minRandomLong;

    @Value("${schema.default.random.long.rule.max}")
    private long maxRandomLong;

    @Value("${schema.default.random.double.rule.min}")
    private double minRandomDouble;

    @Value("${schema.default.random.double.rule.max}")
    private double maxRandomDouble;

    /** **/
    private String arrayWithoutDatasetCustomFunction;

    /** **/
    private String arrayWithDatasetCustomFunction;

    /** **/
    private String objectWithoutDatasetCustomFunction;

    /** **/
    private String objectWithDatasetCustomFunction;

    @PostConstruct
    private void init() throws IOException {

        logger.debug(">>> Reading arra & object custom functions templates.");

        arrayWithDatasetCustomFunction = FileUtil.readFile("templates/array-with-dataset-custom-function.js");
        arrayWithoutDatasetCustomFunction = FileUtil.readFile("templates/array-without-dataset-custom-function.js");
        objectWithoutDatasetCustomFunction = FileUtil.readFile("templates/object-without-dataset-custom-function.js");
        objectWithDatasetCustomFunction = FileUtil.readFile("templates/object-with-dataset-custom-function.js");
    }

    @Override
    public SchemaPropertyRule buildAsIsRule() {

        logger.debug(">>> Building as-is rule");
        return new AsIsSchemaPropertyRule();
    }

    @Override
    public SchemaPropertyRule buildCurrentTimeRule() {

        logger.debug(">>> Building current time rule");
        return new CurrentTimeSchemaPropertyRule();
    }

    @Override
    public SchemaPropertyRule buildRelativeTimeRule(String relativePosition) {

        logger.debug(">>> Building relative time rule with relative position {}", relativePosition);
        return new RelativeTimeSchemaPropertyRule(relativePosition);
    }

    @Override
    public SchemaPropertyRule buildRandomBooleanRule() {

        logger.debug(">>> Building random boolean rule with success probability", randomBooleanSuccessProbability);
        return new BooleanRandomSchemaPropertyRule(randomBooleanSuccessProbability);
    }

    @Override
    public SchemaPropertyRule buildRandomIntegerRule() {

        logger.debug(">>> Building random integer rule with min {} and max {} intervals.", minRandomInteger, maxRandomInteger);
        return new IntegerRandomSchemaPropertyRule(minRandomInteger, maxRandomInteger);
    }

    @Override
    public SchemaPropertyRule buildRandomLongRule() {

        logger.debug(">>> Building random long rule with min {} and max {} intervals.", minRandomLong, maxRandomLong);
        return new LongRandomSchemaPropertyRule(minRandomLong, maxRandomLong);
    }

    @Override
    public SchemaPropertyRule buildRandomDoubleRule() {

        logger.debug(">>> Building random double rule with min {} and max {} intervals.", minRandomDouble, maxRandomDouble);
        return new DoubleRandomSchemaPropertyRule(minRandomDouble, maxRandomDouble);
    }

    @Override
    public SchemaPropertyRule buildUuidRule() {

        logger.debug(">>> Building UUID string rule");
        return new UuidSchemaPropertyRule();
    }

    @Override
    public SchemaPropertyRule buildUuidRule(String prefix) {

        logger.debug(">>> Building UUID string rule with prefix {}", prefix);
        return new UuidSchemaPropertyRule(prefix);
    }

    @Override
    public SchemaPropertyRule buildArrayCustomFunctionRule(boolean isDatasetProvided) {

        logger.debug(">>> Building CUSTOM_FUNCTION rule for array with isDatasetProvided={}", isDatasetProvided);
        CustomFunctionSchemaPropertyRule customFunctionRule = new CustomFunctionSchemaPropertyRule();

        if (isDatasetProvided) {
            customFunctionRule.setFunction(arrayWithDatasetCustomFunction);
        } else {
            customFunctionRule.setFunction(arrayWithoutDatasetCustomFunction);
        }

        return customFunctionRule;
    }

    @Override
    public SchemaPropertyRule buildObjectCustomFunctionRule(boolean isDatasetProvided) {

        logger.debug(">>> Building CUSTOM_FUNCTION rule for object with isDatasetProvided={}", isDatasetProvided);
        CustomFunctionSchemaPropertyRule customFunctionRule = new CustomFunctionSchemaPropertyRule();

        if (isDatasetProvided) {
            customFunctionRule.setFunction(objectWithDatasetCustomFunction);
        } else {
            customFunctionRule.setFunction(objectWithoutDatasetCustomFunction);
        }

        return customFunctionRule;
    }
}
