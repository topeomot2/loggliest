package com.inrista.loggliest;

import java.io.File;

/**
 * Created by Temitope on 9/11/2015.
 */
public interface BulkLogger {

    /**
     * Actual Upload of Logs
     * Must Call Loggly.changeRecentLogFile() everytime after getting a response of success.
     * @param token  Loggly token
     * @param logglyTag The Loggly tag that the log messages are tagged with.
     * @param dir Directory Where Logs are Kept
     * @return
     */
    int log(String token, String logglyTag, File dir);


    void setLogglyUrl(String logglyUrl);
}
