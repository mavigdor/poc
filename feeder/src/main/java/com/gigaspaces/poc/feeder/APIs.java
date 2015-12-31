package com.gigaspaces.poc.feeder;

import com.gigaspaces.async.AsyncFuture;
import com.gigaspaces.poc.common.*;
import com.j_spaces.core.client.SQLQuery;
import org.openspaces.core.GigaSpace;
import org.openspaces.core.context.GigaSpaceContext;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

/**
 * A feeder bean starts a scheduled task that writes a new Journey objects to the space
 * (in an CREATED state).
 *
 * <p>The space is injected into this bean using OpenSpaces support for @GigaSpaceContext
 * annotation.
 *
 * <p>The scheduling uses the java.util.concurrent Scheduled Executor Service. It
 * is started and stopped based on Spring lifecycle events.
 */
public class APIs implements InitializingBean, DisposableBean {

    Logger logger = Logger.getLogger(this.getClass().getName());
    
    @GigaSpaceContext
    private GigaSpace gigaSpace;

    public void afterPropertiesSet() throws Exception {
        createMoneyTransfer();
        getMoneyTransfers();

        Thread.sleep(10000);
        updateAuthorizationService();
        createMoneyTransfer();
    }

    private void updateAuthorizationService() {
        logger.info("--- updateAuthorizationService");
        AsyncFuture<Object> asyncFuture = gigaSpace.execute(new UpdateAuthorizationServiceTask(new AuthorizationServiceImpl2()));
        try {
            asyncFuture.get();
        } catch (Exception e) {
            logger.warning("failed to update service: " + e);
        }
    }

    private void createMoneyTransfer() {
        logger.info("--- CreateMoneyTransfer:");
        MoneyTransferJourney journey = new MoneyTransferJourney();
        journey.setId(UUID.randomUUID().toString());
        journey.setJourneyLifecycle(JourneyLifecycle.CREATED);

        logger.info("\t---> create journey: " + journey);
        gigaSpace.write(journey);
    }

    private void getMoneyTransfers() {
        logger.info("--- GetMoneyTransfers:");
        MoneyTransferJourney[] moneyTransferJourneys = gigaSpace.readMultiple(new SQLQuery<MoneyTransferJourney>(MoneyTransferJourney.class, "journeyLifecycle=? or journeyLifecycle=?",
                JourneyLifecycle.CREATED, JourneyLifecycle.AUTHORIZED));
        for (MoneyTransferJourney moneyTransferJourney : moneyTransferJourneys) {
            logger.info("\t---> " + moneyTransferJourney);
        }
    }

    private void getMoneyTransferDetails(Journey journey) {
        logger.info("GetMoneyTransfersDetails:");
        InstructionEvent[] instructionEvents = gigaSpace.readMultiple(new SQLQuery<InstructionEvent>(InstructionEvent.class, "journeyId=?", journey.getId()));
        for (InstructionEvent instructionEvent : instructionEvents) {
            logger.info("\t---> " + instructionEvent);
        }
    }

    public void destroy() throws Exception {
    }

}
