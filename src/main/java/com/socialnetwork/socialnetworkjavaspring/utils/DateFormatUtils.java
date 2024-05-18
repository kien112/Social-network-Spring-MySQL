package com.socialnetwork.socialnetworkjavaspring.utils;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DateFormatUtils {
    public static String getCurrentDateTimeInVn() {
        ZoneId hanoiTimeZone = ZoneId.of("Asia/Ho_Chi_Minh");
        ZonedDateTime hanoiDateTime = ZonedDateTime.now(hanoiTimeZone);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return hanoiDateTime.format(formatter);
    }
}
