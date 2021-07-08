package org.galatea.starter.domain.rpsy;

import java.util.List;
import org.galatea.starter.domain.HistoricalPriceDB;
import org.springframework.data.repository.CrudRepository;

public interface HistoricalPriceDBRepository extends CrudRepository<HistoricalPriceDB,String> {

  List<HistoricalPriceDB> findHistoricalPriceDBByUrl(String url);

  boolean existsById(String url);
}
