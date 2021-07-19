package org.galatea.starter.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import org.galatea.starter.ASpringTest;
import org.galatea.starter.domain.CompositePrimaryKey;
import org.galatea.starter.domain.HistoricalPriceEntity;
import org.galatea.starter.domain.rpsy.HistoricalPriceEntityRepository;
import org.galatea.starter.testutils.TestDataGenerator;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

public class HistoricalPriceServiceTest extends ASpringTest {

  @MockBean
  private HistoricalPriceEntityRepository rpsy;

  private HistoricalPriceEntityService dbService;

  @Before
  public void setup() {
    dbService = new HistoricalPriceEntityService(rpsy);
  }

  @Test
  public void testFindPriceFound() {
    LocalDate idDate = LocalDate.now().withMonth(7).withDayOfMonth(7).withYear(2021);
    LocalTime idTime = LocalTime.now().withMinute(0).withHour(0).withSecond(0).withNano(0);
    String idSymbol = "AAPL";
    CompositePrimaryKey key = new CompositePrimaryKey(idSymbol, idDate, idTime);
    HistoricalPriceEntity prices = TestDataGenerator.defaultHistoricalPriceData();
    given(this.rpsy.findById(key))
        .willReturn(Optional.of(prices));
    Optional<HistoricalPriceEntity> maybeRetrieved = dbService.getPrices(key);
    assertTrue(maybeRetrieved.isPresent());
  }

  @Test
  public void testFindPriceNotFound() {
    LocalDate idDate = LocalDate.now().withMonth(7).withDayOfMonth(7).withYear(2021);
    LocalTime idTime = LocalTime.now().withMinute(0).withHour(0).withSecond(0).withNano(0);
    String idSymbol = "AAPL";
    CompositePrimaryKey key = new CompositePrimaryKey(idSymbol, idDate, idTime);
    HistoricalPriceEntity prices = TestDataGenerator.defaultHistoricalPriceData();
    given(this.rpsy.findById(key))
        .willReturn(Optional.of(prices));
    Optional<HistoricalPriceEntity> maybeRetrieved = dbService
        .getPrices(new CompositePrimaryKey("FB", LocalDate.now().withMonth(7).withDayOfMonth(9),
            LocalTime.now().withMinute(0).withHour(0).withSecond(0).withNano(0)));
    assertFalse(maybeRetrieved.isPresent());
  }

  @Test
  public void testPriceIsSaved() {
    HistoricalPriceEntity prices =
        new HistoricalPriceEntity(BigDecimal.valueOf(1001)
            , BigDecimal.valueOf(1001)
            , BigDecimal.valueOf(1001)
            , BigDecimal.valueOf(1001)
            , "FB"
            , 500
            , LocalDate.now().withYear(2021).withMonth(7).withDayOfMonth(7)
            , LocalTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0)
            , LocalTime.now());
    given(rpsy.save(prices)).willReturn(prices);
    HistoricalPriceEntity returnedPrice = dbService.save(prices);
    assertNotNull(returnedPrice);
  }

  @Test
  public void testPriceExists() {
    given(rpsy.existsById(
        new CompositePrimaryKey("FB", LocalDate.now().withMonth(7).withDayOfMonth(9),
            LocalTime.now().withMinute(0).withHour(0).withSecond(0).withNano(0)))).
        willReturn(true);
    HistoricalPriceEntityService service = new HistoricalPriceEntityService(rpsy);
    boolean exists =
        service.exists(new CompositePrimaryKey("FB", LocalDate.now().withMonth(7).withDayOfMonth(9),
            LocalTime.now().withMinute(0).withHour(0).withSecond(0).withNano(0)));
    assertTrue(exists);
  }
}
