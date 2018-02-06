package io.ark.core.util;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class Time {

  private static final Instant start = Instant.parse("2017-03-21T13:00:00.000Z");

  public static int getTime() {
    return Math.toIntExact(ChronoUnit.MILLIS.between(start, Instant.now()) / 1000);
  }

}
