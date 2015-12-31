package com.gigaspaces.poc.processor;

import com.gigaspaces.poc.common.Instruction;
import com.gigaspaces.poc.common.InstructionExecutedEvent;
import org.openspaces.core.GigaSpace;
import org.openspaces.core.context.GigaSpaceContext;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * Created by moran on 12/29/15.
 */
public class BackendMock implements InitializingBean, DisposableBean {
    Logger logger = Logger.getLogger(this.getClass().getName());

    private ScheduledExecutorService executorService;

    private long workDelay = 5000;

    @Autowired
    private GigaSpace gigaSpace;

    public void afterPropertiesSet() throws Exception {
        logger.info("--- STARTING BACKEND MOCK WITH [" + workDelay + " ms] WORK DELAY");
        executorService = Executors.newScheduledThreadPool(1);
    }

    public void destroy() throws Exception {
        executorService.shutdown();
    }

    public void submit(final Instruction instruction) {
        logger.info("submitted instruction - " + instruction);
        executorService.schedule(new Runnable() {
            @Override
            public void run() {
                logger.info("executed instruction - " + instruction);

                InstructionExecutedEvent executedEvent = new InstructionExecutedEvent();
                executedEvent.setInstructionId(instruction.getId());
                executedEvent.setJourneyId(instruction.getJourneyId());
                gigaSpace.write(executedEvent);
                logger.info("trigger executed Event - " + executedEvent);
            }
        }, workDelay, TimeUnit.MILLISECONDS);
    }
}
