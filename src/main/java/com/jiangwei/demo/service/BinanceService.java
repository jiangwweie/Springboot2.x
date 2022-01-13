package com.jiangwei.demo.service;

import com.binance.api.client.domain.market.TickerPrice;

/**
 * @author jiangwei
 * @date 2022/1/13
 */
public interface BinanceService {

  TickerPrice getTickerPrice(String symbol);
}
