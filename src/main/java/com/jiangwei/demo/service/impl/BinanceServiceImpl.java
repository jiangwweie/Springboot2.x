package com.jiangwei.demo.service.impl;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.market.TickerPrice;
import com.jiangwei.demo.config.BinanceConfig;
import com.jiangwei.demo.service.BinanceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

/**
 * @author jiangwei
 * @date 2022/1/13
 */
@Service
@Slf4j
public class BinanceServiceImpl implements BinanceService {


    private BinanceApiRestClient binanceApiRestClient;

    private BinanceConfig binanceConfig;

    public BinanceServiceImpl(BinanceConfig binanceConfig) {
        BinanceApiClientFactory factory = BinanceApiClientFactory.newInstance("", "");
        BinanceApiRestClient restClient = factory.newRestClient();
        this.binanceApiRestClient = restClient;
    }


    @Override
    public TickerPrice getTickerPrice(String symbol) {
        TickerPrice price = binanceApiRestClient.getPrice(symbol);
        return price;
    }
}
