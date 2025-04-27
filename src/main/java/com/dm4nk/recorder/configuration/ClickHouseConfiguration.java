package com.dm4nk.recorder.configuration;

import com.clickhouse.client.ClickHouseCredentials;
import com.clickhouse.client.ClickHouseNode;
import com.clickhouse.client.ClickHouseProtocol;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClickHouseConfiguration {

    @Bean
    public ClickHouseNode server() {
        return ClickHouseNode.builder()
                .host(System.getProperty("chHost", "localhost"))
                .port(ClickHouseProtocol.HTTP, Integer.getInteger("chPort", 8123))
                .database("default")
                .credentials(ClickHouseCredentials.fromUserAndPassword(
                        System.getProperty("chUser", "default"),
                        System.getProperty("chPassword", "")))
                .build();
    }
}
