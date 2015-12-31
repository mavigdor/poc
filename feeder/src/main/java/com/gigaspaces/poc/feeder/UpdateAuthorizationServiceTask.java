package com.gigaspaces.poc.feeder;

import com.gigaspaces.async.AsyncResult;
import com.gigaspaces.poc.common.AuthorizationService;
import com.gigaspaces.poc.common.AuthorizationServiceBean;
import org.openspaces.core.executor.AutowireTask;
import org.openspaces.core.executor.DistributedTask;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by moran on 12/31/15.
 */
@AutowireTask
public class UpdateAuthorizationServiceTask implements DistributedTask<Serializable,Object> {

    private static Logger logger = Logger.getLogger(UpdateAuthorizationServiceTask.class.getName());

    private AuthorizationService authorizationService;

    @Autowired
    private transient AuthorizationServiceBean authorizationServiceBean;

    public UpdateAuthorizationServiceTask(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }


    @Override
    public Serializable execute() throws Exception {
        logger.info("updating authorization service with: " + authorizationService.getClass().getName());
        authorizationServiceBean.setAuthorizationService(authorizationService);
        return null;
    }

    @Override
    public Object reduce(List<AsyncResult<Serializable>> list) throws Exception {
        for (AsyncResult<Serializable> result : list) {
            if (result.getException() != null) {
                throw result.getException();
            }
        }
        return null;
    }
}
