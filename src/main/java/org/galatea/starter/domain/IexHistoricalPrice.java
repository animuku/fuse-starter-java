package org.galatea.starter.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IexHistoricalPrice {

  private BigDecimal close;
  private BigDecimal high;
  private BigDecimal low;
  private BigDecimal open;
  private LocalDate date;
  private LocalTime minute;
  private String symbol;
  private Integer volume;
}
