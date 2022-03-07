package com.bot.demo.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

/**
 * @author WangChen
 * @date 2022/3/7 16:59
 * @description
 */
public interface ReplyMessageService {

    /**
     * check correct code
     *
     * @param text code
     * @param message message
     * @return message
     */
    SendMessage regexAchievements(String text, SendMessage message);
}
