package com.bot.demo.bot;

import lombok.extern.slf4j.Slf4j;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.util.AbilityUtils;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatAdministrators;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.LoginUrl;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;
import org.telegram.telegrambots.meta.api.objects.games.CallbackGame;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ForceReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author WangChen
 * @date 2022/3/3 15:00
 * @description
 */
@Slf4j
public class MyAmazingBot extends AbilityBot {


    protected MyAmazingBot(String botToken, String botUsername, DefaultBotOptions botOptions) {
        super(botToken, botUsername, botOptions);
    }

    private static ForceReplyKeyboard forceReply() {
        ForceReplyKeyboard replyKeyboard = new ForceReplyKeyboard();
        replyKeyboard.setForceReply(true);
        return replyKeyboard;
    }

    @Override
    public long creatorId() {
        return 1494627839;
    }

    @Override
    public void onUpdateReceived(Update update) {

        //正常事件返回
        if (update.hasMessage() && update.getMessage().hasText()) {
            SendMessage message = new SendMessage();

            //进群时间
            if (update.getMessage().getNewChatMembers() != null) {
                List<User> user = update.getMessage().getNewChatMembers();
                log.warn("[进群事件]->User[{}]", user);
                return;
            }

            //退群事件
            if (update.getMessage().getLeftChatMember() != null) {
                User user = update.getMessage().getLeftChatMember();
                log.warn("[退群事件]->User[{}]", user);
                return;
            }


            //群组消息
            if (AbilityUtils.isSuperGroupUpdate(update) || AbilityUtils.isGroupUpdate(update)) {
                ArrayList<ChatMember> admins = getAdmin(update);
                boolean isAdmin = false;
                assert admins != null;
                for (ChatMember admin : admins) {
                    isAdmin = update.getMessage().getFrom().getUserName().equals(admin.getUser().getUserName());
                }


                //TODO 判断条件 用于区分私聊还是群聊
                //群聊
                message.setChatId(update.getMessage().getChatId().toString());
                //私聊
                //TODO 逻辑
                if (isAdmin) {
                    message.setChatId(String.valueOf(update.getMessage().getFrom().getId()));
                    message.setText("https://baidu.com");
                } else {
                    message.setText("if you want get some help,Please click @nnnnnbbbbbbot!");
                    message.setReplyToMessageId(update.getMessage().getMessageId());
                    message.setProtectContent(true);
                }


                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }

                return;

            }


            //commands
            if (update.getMessage().getText().equals("/achievements")) {

                message.setChatId(update.getMessage().getChatId().toString());
                message.setText("Please input your wallet address on chain!");
                message.setReplyToMessageId(update.getMessage().getMessageId());
                message.setProtectContent(true);
                message.setReplyMarkup(forceReply());
                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }

                return;
            }

            //回复事件返回
            if (update.getMessage().getReplyToMessage() != null) {
                String text = update.getMessage().getText();
                message.setChatId(String.valueOf(update.getMessage().getChatId()));
                //regex
                if ("1" .equals(text)) {
                    message.setText("Add another address");
                    InlineKeyboardMarkup inlineKeyboardMarkup = getAddModule("no");
                    message.setReplyMarkup(inlineKeyboardMarkup);
                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    return;
                } else {
                    message.setText("It's not a legal wallet address!");
                    InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
                    List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
                    List<InlineKeyboardButton> buttons = new ArrayList<>();
                    InlineKeyboardButton back = new InlineKeyboardButton("Cancel");
                    back.setCallbackData("Cancel");
                    buttons.add(back);
                    keyboard.add(buttons);
                    inlineKeyboardMarkup.setKeyboard(keyboard);
                    message.setReplyMarkup(inlineKeyboardMarkup);
                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    return;
                }
            }
        }

        //按钮事件返回
        if (update.hasCallbackQuery() && update.getCallbackQuery().getData() != null) {
            SendMessage message = new SendMessage();
            if ("Cancel" .equals(update.getCallbackQuery().getData())) {
                EditMessageText editMessageText = new EditMessageText();
                editMessageText.setChatId(update.getCallbackQuery().getMessage().getChatId().toString());
                editMessageText.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
                editMessageText.setText("end!");
                try {
                    execute(editMessageText);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
                return;
            } else if ("add" .equals(update.getCallbackQuery().getData())) {
                forReplyMessage(update, message);
                return;
            }
            forReplyMessage(update, message);

        }
    }


    private InlineKeyboardMarkup getAddModule(String kind) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> buttons = new ArrayList<>();
        InlineKeyboardButton add = addInlineKeyboard("Add", "add", InlineKeyboardButtonFiled.CALLBACK_DATA);
        buttons.add(add);
        if ("back" .equals(kind)) {
            InlineKeyboardButton back = addInlineKeyboard("Cancel", "Cancel", InlineKeyboardButtonFiled.CALLBACK_DATA);
            buttons.add(back);
        } else if ("no" .equals(kind)) {
            InlineKeyboardButton back = addInlineKeyboard("No more, show my report", "No more, show my report", InlineKeyboardButtonFiled.CALLBACK_DATA);
            buttons.add(back);
        }

        keyboard.add(buttons);
        inlineKeyboardMarkup.setKeyboard(keyboard);
        return inlineKeyboardMarkup;
    }

    private void forReplyMessage(Update update, SendMessage message) {
        message.setChatId(update.getCallbackQuery().getMessage().getChatId().toString());
        message.setText("Please input your wallet address on chain!");
        ForceReplyKeyboard replyKeyboard = forceReply();
        message.setReplyMarkup(replyKeyboard);
        try {
            // Call method to send the message
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    private InlineKeyboardButton addInlineKeyboard(String text, Object arg, InlineKeyboardButtonFiled filed) {
        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton(text);
        switch (filed) {
            case LOGIN_URL:
                inlineKeyboardButton.setLoginUrl((LoginUrl) arg);
                break;
            case PAY:
                inlineKeyboardButton.setPay((Boolean) arg);
                break;
            case CALLBACK_DATA:
                inlineKeyboardButton.setCallbackData((String) arg);
                break;
            case CALLBACK_GAME:
                inlineKeyboardButton.setCallbackGame((CallbackGame) arg);
                break;
            case SWITCH_INLINE_QUERY:
                inlineKeyboardButton.setSwitchInlineQuery((String) arg);
                break;
            case SWITCH_INLINE_QUERY_CURRENT_CHAT:
                inlineKeyboardButton.setSwitchInlineQueryCurrentChat((String) arg);
                break;
            default:

        }
        return inlineKeyboardButton;

    }

    private ArrayList<ChatMember> getAdmin(Update update) {
        GetChatAdministrators getChatAdministrators = new GetChatAdministrators();
        getChatAdministrators.setChatId(update.getMessage().getChatId().toString());
        try {
            ArrayList<ChatMember> administrators = execute(getChatAdministrators);
            return administrators;
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

        return null;
    }

    enum InlineKeyboardButtonFiled {
        LOGIN_URL,
        CALLBACK_DATA,
        SWITCH_INLINE_QUERY,
        SWITCH_INLINE_QUERY_CURRENT_CHAT,
        CALLBACK_GAME,
        PAY;

    }
}