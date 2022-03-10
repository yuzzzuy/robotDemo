package com.bot.demo.bot;

import com.bot.demo.constants.Button;
import com.bot.demo.service.ButtonMessageService;
import com.bot.demo.service.GroupMessageService;
import com.bot.demo.service.ReplyMessageService;
import com.bot.demo.utils.MessageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.objects.Ability;
import org.telegram.abilitybots.api.objects.Locality;
import org.telegram.abilitybots.api.objects.Privacy;
import org.telegram.abilitybots.api.util.AbilityUtils;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.groupadministration.BanChatMember;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatAdministrators;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;


/**
 * @author WangChen
 * @date 2022/3/3 15:00
 * @description Bot
 */
@Slf4j
@Component
public class TelegramBot extends AbilityBot {
    @Value("${bot.creatorId}")
    private Long creatorId;

    @Resource
    private ButtonMessageService buttonService;
    @Resource
    private ReplyMessageService replyService;
    @Resource
    private GroupMessageService groupService;

    protected TelegramBot(@Value("${bot.token}") String botToken
            , @Value("${bot.name}") String botUsername
            , @NotNull ObjectProvider<DefaultBotOptions> defaultBotOptions) {
        super(botToken, botUsername, defaultBotOptions.getIfAvailable(DefaultBotOptions::new));
    }


    @Override
    public long creatorId() {
        return creatorId;
    }


    @Override
    public void onUpdateReceived(Update update) {
        //命令可以走此方法 只需要实现命令
        super.onUpdateReceived(update);
        //TODO 拆分方法

        //进群事件
        if (update.getMessage().getNewChatMembers() != null && update.getMessage().getNewChatMembers().size() > 0) {
            List<User> users = update.getMessage().getNewChatMembers();
            users.forEach(user -> {
                log.warn("[进群事件]->User[{}]", user);
                SendMessage sendMessage = groupService.joinGroupEvent(update, user);
                send(sendMessage);
            });
            return;
        }

        //退群事件
        if (update.getMessage().getLeftChatMember() != null) {
            User user = update.getMessage().getLeftChatMember();
            log.warn("[退群事件]->User[{}]", user);
            return;
        }

        //正常事件返回
        if (update.hasMessage() && update.getMessage().hasText()) {
            SendMessage message = new SendMessage();

            //群组消息
            if (AbilityUtils.isSuperGroupUpdate(update) || AbilityUtils.isGroupUpdate(update)) {
                log.warn("[群组消息]->update[{}]", update);
                ArrayList<ChatMember> admins = getAdmin(update);
                boolean isAdmin = false;
                assert admins != null;

                for (ChatMember admin : admins) {
                    isAdmin = update.getMessage().getFrom().getUserName().equals(admin.getUser().getUserName());
                }
                //TODO 逻辑用于区分私聊还是群聊
                //群聊
                message.setChatId(update.getMessage().getChatId().toString());
                //私聊
                //TODO 逻辑
                if (!isAdmin) {
                    message.setText("@" + update.getMessage().getFrom().getUserName() + ",抱歉,您没有管理员权限,禁止乱玩指令或按钮!");
                    send(message);
                    return;
                }
            }

            //回复事件返回
            if (update.getMessage().getReplyToMessage() != null) {
                String text = update.getMessage().getText();
                message.setChatId(String.valueOf(update.getMessage().getChatId()));
                //regex
                message = replyService.regexAchievements(text, message);
                send(message);
                return;
            }
        }


        //按钮事件返回
        if (update.hasCallbackQuery() && update.getCallbackQuery().getData() != null) {
            BotApiMethod<?> message;
            Button button = Button.valueOf(update.getCallbackQuery().getData());
            switch (button) {
                case CANCEL:
                    message = buttonService.cancelButton(update);
                    break;
                case ADD:
                    message = buttonService.addButton(update);
                    break;
                default:
                    message = buttonService.normalButton(update);
            }
            send(message);
        }
    }

    //commands start
    //person

    /**
     * find achievements
     * @return Ability
     */
    public Ability achievements() {
        return Ability
                .builder()
                .name("achievements")
                .info("says hello world!")
                .locality(Locality.USER)
                .privacy(Privacy.PUBLIC)
                .action(ctx -> {
                    SendMessage message = new SendMessage();
                    message.setChatId(ctx.update().getMessage().getChatId().toString());
                    message.setText("Please input your wallet address on chain!");
                    message.setReplyToMessageId(ctx.update().getMessage().getMessageId());
                    message.setReplyMarkup(MessageUtils.forceReply());
                    silent.execute(message);
                })
                .build();
    }

    //Groups

    /**
     * set groupRule
     * @return Ability
     */
    public Ability setJoinRule() {
        return Ability
                .builder()
                .name("setjoinrule")
                .info("set Group Rules!")
                .locality(Locality.GROUP)
                .privacy(Privacy.ADMIN)
                .action(ctx -> {
                    SendMessage message = new SendMessage();
                    message.setChatId(String.valueOf(ctx.update().getMessage().getFrom().getId()));
                    message.setText("https://baidu.com");
                    silent.execute(message);
                })
                .build();
    }


    // commands -- end

    private ArrayList<ChatMember> getAdmin(Update update) {
        GetChatAdministrators getChatAdministrators = new GetChatAdministrators();
        getChatAdministrators.setChatId(update.getMessage().getChatId().toString());
        try {
            return execute(getChatAdministrators);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        return null;
    }


    private void send(BotApiMethod<?> message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}