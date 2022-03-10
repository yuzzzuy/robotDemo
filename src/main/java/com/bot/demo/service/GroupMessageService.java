package com.bot.demo.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

/**
 * @author WangChen
 * @date 2022/3/7 16:57
 * @description
 */
public interface GroupMessageService {

    /**
     * 进群事件
     * @param update update
     * @param user user
     * @return SendMessage
     */
    SendMessage joinGroupEvent(Update update, User user);

}
