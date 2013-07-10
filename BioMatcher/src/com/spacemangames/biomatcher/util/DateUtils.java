package com.spacemangames.biomatcher.util;

public class DateUtils {
    public static final long DAY_MILLIS = 24 * 60 * 60 * 1000;

    public static final long roundToFullDay(long in) {
        return (in / DAY_MILLIS) * DAY_MILLIS;
    }
}
