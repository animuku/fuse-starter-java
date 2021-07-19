package org.galatea.starter.domain.rpsy;

import org.galatea.starter.domain.CompositePrimaryKey;
import org.galatea.starter.domain.HistoricalPriceEntity;
import org.springframework.data.repository.CrudRepository;

public interface HistoricalPriceEntityRepository extends CrudRepository<HistoricalPriceEntity, CompositePrimaryKey> {
}
