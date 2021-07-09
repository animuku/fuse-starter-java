package org.galatea.starter.service;

import java.util.Optional;
import org.galatea.starter.domain.CompositePrimaryKey;
import org.galatea.starter.domain.HistoricalPriceDB;
import org.galatea.starter.domain.rpsy.HistoricalPriceDBRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HistoricalPriceDBService {

  @Autowired
  HistoricalPriceDBRepository repo;


  public Optional<HistoricalPriceDB> getPrices(CompositePrimaryKey obj) {
    return repo.findById(obj);
  }

  public void save(HistoricalPriceDB price) {
    repo.save(price);
  }

  public boolean exists(CompositePrimaryKey obj) {
    return repo.existsById(obj);
  }


}
