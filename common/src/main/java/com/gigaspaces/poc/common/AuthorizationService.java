package com.gigaspaces.poc.common;

import com.gigaspaces.poc.common.Journey;

import java.io.Serializable;

/**
 * Created by moran on 12/31/15.
 */
public interface AuthorizationService extends Serializable {

    void authorize(Journey journey) throws Exception;
}
