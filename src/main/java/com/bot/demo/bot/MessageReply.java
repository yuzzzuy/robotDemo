package com.bot.demo.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.extern.slf4j.Slf4j;

/**
 * @author WangChen
 * @date 2022/3/1 11:20
 * @description
 */
@Slf4j
public class MessageReply {


    public static InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup(
            new InlineKeyboardButton("google").callbackData("google"),
            new InlineKeyboardButton("callback_data").callbackData("callback1"),
            new InlineKeyboardButton("callback!").callbackData("callback"));

    public static InlineKeyboardMarkup inlineKeyboard2 = new InlineKeyboardMarkup(
            new InlineKeyboardButton("google1111").callbackData("111"),
            new InlineKeyboardButton("callback_data1111111").callbackData("1111111"),
            new InlineKeyboardButton("callback!").callbackData("callback"));


    public static void main(String[] args) {
        TelegramBot bot = new TelegramBot("5208611051:AAGFF26Mom8YW4y0YcIiWZD-suTdwoLjN-M");


        bot.setUpdatesListener(updates -> {

            for (Update update : updates) {
                String receivedMessage = "";
                long chatId = 0L;
                Message message = null;
                if (update.message() != null) {
                    message = update.message();
                } else if (update.editedMessage() != null) {
                    message = update.editedMessage();
                }

                if (update.callbackQuery() != null) {
                    receivedMessage = update.callbackQuery().data();
                    if ("callback".equals(receivedMessage)) {
                        SendMessage sentMessage = new SendMessage(update.callbackQuery().message().chat(), "Please select your want!");
                        sentMessage.replyMarkup(inlineKeyboard2);
                        bot.execute(sentMessage);
                        continue;
                    }

                }


                if (update.message() != null) {
                    receivedMessage = update.message().text();
                    chatId = update.message().chat().id();
                    if ("/help".equals(receivedMessage) || "/achievements".equals(receivedMessage)) {
                        SendMessage sentMessage = new SendMessage(chatId, "Please select your want!");
                        sentMessage.replyToMessageId(update.message().messageId());
                        sentMessage.replyMarkup(inlineKeyboard);
                        bot.execute(sentMessage);
                        continue;
                    }


                    SendMessage textMessage = new SendMessage(chatId, "TEXT");
                    InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup(
                            new InlineKeyboardButton[]{
                                    new InlineKeyboardButton("google").callbackData("google"),
                                    new InlineKeyboardButton("callback_data").callbackData("callback"),
                                    new InlineKeyboardButton("Switch!").callbackData("switch")
                            });
                    textMessage.replyMarkup(inlineKeyboard);
                    bot.execute(textMessage);
                }


                //普通文本
//                SendMessage textMessage = new SendMessage(chatId, "TEXT");


                //发送消息后 的 强制回复 和 移除回复 (直接引用)
//                Keyboard forceReply = new ForceReply(); // or just new ForceReply();
//                Keyboard replyKeyboardRemove = new ReplyKeyboardRemove(); // new ReplyKeyboardRemove(isSelective)
//                textMessage.replyMarkup(forceReply);
//                textMessage.replyMarkup(replyKeyboardRemove);

                //键盘按钮
//                Keyboard keyboard = new ReplyKeyboardMarkup(
//                        new KeyboardButton[]{
//                                new KeyboardButton("text"),
//                                new KeyboardButton("contact").requestContact(true),
//                                new KeyboardButton("location").requestLocation(true)
//                        }
//                );
//                textMessage.replyMarkup(keyboard);


//                //内联回复
//                InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup(
//                        new InlineKeyboardButton[]{
//                                new InlineKeyboardButton("google").callbackData("google"),
//                                new InlineKeyboardButton("callback_data").callbackData("callback"),
//                                new InlineKeyboardButton("Switch!").callbackData("switch")
//                        });
//
//
//
//                textMessage.replyMarkup(inlineKeyboard);
//
//                bot.execute(textMessage);
//
//
//
//                InlineQueryResult r1 = new InlineQueryResultPhoto("id", "photoUrl", "thumbUrl");
//                InlineQueryResult r2 = new InlineQueryResultArticle("id", "title", "message text").thumbUrl("url");
//                InlineQueryResult r3 = new InlineQueryResultGif("id", "gifUrl", "thumbUrl");
//                InlineQueryResult r4 = new InlineQueryResultMpeg4Gif("id", "mpeg4Url", "thumbUrl");
//
//                InlineQueryResult r5 = new InlineQueryResultVideo(
//                        "id", "videoUrl", InlineQueryResultVideo.MIME_VIDEO_MP4, "message", "thumbUrl", "video title")
//                        .inputMessageContent(new InputLocationMessageContent(21.03f, 105.83f));
//
//                bot.execute(new AnswerInlineQuery("google", r1, r2, r3, r4, r5));

            }

            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });

    }
}
