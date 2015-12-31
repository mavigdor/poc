package com.gigaspaces.poc.common;

import java.util.logging.Logger;

/**
 * Created by moran on 12/29/15.
 */
public class Debug {
    private static boolean debugOn = false;
    private static Logger logger = Logger.getLogger("info");
    public static void info(String msg) {
        if (debugOn) {
            logger.info("---> " + msg);
        }
    }
}
