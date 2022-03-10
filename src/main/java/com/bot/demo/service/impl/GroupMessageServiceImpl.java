package com.bot.demo.service.impl;

import com.bot.demo.service.GroupMessageService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

/**
 * @author WangChen
 * @date 2022/3/10 10:58
 * @description
 */
@Service
public class GroupMessageServiceImpl implements GroupMessageService {

    @Override
    public SendMessage joinGroupEvent(Update update, User user) {
        return new SendMessage(update.getMessage().getChatId().toString(),"Hello!"+user.getUserName());
    }
}
