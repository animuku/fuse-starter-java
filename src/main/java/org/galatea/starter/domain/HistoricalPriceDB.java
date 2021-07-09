package org.galatea.starter.domain;

import io.swagger.models.auth.In;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.IdClass;

@Entity
@IdClass(CompositePrimaryKey.class)
public class HistoricalPriceDB {

  private BigDecimal close;
  private BigDecimal high;
  private BigDecimal low;
  private BigDecimal open;
  @Id
  private String symbol;
  private Integer volume;
  @Id
  private Date date;

  private LocalTime timestamp;

  public HistoricalPriceDB() {
  }

  public HistoricalPriceDB(BigDecimal close, BigDecimal high, BigDecimal low,
      BigDecimal open, String symbol, Integer volume, Date date, LocalTime timestamp) {
    this.close = close;
    this.high = high;
    this.low = low;
    this.open = open;
    this.symbol = symbol;
    this.volume = volume;
    this.date = date;
    this.timestamp = timestamp;
  }

  public BigDecimal getClose() {
    return close;
  }

  public void setClose(BigDecimal close) {
    this.close = close;
  }

  public BigDecimal getHigh() {
    return high;
  }

  public void setHigh(BigDecimal high) {
    this.high = high;
  }

  public BigDecimal getLow() {
    return low;
  }

  public void setLow(BigDecimal low) {
    this.low = low;
  }

  public BigDecimal getOpen() {
    return open;
  }

  public void setOpen(BigDecimal open) {
    this.open = open;
  }

  public String getSymbol() {
    return symbol;
  }

  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }

  public Integer getVolume() {
    return volume;
  }

  public void setVolume(Integer volume) {
    this.volume = volume;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public LocalTime getTime() {return timestamp;}

  public void setTime(LocalTime time) {this.timestamp = timestamp;}
}
