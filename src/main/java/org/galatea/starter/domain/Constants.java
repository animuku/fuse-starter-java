package org.galatea.starter.domain;

public final class Constants {
  public static final String token = System.getenv("IEX_TOKEN");
  public static final String rangeURL = "/stock/{symbols}/chart/{range}?token="+token;
  public static final String dateeURL = "/stock/{symbols}/chart/date/{date}?token="+token;
}
