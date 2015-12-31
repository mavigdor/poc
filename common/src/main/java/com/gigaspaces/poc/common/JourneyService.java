package com.gigaspaces.poc.common;

import org.openspaces.remoting.Routing;

/**
 * Created by moran on 12/30/15.
 */
public interface JourneyService {

    /**
     * Creates a Journey
     * @param journey journey to create
     */
    void createJourney(@Routing("getId") Journey journey);
}
