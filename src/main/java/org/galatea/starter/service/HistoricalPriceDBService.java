package org.galatea.starter.service;

import java.util.Optional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.galatea.starter.domain.CompositePrimaryKey;
import org.galatea.starter.domain.HistoricalPriceDB;
import org.galatea.starter.domain.rpsy.HistoricalPriceDBRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HistoricalPriceDBService {

  @NonNull
  private final HistoricalPriceDBRepository repo;


  public Optional<HistoricalPriceDB> getPrices(CompositePrimaryKey key) {
    return repo.findById(key);
  }

  public HistoricalPriceDB save(HistoricalPriceDB price) {
    repo.save(price);
    return price;
  }

  public boolean exists(CompositePrimaryKey key) {
    return repo.existsById(key);
  }


}
