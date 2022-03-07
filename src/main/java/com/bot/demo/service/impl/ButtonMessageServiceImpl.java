package com.bot.demo.service.impl;

import com.bot.demo.service.ButtonMessageService;
import com.bot.demo.utils.MessageUtils;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ForceReplyKeyboard;

/**
 * @author WangChen
 * @date 2022/3/7 17:01
 * @description
 */
@Service
public class ButtonMessageServiceImpl implements ButtonMessageService {

    @Override
    public EditMessageText cancelButton(Update update) {
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(update.getCallbackQuery().getMessage().getChatId().toString());
        editMessageText.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
        editMessageText.setText("end!");
        return editMessageText;
    }

    @Override
    public EditMessageText normalButton(Update update) {
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(update.getCallbackQuery().getMessage().getChatId().toString());
        editMessageText.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
        editMessageText.setText("end!");
        return editMessageText;
    }

    @Override
    public SendMessage addButton(Update update) {
        SendMessage message = new SendMessage();
        message.setChatId(update.getCallbackQuery().getMessage().getChatId().toString());
        message.setText("Please input your wallet address on chain!");
        ForceReplyKeyboard replyKeyboard = MessageUtils.forceReply();
        message.setReplyMarkup(replyKeyboard);
        return message;
    }
}
