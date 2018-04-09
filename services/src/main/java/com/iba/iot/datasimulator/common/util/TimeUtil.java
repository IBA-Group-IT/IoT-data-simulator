package com.iba.iot.datasimulator.common.util;

import com.iba.iot.datasimulator.common.model.IntervalMetric;
import com.iba.iot.datasimulator.common.model.TimestampType;
import com.iba.iot.datasimulator.common.model.schema.property.metadata.FormattedTypedSchemaPropertyMetadata;
import com.iba.iot.datasimulator.common.model.schema.property.metadata.SchemaPropertyMetadataType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 */
public class TimeUtil {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(TimeUtil.class);

    /**
     *
     * @return
     */
    public static long getUnixTime(){
        return getCurrentTime() / 1000;
    }

    /**
     *
     * @return
     */
    public static long getCurrentTime() {
        return System.currentTimeMillis();
    }

    /**
     *
     * @param timestamp
     * @param metric
     * @param shift
     * @return
     */
    public static long getTimestamp(long timestamp, IntervalMetric metric, Integer shift) {

        if (metric != null) {

            switch(metric) {

                case MILLISECONDS:
                    timestamp += shift;
                    break;

                case SECONDS:
                    timestamp += shift * 1000;
                    break;

                case MINUTES:
                    timestamp += shift * 1000 * 60;
                    break;

                case HOURS:
                    timestamp += shift * 1000 * 60 * 60;
                    break;
            }
        }

        return timestamp;
    }

    /**
     *
     * @param datasetEntry
     * @param metadata
     * @return
     * @throws ParseException
     */
    public static long parseDate(String datasetEntry, FormattedTypedSchemaPropertyMetadata metadata) throws ParseException {

        SchemaPropertyMetadataType dateType = metadata.getType();
        switch (dateType) {

            case TIMESTAMP:

                TimestampType timestampType = TimestampType.fromString(metadata.getFormat());
                Long time = Long.valueOf(datasetEntry);
                if (timestampType == TimestampType.SECONDS) {
                    time *= 1000;
                }
                return time;

            case DATE:
                return new SimpleDateFormat(metadata.getFormat()).parse(datasetEntry).getTime();
        }

        logger.debug(">>> Cannot parse raw time value {} by provided description {}", datasetEntry, metadata);
        throw new RuntimeException("Dataset date value parsing error.");
    }

    /**
     *
     * @param metadata
     * @param propertyTime
     * @return
     */
    public static String formatDateTime(FormattedTypedSchemaPropertyMetadata metadata, long propertyTime) {

        String format = metadata.getFormat();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(new Date(propertyTime));
    }

    /**
     *
     * @param metadata
     * @param propertyTime
     * @return
     */
    public static long formatTimestamp(FormattedTypedSchemaPropertyMetadata metadata, long propertyTime) {

        if (TimestampType.fromString(metadata.getFormat()) == TimestampType.SECONDS) {
            return propertyTime / 1000;
        } else {
            return propertyTime;
        }
    }

    /**
     *
     * @param value
     * @return
     */
    public static long getIntervalMilliseconds(long value, IntervalMetric timeMetric) {

        switch (timeMetric) {

            case MILLISECONDS:
                return value;

            case SECONDS:
                return value * 1000;

            case MINUTES:
                return value * 60 * 1000;

            case HOURS:
                return value * 60 * 60 * 1000;
        }

        return 0L;
    }
}
