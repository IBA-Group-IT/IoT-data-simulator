package com.iba.iot.datasimulator.session.service.active.processing.timer;

import com.iba.iot.datasimulator.common.model.schema.Schema;
import com.iba.iot.datasimulator.common.model.schema.SchemaRootMetadataType;
import com.iba.iot.datasimulator.common.model.schema.property.SchemaProperty;
import com.iba.iot.datasimulator.common.model.schema.property.metadata.FormattedTypedSchemaPropertyMetadata;
import com.iba.iot.datasimulator.common.service.dataset.parser.entry.DatasetEntryParser;
import com.iba.iot.datasimulator.common.service.schema.lookup.SchemaPropertyLookupService;
import com.iba.iot.datasimulator.common.util.TimeUtil;
import com.iba.iot.datasimulator.session.model.active.timer.DatasetTimer;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.text.ParseException;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class DatasetTimerProcessor implements TimerProcessor {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(DatasetTimerProcessor.class);

    /** **/
    private DatasetTimer timer;

    /** **/
    private Schema schema;

    /** **/
    private FormattedTypedSchemaPropertyMetadata timePropertyMetadata;

    @Autowired
    private SchemaPropertyLookupService propertyLookupService;

    @Autowired
    private DatePropertyValidator datePropertyValidator;

    @Autowired
    private DatasetEntryParser datasetEntryParser;

    /**
     *
     * @param timer
     */
    public DatasetTimerProcessor(DatasetTimer timer, Schema schema) {

        this.timer = timer;
        this.schema = schema;
    }

    @PostConstruct
    private void init() {

        String datePosition = timer.getDatePosition();
        SchemaProperty property = propertyLookupService.findProperty(datePosition, schema);

        datePropertyValidator.validate(timer.getDatePosition(), property);

        timePropertyMetadata = (FormattedTypedSchemaPropertyMetadata) property.getMetadata();
    }

    @Override
    public long getWaitInterval(String previousEntry, String nextEntry) throws ParseException, IOException {

        // In case of first dataset entry reading there shouldn't be any delay for generatedPayload sending
        if (StringUtils.isEmpty(previousEntry)) {
            return 0;
        }

        long previousTimestamp = parseDate(previousEntry);
        long nextTimeStamp = parseDate(nextEntry);

        if (nextTimeStamp < previousTimestamp) {

            logger.warn(">>> Next dataset time value {} less than previous one {}. So, wait interval will be zero.", nextEntry, previousEntry);
            return 0;
        }

        double replayRate = timer.getReplayRate().doubleValue();
        return(long)((nextTimeStamp - previousTimestamp) / replayRate);
    }

    /**
     *
     * @param datasetEntry
     * @return
     */
    private long parseDate(String datasetEntry) throws ParseException, IOException {

        String rawValue = parseRawValue(datasetEntry).trim();
        return TimeUtil.parseDate(rawValue, timePropertyMetadata);
    }

    /**
     *
     * @param datasetEntry
     * @return
     */
    private String parseRawValue(String datasetEntry) throws IOException {

        SchemaRootMetadataType schemaType = schema.getMetadata().getType();
        return datasetEntryParser.getValue(datasetEntry, timer.getDatePosition(), schemaType);
    }
}
