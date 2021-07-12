package org.galatea.starter.service;

import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.galatea.starter.domain.CompositePrimaryKey;
import org.galatea.starter.domain.HistoricalPriceDB;
import org.galatea.starter.domain.IexHistoricalPrice;
import org.galatea.starter.domain.IexLastTradedPrice;
import org.galatea.starter.domain.IexSymbol;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import java.time.LocalDate;

/**
 * A layer for transformation, aggregation, and business required when retrieving data from IEX.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class IexService {

  @NonNull
  private IexClient iexClient;
  @NonNull
  private HistoricalPriceClient newClient;

  @NonNull
  private HistoricalPriceDBService service;


  /**
   * Get all stock symbols from IEX.
   *
   * @return a list of all Stock Symbols from IEX.
   */
  public List<IexSymbol> getAllSymbols() {
    return iexClient.getAllSymbols();
  }

  /**
   * Get the last traded price for each Symbol that is passed in.
   *
   * @param symbols the list of symbols to get a last traded price for.
   * @return a list of last traded price objects for each Symbol that is passed in.
   */
  public List<IexLastTradedPrice> getLastTradedPriceForSymbols(final List<String> symbols) {
    if (CollectionUtils.isEmpty(symbols)) {
      return Collections.emptyList();
    } else {
      return iexClient.getLastTradedPriceForSymbols(symbols.toArray(new String[0]));
    }
  }

  /**
   * Get historical prices for a particular symbol.
   *
   * @param symbols the list of symbols to get a historical price for.
   * @return a list of historical price objects for each symbol that is passed.
   */
  public List<IexHistoricalPrice> getHistoricalPrices(final String symbols, final String range,
      final String date) {
    if (symbols.length() == 0) {
      return Collections.emptyList();
    } else if (date == null) {
      checkAndRetrieve(service, symbols, range);
      return retrieveFromDB(service, symbols, range);
    } else {
      return Collections.emptyList();
    }
  }

  public void checkAndRetrieve(HistoricalPriceDBService service, String symbol,
      String rangeOfDays) {
    String r = rangeOfDays.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)")[0];
    int range = Integer.parseInt(r);
    LocalDate today = LocalDate.now();
    for (int i = range; i >= 1; i--) {
      LocalDate date = today.minusDays(i);
      DateTimeFormatter formatters = DateTimeFormatter.ofPattern("yyyyMMdd");
      CompositePrimaryKey obj = new CompositePrimaryKey(symbol,
          Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()));
      if (!service.exists(obj)) {
        List<IexHistoricalPrice> prices =
            newClient.getHistoricalPriceWithDateByDay(symbol, date.format(formatters));
        for (IexHistoricalPrice price : prices) {
          Date d = price.getDate();
          Date modifiedDate = new Date(d.getYear(), d.getMonth(), d.getDate() + 1);
          HistoricalPriceDB objToSave =
              new HistoricalPriceDB(price.getClose(), price.getHigh(), price.getLow(),
                  price.getOpen(), price.getSymbol(), price.getVolume(), modifiedDate,
                  LocalTime.now());
          service.save(objToSave);
        }
      }
    }
  }

  List<IexHistoricalPrice> retrieveFromDB(HistoricalPriceDBService service, String symbol,
      String rangeOfDays) {
    List<IexHistoricalPrice> returnList = new ArrayList<>();
//    rangeOfDays.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
    String r = rangeOfDays.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)")[0];
    int range = Integer.parseInt(r);
    LocalDate today = LocalDate.now();
    for (int i = 1; i <= range; i++) {
      LocalDate date = today.minusDays(i);
      CompositePrimaryKey obj = new CompositePrimaryKey(symbol,
          Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()));
      Optional<HistoricalPriceDB> p = service.getPrices(obj);
      if (p.isPresent()) {
        HistoricalPriceDB price = p.get();
        IexHistoricalPrice priceToReturn =
            IexHistoricalPrice.builder().close(price.getClose()).open(price.getOpen())
                .high(price.getHigh()).low(price.getLow()).symbol(price.getSymbol())
                .date(price.getDate()).volume(price.getVolume()).build();
        returnList.add(priceToReturn);
      }
    }
    return returnList;
  }
}

