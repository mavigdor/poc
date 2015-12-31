package com.gigaspaces.poc.processor;

import com.gigaspaces.client.ChangeSet;
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

import java.util.logging.Logger;

/**
 * Process MoneyTransferJourney in an {@link JourneyLifecycle#AUTHORIZED} state
 */
@EventDriven
@Polling
@TransactionalEvent
public class InstructionReleasedEventProcessor {

    private static Logger logger = Logger.getLogger(InstructionReleasedEventProcessor.class.getName());

    @Autowired
    private GigaSpace gigaSpace;

    @EventTemplate
    public SQLQuery<InstructionReleaseEvent> eventTemplate() {
        Debug.info("InstructionReleasedEventProcessor.eventTemplate()");

        SQLQuery<InstructionReleaseEvent> template = new SQLQuery<>(InstructionReleaseEvent.class, "");
        return template;
    }

    /**
     * @param event released event
     */
    @SpaceDataEvent
    public void released(InstructionReleaseEvent event) {
        Debug.info("InstructionReleasedEventProcessor.released()");

        logger.info("received released event: " + event);

        IdQuery idQuery = new IdQuery(Journey.class, event.getJourneyId());
        gigaSpace.change(idQuery, new ChangeSet().set("journeyLifecycle", JourneyLifecycle.RELEASED));
        logger.info("journey/lifecycle - RELEASED - " + event.getJourneyId());
    }

}
