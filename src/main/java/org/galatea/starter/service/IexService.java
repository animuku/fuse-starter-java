package org.galatea.starter.service;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.galatea.starter.domain.CompositePrimaryKey;
import org.galatea.starter.domain.HistoricalPriceEntity;
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
  private HistoricalPriceEntityService service;


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
      checkAndFetchForRange(service, symbols, range);
      return retrieveFromDBForRange(service, symbols, range);
    } else {
      checkAndFetchForDate(service, symbols, date);
      return retrieveFromDBForDate(service, symbols, date);
    }
  }

  public void checkAndFetchForRange(HistoricalPriceEntityService service, String symbol,
      String rangeOfDays) {
    int range = calculateRange(rangeOfDays);
    DateTimeFormatter formatters = DateTimeFormatter.ofPattern("yyyyMMdd");
    LocalDate today = LocalDate.now();
    for (int i = range; i >= 1; i--) {
      LocalDate date = today.minusDays(i);
      CompositePrimaryKey obj = new CompositePrimaryKey(symbol, date,
          LocalTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0));
      if (!service.exists(obj)) {
        List<IexHistoricalPrice> prices =
            newClient.getHistoricalPriceWithDateByDay(symbol, date.format(formatters));
        for (IexHistoricalPrice price : prices) {
          LocalTime t = LocalTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
          HistoricalPriceEntity objToSave =
              new HistoricalPriceEntity(price.getClose(), price.getHigh(), price.getLow(),
                  price.getOpen(), price.getSymbol(), price.getVolume(), price.getDate(), t,
                  LocalTime.now());
          service.save(objToSave);
        }
      }
    }
  }

  public List<IexHistoricalPrice> retrieveFromDBForRange(HistoricalPriceEntityService service,
      String symbol,
      String rangeOfDays) {
    List<IexHistoricalPrice> returnList = new ArrayList<>();
    int range = calculateRange(rangeOfDays);
    LocalDate today = LocalDate.now();
    for (int i = 1; i <= range; i++) {
      LocalDate date = today.minusDays(i);
      CompositePrimaryKey obj = new CompositePrimaryKey(symbol, date,
          LocalTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0));
      Optional<HistoricalPriceEntity> p = service.getPrices(obj);
      if (p.isPresent()) {
        HistoricalPriceEntity price = p.get();
        IexHistoricalPrice priceToReturn =
            IexHistoricalPrice.builder().close(price.getClose()).open(price.getOpen())
                .high(price.getHigh()).low(price.getLow()).symbol(price.getSymbol())
                .date(price.getDate()).volume(price.getVolume()).minute(price.getMinute()).build();
        returnList.add(priceToReturn);
      }
    }
    return returnList;
  }

  public void checkAndFetchForDate(HistoricalPriceEntityService service, String symbols,
      String date) {
    int[] parsedDate = parseDateString(date);
    int year = parsedDate[0];
    int month = parsedDate[1];
    int day = parsedDate[2];
    LocalDate d = LocalDate.now().withYear(year).withMonth(month).withDayOfMonth(day);
    LocalTime start = LocalTime.now().withHour(9).withMinute(30).withSecond(0).withNano(0);
    LocalTime end = LocalTime.now().withHour(16).withMinute(0).withSecond(0).withNano(0);
    for (LocalTime time = start; time.isBefore(end); time = time.plusMinutes(1)) {
      CompositePrimaryKey key = new CompositePrimaryKey(symbols, d, time);
      if (!service.exists(key)) {
        List<IexHistoricalPrice> returnList = newClient.getHistoricalPricesWithDate(symbols, date);
        for (IexHistoricalPrice price : returnList) {
          HistoricalPriceEntity obj =
              new HistoricalPriceEntity(price.getClose(), price.getHigh(), price.getLow(),
                  price.getOpen(), symbols, price.getVolume(), price.getDate(),
                  price.getMinute(), LocalTime.now());
          service.save(obj);
        }
        return;
      }
    }
  }

  public List<IexHistoricalPrice> retrieveFromDBForDate(HistoricalPriceEntityService service,
      String symbols, String date) {
    List<IexHistoricalPrice> list = new ArrayList<>();
    int[] parsedDate = parseDateString(date);
    int year = parsedDate[0];
    int month = parsedDate[1];
    int day = parsedDate[2];
    LocalDate d = LocalDate.now().withYear(year).withMonth(month).withDayOfMonth(day);
    LocalTime start = LocalTime.now().withHour(9).withMinute(30).withSecond(0).withNano(0);
    LocalTime end = LocalTime.now().withHour(16).withMinute(0).withSecond(0).withNano(0);
    for (LocalTime time = start; time.isBefore(end); time = time.plusMinutes(1)) {
      CompositePrimaryKey key = new CompositePrimaryKey(symbols, d, time);
      Optional<HistoricalPriceEntity> temp = service.getPrices(key);
      if (temp.isPresent()) {
        HistoricalPriceEntity obj = temp.get();
        IexHistoricalPrice price =
            IexHistoricalPrice.builder().close(obj.getClose()).open(obj.getOpen())
                .high(obj.getHigh()).low(obj.getLow()).symbol(obj.getSymbol())
                .date(obj.getDate()).volume(obj.getVolume()).minute(obj.getMinute()).build();
        list.add(price);
      }
    }
    return list;
  }

  public int calculateRange(String rangeOfDays) {
    if (!rangeOfDays.isEmpty()) {
      String r = rangeOfDays.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)")[0];
      return Integer.parseInt(r);
    }
    return 30;
  }

  public int[] parseDateString(String date) {
    int[] result = new int[3];
    DateTimeFormatter formatters = DateTimeFormatter.ofPattern("yyyyMMdd");
    LocalDate d = LocalDate.parse(date, formatters);
    result[0] = d.getYear();
    result[1] = d.getMonthValue();
    result[2] = d.getDayOfMonth();
    return result;
  }
}

