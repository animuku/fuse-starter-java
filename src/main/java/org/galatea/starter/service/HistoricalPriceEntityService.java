package org.galatea.starter.service;

import java.util.Optional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.galatea.starter.domain.CompositePrimaryKey;
import org.galatea.starter.domain.HistoricalPriceEntity;
import org.galatea.starter.domain.rpsy.HistoricalPriceEntityRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HistoricalPriceEntityService {

  @NonNull
  private final HistoricalPriceEntityRepository repo;


  public Optional<HistoricalPriceEntity> getPrices(CompositePrimaryKey key) {
    return repo.findById(key);
  }

  public HistoricalPriceEntity save(HistoricalPriceEntity price) {
    repo.save(price);
    return price;
  }

  public boolean exists(CompositePrimaryKey key) {
    return repo.existsById(key);
  }


}
