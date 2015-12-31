package com.gigaspaces.poc.common;

import java.util.logging.Logger;

/**
 * Created by moran on 12/31/15.
 */
public class AuthorizationServiceImpl2 implements AuthorizationService {
    private static Logger logger = Logger.getLogger(AuthorizationServiceImpl2.class.getName());

    @Override
    public void authorize(Journey journey) throws Exception {
        logger.info("ver2. - authorized journey: " + journey);
    }
}
