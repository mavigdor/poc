package com.gigaspaces.poc.processor;

import com.gigaspaces.poc.common.*;
import com.j_spaces.core.LeaseContext;
import com.j_spaces.core.client.SQLQuery;
import org.openspaces.core.GigaSpace;
import org.openspaces.core.context.GigaSpaceContext;
import org.openspaces.events.EventDriven;
import org.openspaces.events.EventTemplate;
import org.openspaces.events.TransactionalEvent;
import org.openspaces.events.adapter.SpaceDataEvent;
import org.openspaces.events.polling.Polling;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Iterator;
import java.util.logging.Logger;

/**
 * Process MoneyTransferJourney in an {@link JourneyLifecycle#AUTHORIZED} state
 */
@EventDriven
@Polling
@TransactionalEvent
public class MoneyTransferJourneyProcessor {

    private static Logger logger = Logger.getLogger(MoneyTransferJourneyProcessor.class.getName());

    @Autowired
    private GigaSpace gigaSpace;

    @EventTemplate
    public SQLQuery<MoneyTransferJourney> eventTemplate() {
        Debug.info("MoneyTransferJourneyProcessor.eventTemplate()");

        SQLQuery<MoneyTransferJourney> template = new SQLQuery<>(MoneyTransferJourney.class, "journeyLifecycle=?", JourneyLifecycle.AUTHORIZED);
        return template;
    }

    /**
     * @param journey authorized journey to be scheduled
     * @return an authorized journey
     */
    @SpaceDataEvent
    public MoneyTransferJourney schedule(MoneyTransferJourney journey) {
        Debug.info("MoneyTransferJourneyProcessor.schedule()");

        Instruction nextInstruction = journey.getNextInstruction();
        nextInstruction.setJourneyId(journey.getId());
        nextInstruction.setInstructionLifecycle(InstructionLifecycle.CREATED);
        gigaSpace.write(nextInstruction);
        logger.info("entry/createInstruction() - " + nextInstruction);

        journey.setJourneyLifecycle(JourneyLifecycle.SCHEDULED);
        logger.info("journey/lifecycle - SCHEDULED - " + journey);
        return journey;
    }

}
