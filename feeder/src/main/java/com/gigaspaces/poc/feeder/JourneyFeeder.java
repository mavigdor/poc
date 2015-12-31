package com.gigaspaces.poc.feeder;

import com.gigaspaces.poc.common.*;
import org.openspaces.core.GigaSpace;
import org.openspaces.core.SpaceInterruptedException;
import org.openspaces.core.context.GigaSpaceContext;
import org.openspaces.remoting.ExecutorProxy;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
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
public class JourneyFeeder implements InitializingBean, DisposableBean {

    Logger log= Logger.getLogger(this.getClass().getName());
    
    private ScheduledExecutorService executorService;

    private ScheduledFuture<?> sf;

    private long initialDelay = 1000;
    private long defaultDelay = 1000;

    private JourneyTask journeyTask;

    @GigaSpaceContext
    private GigaSpace gigaSpace;

    @ExecutorProxy
    private JourneyService journeyService;

    public void setDefaultDelay(long defaultDelay) {
        this.defaultDelay = defaultDelay;
    }

    public void afterPropertiesSet() throws Exception {
        log.info("--- STARTING FEEDER WITH [" + defaultDelay + " ms] DELAY BETWEEN CYCLES");
        executorService = Executors.newScheduledThreadPool(1);
        journeyTask = new JourneyTask();
        sf = executorService.scheduleAtFixedRate(journeyTask, initialDelay, defaultDelay,
                TimeUnit.MILLISECONDS);
    }

    public void destroy() throws Exception {
        sf.cancel(false);
        sf = null;
        executorService.shutdown();
    }
    
    public class JourneyTask implements Runnable {
        public void run() {
            try {
                long time = System.currentTimeMillis();
                MoneyTransferJourney journey = new MoneyTransferJourney();
                journey.setId(UUID.randomUUID().toString());
                journey.setJourneyLifecycle(JourneyLifecycle.CREATED);
                log.info("---> FEEDER WRITE " + journey);
                gigaSpace.write(journey);
                log.info("<--- FEEDER WROTE " + journey);
            } catch (SpaceInterruptedException e) {
                // ignore, we are being shutdown
            } catch (Exception e) {
                log.warning("--- FEEDER EXCEPTION " + e);
            }
        }
    }

    
}
