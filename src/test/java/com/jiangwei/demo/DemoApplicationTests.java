package com.jiangwei.demo;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.market.TickerPrice;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * springboot2.5.4依赖的Junit5，不在需要@RunWith了
 */
@SpringBootTest
class DemoApplicationTests {


    @Test
    public void binanceTest() {
        BinanceApiClientFactory factory = BinanceApiClientFactory.newInstance("", "");
        BinanceApiRestClient restClient = factory.newRestClient();
        TickerPrice price = restClient.getPrice("");
        System.out.println(price);
    }

}
