package org.finasoft.metal.core.normalization;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class TimeUtility {
    private SimpleDateFormat utcDateFormat;

    public TimeUtility() {
        utcDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        utcDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    /**
     * Formats a string to the FIX standard representing timestamp in UTC timezone
     * @return
     */
    public String nowUTC() {
        return utcDateFormat.format( new Date());
    }
}
