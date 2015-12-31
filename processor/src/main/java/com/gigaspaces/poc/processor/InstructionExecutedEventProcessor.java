package com.gigaspaces.poc.processor;

import com.gigaspaces.client.ChangeModifiers;
import com.gigaspaces.client.ChangeResult;
import com.gigaspaces.client.ChangeSet;
import com.gigaspaces.client.ChangedEntryDetails;
import com.gigaspaces.poc.common.*;
import com.gigaspaces.query.IdQuery;
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
import java.util.List;
import java.util.function.Predicate;
import java.util.logging.Logger;

/**
 * Process MoneyTransferJourney in an {@link JourneyLifecycle#AUTHORIZED} state
 */
@EventDriven
@Polling
@TransactionalEvent
public class InstructionExecutedEventProcessor {

    private static Logger logger = Logger.getLogger(InstructionExecutedEventProcessor.class.getName());

    @Autowired
    private GigaSpace gigaSpace;

    @EventTemplate
    public SQLQuery<InstructionExecutedEvent> eventTemplate() {
        Debug.info("InstructionExecutedEventProcessor.eventTemplate()");

        SQLQuery<InstructionExecutedEvent> template = new SQLQuery<>(InstructionExecutedEvent.class, "");
        return template;
    }

    /**
     * @param event executed event
     */
    @SpaceDataEvent
    public void executed(final InstructionExecutedEvent event) {
        Debug.info("InstructionExecutedEventProcessor.executed()");

        logger.info("received executed event: " + event);

        IdQuery idQuery = new IdQuery(Instruction.class, event.getInstructionId());
        gigaSpace.change(idQuery, new ChangeSet().set("instructionLifecycle", InstructionLifecycle.EXECUTED));
        logger.info("instruction/lifecycle - EXECUTED - instruction-Id: " + event.getInstructionId());

        logger.info("entry/fulfillJourney() - journey-Id: " + event.getJourneyId());
        InstructionFulfilledEvent fulfilledEvent = new InstructionFulfilledEvent();
        fulfilledEvent.setInstructionId(event.getInstructionId());
        fulfilledEvent.setJourneyId(event.getJourneyId());
        gigaSpace.write(fulfilledEvent);
        logger.info("trigger fulfilled event - " + fulfilledEvent);
    }
}
