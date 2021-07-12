package org.galatea.starter.domain.rpsy;

import org.galatea.starter.domain.CompositePrimaryKey;
import org.galatea.starter.domain.HistoricalPriceDB;
import org.springframework.data.repository.CrudRepository;

public interface HistoricalPriceDBRepository extends CrudRepository<HistoricalPriceDB, CompositePrimaryKey> {
}
