package org.galatea.starter.domain;

import java.io.Serializable;
import java.util.Date;

public class CompositePrimaryKey implements Serializable {
  private String symbol;
  private Date date;

  public CompositePrimaryKey(){

  }
  public  CompositePrimaryKey(String symbol, Date date){
    this.symbol = symbol;
    this.date = date;
  }
}
