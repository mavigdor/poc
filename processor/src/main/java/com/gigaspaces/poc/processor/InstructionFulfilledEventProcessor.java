package com.gigaspaces.poc.processor;

import com.gigaspaces.poc.common.*;
import com.j_spaces.core.client.SQLQuery;
import org.openspaces.core.GigaSpace;
import org.openspaces.core.context.GigaSpaceContext;
import org.openspaces.events.EventDriven;
import org.openspaces.events.EventTemplate;
import org.openspaces.events.TransactionalEvent;
import org.openspaces.events.adapter.SpaceDataEvent;
import org.openspaces.events.polling.Polling;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.logging.Logger;

/**
 * Process MoneyTransferJourney in an {@link JourneyLifecycle#AUTHORIZED} state
 */
@EventDriven
@Polling
@TransactionalEvent
public class InstructionFulfilledEventProcessor {

    private static Logger logger = Logger.getLogger(InstructionFulfilledEventProcessor.class.getName());

    @Autowired
    private GigaSpace gigaSpace;

    @EventTemplate
    public SQLQuery<InstructionFulfilledEvent> eventTemplate() {
        Debug.info("InstructionFulfilledEventProcessor.eventTemplate()");

        SQLQuery<InstructionFulfilledEvent> template = new SQLQuery<>(InstructionFulfilledEvent.class, "");
        return template;
    }

    /**
     * @param event executed event
     */
    @SpaceDataEvent
    public void executed(final InstructionFulfilledEvent event) {
        Debug.info("InstructionFulfilledEventProcessor.executed()");

        logger.info("received fulfilled event: " + event);

        Journey journey = gigaSpace.readById(Journey.class, event.getJourneyId());
        Instruction nextInstruction = journey.getNextInstruction();
        if (nextInstruction == null) {
            journey.setJourneyLifecycle(JourneyLifecycle.FULFILLED);
            gigaSpace.write(journey);
            logger.info("journey/lifecycle - FULFILLED - " + journey);
        } else {
            gigaSpace.write(journey); //next instruction changed journey

            nextInstruction.setJourneyId(journey.getId());
            nextInstruction.setInstructionLifecycle(InstructionLifecycle.CREATED);
            gigaSpace.write(nextInstruction);
            logger.info("entry/createInstruction() - " + nextInstruction);
        }
    }
}
