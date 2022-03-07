package com.bot.demo.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author WangChen
 * @date 2022/3/7 16:59
 * @description
 */
public interface ButtonMessageService {

    /**
     * CancelButton click event
     * @param update message
     * @return EditMessageText
     */
    EditMessageText cancelButton(Update update);

    /**
     * normal button
     * @param update update
     * @return EditMessageText
     */
    EditMessageText normalButton(Update update);

    /**
     * normal button
     * @param update update
     * @return SendMessage
     */
    SendMessage addButton(Update update);
}
