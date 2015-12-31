package com.gigaspaces.poc.processor;

import com.gigaspaces.poc.common.Journey;
import com.gigaspaces.poc.common.JourneyService;
import org.openspaces.core.GigaSpace;
import org.openspaces.remoting.RemotingService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.logging.Logger;

/**
 * Created by moran on 12/30/15.
 */
@RemotingService
public class JourneyServiceImpl implements JourneyService {

    private static Logger logger = Logger.getLogger(JourneyServiceImpl.class.getName());

    @Autowired
    private GigaSpace gigaSpace;


    @Override
    public void createJourney(Journey journey) {
        logger.info("creating journey " + journey);
        gigaSpace.write(journey);
    }
}
