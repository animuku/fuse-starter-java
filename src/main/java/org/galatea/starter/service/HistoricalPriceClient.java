package org.galatea.starter.service;

import java.util.List;
import org.galatea.starter.domain.IexHistoricalPrice;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "IEXHistoricalClient", url = "${spring.rest.iexNewBasePath}")
public interface HistoricalPriceClient {

  /**
   * Get historical price for each stock symbol passed in.
   *
   * @param symbols stock symbols to get historical price for.
   * @return a list of historical price for each symbol.
   */

  @GetMapping("/stock/{symbols}/chart/{range}?token=${iexToken}")
  List<IexHistoricalPrice> getHistoricalPricesWithRange(@PathVariable("symbols") String symbols,
      @PathVariable("range") String range);

  /**
   * Get historical price for each stock symbol passed in.
   *
   * @param symbols stock symbols to get historical price for.
   * @return a list of historical price for each symbol.
   */
  @GetMapping("/stock/{symbols}/chart/date/{date}?token=${iexToken}")
  List<IexHistoricalPrice> getHistoricalPricesWithDate(@PathVariable("symbols") String symbols,
      @PathVariable("date") String date);

}
