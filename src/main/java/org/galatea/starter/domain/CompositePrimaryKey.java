package org.galatea.starter.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import lombok.Data;
import net.bytebuddy.asm.Advice.Local;

@Data
public class CompositePrimaryKey implements Serializable {

  private String symbol;
  private LocalDate date;
  private LocalTime minute;

  public CompositePrimaryKey() {

  }

  public CompositePrimaryKey(String symbol, LocalDate date, LocalTime minute) {
    this.symbol = symbol;
    this.date = date;
    this.minute = minute;
  }

}
