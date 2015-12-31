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

import javax.annotation.Resource;
import java.util.logging.Logger;

/**
 * Process MoneyTransferJourney in an {@link JourneyLifecycle#AUTHORIZED} state
 */
@EventDriven
@Polling
@TransactionalEvent
public class MoneyTransferInstructionProcessor {

    private static Logger logger = Logger.getLogger(MoneyTransferInstructionProcessor.class.getName());

    @Autowired
    private GigaSpace gigaSpace;

    @Autowired
    private BackendMock backendMock;

    @EventTemplate
    public SQLQuery<MoneyTransferInstruction> eventTemplate() {
        Debug.info("MoneyTransferInstructionProcessor.eventTemplate()");

        SQLQuery<MoneyTransferInstruction> template = new SQLQuery<>(MoneyTransferInstruction.class, "instructionLifecycle=?", InstructionLifecycle.CREATED);
        return template;
    }

    /**
     * @param instruction created instruction to be submitted
     * @return a submitted instruction
     */
    @SpaceDataEvent
    public MoneyTransferInstruction submit(MoneyTransferInstruction instruction) {
        Debug.info("MoneyTransferInstructionProcessor.submit()");

        logger.info("entry/submit() - " + instruction);
        logger.info("sending instruction to backend ...");
        backendMock.submit(instruction);

        instruction.setInstructionLifecycle(InstructionLifecycle.SUBMITTED);
        logger.info("instruction/lifecycle - SUBMITTED - " + instruction);


        logger.info("entry/releaseJourney() - journey-Id: " + instruction.getJourneyId());
        InstructionReleaseEvent releaseEvent = new InstructionReleaseEvent();
        releaseEvent.setInstructionId(instruction.getId());
        releaseEvent.setJourneyId(instruction.getJourneyId());
        gigaSpace.write(releaseEvent);
        logger.info("trigger release event - " + releaseEvent);

        return instruction;
    }

}
