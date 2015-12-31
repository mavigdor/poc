package com.gigaspaces.poc.common;

/**
 * Created by moran on 12/31/15.
 */
public class AuthorizationServiceBean {
    private AuthorizationService authorizationService = new AuthorizationServiceImpl1();

    public AuthorizationService getAuthorizationService() {
        return authorizationService;
    }

    public void setAuthorizationService(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }
}
