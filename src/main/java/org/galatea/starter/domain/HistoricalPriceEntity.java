package org.galatea.starter.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import lombok.Data;

@Data
@Entity
@IdClass(CompositePrimaryKey.class)
public class HistoricalPriceEntity {


  @Id
  private String symbol;
  @Id
  private LocalDate date;
  @Id
  private LocalTime minute;

  private Integer volume;
  private BigDecimal close,high,low,open;
  private LocalTime timestamp;

  public HistoricalPriceEntity() {
  }

  public HistoricalPriceEntity(BigDecimal close, BigDecimal high, BigDecimal low,
      BigDecimal open, String symbol, Integer volume, LocalDate date, LocalTime minute,
      LocalTime timestamp) {
    this.close = close;
    this.high = high;
    this.low = low;
    this.open = open;
    this.symbol = symbol;
    this.volume = volume;
    this.date = date;
    this.minute = minute;
    this.timestamp = timestamp;
  }
}
