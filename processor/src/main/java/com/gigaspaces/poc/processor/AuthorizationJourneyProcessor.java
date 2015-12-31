package com.gigaspaces.poc.processor;

import com.gigaspaces.poc.common.AuthorizationServiceBean;
import com.gigaspaces.poc.common.Debug;
import com.gigaspaces.poc.common.Journey;
import com.gigaspaces.poc.common.JourneyLifecycle;
import com.j_spaces.core.client.SQLQuery;
import org.openspaces.events.EventDriven;
import org.openspaces.events.EventTemplate;
import org.openspaces.events.TransactionalEvent;
import org.openspaces.events.adapter.SpaceDataEvent;
import org.openspaces.events.polling.Polling;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.logging.Logger;

/**
 * Process Journey in a {@link JourneyLifecycle#CREATED} state, needed to be authorized.
 */
@EventDriven
@Polling
@TransactionalEvent
public class AuthorizationJourneyProcessor {

    private static Logger logger = Logger.getLogger(AuthorizationJourneyProcessor.class.getName());

    @Autowired
    private AuthorizationServiceBean authorizationServiceBean;

    @EventTemplate
    public SQLQuery<Journey> eventTemplate() {
        Debug.info("AuthorizationJourneyProcessor.eventTemplate()");

        SQLQuery<Journey> template = new SQLQuery<>(Journey.class, "journeyLifecycle=?", JourneyLifecycle.CREATED);
        return template;
    }

    /**
     * @param journey created journey to be authorized
     * @return an authorized journey
     */
    @SpaceDataEvent
    public Journey authorize(Journey journey) {
        Debug.info("AuthorizationJourneyProcessor.authorize()");

        logger.info("entry/verifyAuthorization() - " + journey);

        logger.info("toBeAuthorized()");
        try {
            authorizationServiceBean.getAuthorizationService().authorize(journey);
            journey.setJourneyLifecycle(JourneyLifecycle.AUTHORIZED);
            logger.info("journey/lifecycle - AUTHORIZED - " + journey);
        } catch (Exception e) {
            logger.info("Failed to authorize: " + e);
            journey.setJourneyLifecycle(null); //TODO handle authorization failures
        }
        return journey;
    }

}
