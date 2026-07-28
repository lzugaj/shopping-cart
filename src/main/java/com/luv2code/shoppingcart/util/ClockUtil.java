package com.luv2code.shoppingcart.util;

import lombok.experimental.UtilityClass;

import java.time.Clock;
import java.time.Instant;

import static java.time.Clock.fixed;
import static java.time.Clock.systemDefaultZone;
import static java.time.ZoneOffset.UTC;

@UtilityClass
public final class ClockUtil {

    private static Clock systemClock = systemDefaultZone();

    public static void useFixedClockAt(Instant instant) {
        systemClock = fixed(instant, UTC);
    }

    public static void resetClock() {
        systemClock = systemDefaultZone();
    }

    public static Clock getClock() {
        return systemClock;
    }
}
