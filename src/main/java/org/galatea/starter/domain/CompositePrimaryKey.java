package org.galatea.starter.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public class CompositePrimaryKey implements Serializable {
  private String symbol;
  private LocalDate date;

  public CompositePrimaryKey(){

  }
  public  CompositePrimaryKey(String symbol, LocalDate date){
    this.symbol = symbol;
    this.date = date;
  }
}
