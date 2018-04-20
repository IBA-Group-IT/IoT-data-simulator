package com.iba.iot.datasimulator.session.service.active.processing.producer;

import com.iba.iot.datasimulator.session.model.active.ActiveSessionPayload;
import com.iba.iot.datasimulator.session.service.active.processing.reader.DatasetReader;
import com.iba.iot.datasimulator.session.service.active.processing.timer.TimerProcessor;
import io.reactivex.ObservableEmitter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.ParseException;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class DataProducer extends AbstractManageableObservableOnSubscribe<ActiveSessionPayload> {

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(DataProducer.class);

    /** **/
    private static final String EMPTY_PAYLOAD = "ping";

    /** **/
    private DatasetReader<String> datasetReader;

    /** **/
    private TimerProcessor timerProcessor;

    /** **/
    private String previousDatasetEntry;

    /** **/
    private int ticksThreshold;

    /** **/
    private int ticksCounter;

    /** **/
    private ObservableEmitter<ActiveSessionPayload> emitter;

    /**
     *
     * @param datasetReader
     * @param timerProcessor
     */
    public DataProducer(DatasetReader<String> datasetReader, TimerProcessor timerProcessor, int ticksNumber) {

        this.datasetReader = datasetReader;
        this.timerProcessor = timerProcessor;
        this.ticksThreshold = ticksNumber;
    }

    @Override
    public void subscribe(ObservableEmitter<ActiveSessionPayload> emitter) throws Exception {

        this.emitter = emitter;
        if (datasetReader != null) {

            logger.debug(">>> Processing dataset reading.");
            processDatasetEntry();

        } else {

            logger.debug(">>> Processing empty data generation.");
            processEmptyEntry();
        }
    }

    /**
     *
     * @throws InterruptedException
     * @throws IOException
     * @throws ParseException
     */
    private void processDatasetEntry() throws InterruptedException, IOException, ParseException {

        while (datasetReader.hasNext() && getState() != ObservableState.STOPPED) {

            String nextDatasetEntry = datasetReader.next();
            long waitInterval = timerProcessor.getWaitInterval(previousDatasetEntry, nextDatasetEntry);

            logger.debug(">>> Waiting for {} ms before sending next dataset entry", waitInterval);
            if (!processWaiting(waitInterval)) break;
            processPause();

            logger.debug(">>> Sending next dataset item: {}", nextDatasetEntry);

            ActiveSessionPayload activeSessionPayload = new ActiveSessionPayload(true, nextDatasetEntry);
            emitter.onNext(activeSessionPayload);

            previousDatasetEntry = nextDatasetEntry;
            handleTicksRestriction();
        }

        logger.debug(">>> Dataset reading has been completed.");
        emitter.onComplete();
    }

    /**
     *
     * @throws InterruptedException
     * @throws IOException
     * @throws ParseException
     */
    private void processEmptyEntry() throws InterruptedException, IOException, ParseException {

        while (getState() != ObservableState.STOPPED) {

            ActiveSessionPayload activeSessionPayload = new ActiveSessionPayload(false, EMPTY_PAYLOAD);
            emitter.onNext(activeSessionPayload);

            long waitInterval = timerProcessor.getWaitInterval(null, null);
            if (!processWaiting(waitInterval)) break;

            processPause();
            handleTicksRestriction();
        }

        logger.debug(">>> Event sending has been completed.");
        emitter.onComplete();
    }

    /**
     *
     */
    private void handleTicksRestriction() {

        if (ticksThreshold > 0) {

            ticksCounter++;
            if (ticksCounter >= ticksThreshold) {

                logger.debug(">>> Stopping session due to ticks threshold exceeding.");
                super.stop();
            }
        }
    }

    @Override
    public void stop() {
        super.stop();

        if (emitter != null) {
            emitter.onComplete();
        }
    }
}
