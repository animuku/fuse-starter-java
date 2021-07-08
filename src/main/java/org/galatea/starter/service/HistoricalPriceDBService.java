package org.galatea.starter.service;

import java.util.List;
import org.galatea.starter.domain.HistoricalPriceDB;
import org.galatea.starter.domain.rpsy.HistoricalPriceDBRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HistoricalPriceDBService {

  @Autowired
  HistoricalPriceDBRepository repo;


  public List<HistoricalPriceDB> getPrices(String url) {
    return repo.findHistoricalPriceDBByUrl(url);
  }

  public void save(HistoricalPriceDB price){
    repo.save(price);
  }

  public boolean exists(String url){
    return repo.existsByUrl(url);
  }


}
