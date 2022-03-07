package com.bot.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.bots.DefaultBotOptions;

/**
 * @author WangChen
 * @date 2022/3/7 15:16
 * @description
 */
@Configuration
public class DefaultBotOptionsConfig {
    @Bean
    public DefaultBotOptions newDefaultBotOptions() {
        final DefaultBotOptions defaultBotOptions = new DefaultBotOptions();
        // XXX: set additional bot options such as Proxy here
        defaultBotOptions.setProxyHost("127.0.0.1");
        defaultBotOptions.setProxyPort(7890);
        // Select proxy type: [HTTP|SOCKS4|SOCKS5] (default: NO_PROXY)
        defaultBotOptions.setProxyType(DefaultBotOptions.ProxyType.SOCKS5);

        return defaultBotOptions;
    }
}
