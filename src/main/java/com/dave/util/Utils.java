package com.dave.util;

import java.time.Instant;

public class Utils {

    public static String dateTime() {
        return Instant.now().toString();
    }

}
