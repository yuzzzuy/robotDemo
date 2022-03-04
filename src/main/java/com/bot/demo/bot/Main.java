package com.bot.demo.bot;

import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

/**
 * @author WangChen
 * @date 2022/3/3 15:02
 * @description
 */
public class Main {
    public static void main(String[] args) {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            // Set up Http proxy
            DefaultBotOptions botOptions = new DefaultBotOptions();

            botOptions.setProxyHost("127.0.0.1");
            botOptions.setProxyPort(7890);
            // Select proxy type: [HTTP|SOCKS4|SOCKS5] (default: NO_PROXY)
            botOptions.setProxyType(DefaultBotOptions.ProxyType.SOCKS5);
            botsApi.registerBot(new MyAmazingBot("5154611046:AAFIRV8YTUn9wM43L3RaxVO34m4qP55y7kI","nnnnnbbbbbbot",botOptions));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}