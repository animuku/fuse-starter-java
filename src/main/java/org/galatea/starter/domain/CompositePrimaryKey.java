package org.galatea.starter.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.Data;

@Data
public class CompositePrimaryKey implements Serializable {

  private String symbol;
  private LocalDate date;
  private LocalTime time;

  public CompositePrimaryKey() {

  }

  public CompositePrimaryKey(String symbol, LocalDate date, LocalTime time) {
    this.symbol = symbol;
    this.date = date;
    this.time = time;
  }

}
