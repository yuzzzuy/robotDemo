package com.bot.demo.service.impl;

import com.bot.demo.service.ReplyMessageService;
import com.bot.demo.utils.ButtonModuleUtils;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

/**
 * @author WangChen
 * @date 2022/3/7 17:30
 * @description
 */
@Service
public class ReplyMessageServiceImpl implements ReplyMessageService {

    @Override
    public SendMessage regexAchievements(String text, SendMessage message) {
        if ("1".equals(text)) {
            message.setText("Add another address");
            InlineKeyboardMarkup inlineKeyboardMarkup = ButtonModuleUtils.getAddModule("no");
            message.setReplyMarkup(inlineKeyboardMarkup);
        } else {
            message.setText("It's not a legal wallet address!");
            InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
            List<InlineKeyboardButton> buttons = new ArrayList<>();
            InlineKeyboardButton back = new InlineKeyboardButton("CANCEL");
            back.setCallbackData("CANCEL");
            buttons.add(back);
            keyboard.add(buttons);
            inlineKeyboardMarkup.setKeyboard(keyboard);
            message.setReplyMarkup(inlineKeyboardMarkup);
        }
        return message;
    }
}
