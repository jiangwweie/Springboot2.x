package com.jiangwei.demo.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author jiangwei
 * @date 2022/1/13
 */

@Slf4j
@ConfigurationProperties(prefix = "demo.binance")
@Data
@Component
public class BinanceConfig {


    private String apiKey;

    private String secretKey;

}
