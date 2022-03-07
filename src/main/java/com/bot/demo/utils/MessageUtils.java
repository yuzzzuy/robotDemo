package com.bot.demo.utils;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ForceReplyKeyboard;

/**
 * @author WangChen
 * @date 2022/3/7 17:34
 * @description
 */
public class MessageUtils {
    public static ForceReplyKeyboard forceReply() {
        ForceReplyKeyboard replyKeyboard = new ForceReplyKeyboard();
        replyKeyboard.setForceReply(true);
        return replyKeyboard;
    }
}
