package org.galatea.starter.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.galatea.starter.domain.HistoricalPriceDB;
import org.galatea.starter.domain.IexHistoricalPrice;
import org.galatea.starter.domain.IexLastTradedPrice;
import org.galatea.starter.domain.IexSymbol;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

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

      if (!service.exists(symbols + range)) {
        List<IexHistoricalPrice> prices = newClient.getHistoricalPricesWithRange(symbols, range);
        createHistoricalPriceDBObject(prices, service, symbols + range);
        return prices;
      } else {
        List<HistoricalPriceDB> prices = service.getPrices(symbols + range);
        return createReturnableObject(prices);
      }

    } else {
      if (!service.exists(symbols + date)) {
        List<IexHistoricalPrice> prices = newClient.getHistoricalPricesWithDate(symbols, date);
        createHistoricalPriceDBObject(prices, service, symbols + date);
        return prices;
      } else {
        List<HistoricalPriceDB> prices = service.getPrices(symbols + date);
        return createReturnableObject(prices);
      }
    }
  }

  public void createHistoricalPriceDBObject(List<IexHistoricalPrice> prices,
      HistoricalPriceDBService service, String id) {
    for (IexHistoricalPrice price : prices) {
      HistoricalPriceDB obj =
          new HistoricalPriceDB(id, price.getClose(), price.getHigh(),
              price.getLow(), price.getOpen(), price.getSymbol(), price.getVolume(),
              price.getDate());
      service.save(obj);
    }
  }

  public List<IexHistoricalPrice> createReturnableObject(List<HistoricalPriceDB> prices) {
    List<IexHistoricalPrice> pricesToReturn = new ArrayList<>();
    for (HistoricalPriceDB price : prices) {
      IexHistoricalPrice obj =
          IexHistoricalPrice.builder().close(price.getClose()).open(price.getOpen())
              .high(price.getHigh()).low(price.getLow()).symbol(price.getSymbol())
              .date(price.getDate()).volume(price.getVolume()).build();
      pricesToReturn.add(obj);
    }
    return pricesToReturn;
  }
}

