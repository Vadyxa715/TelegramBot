package ru.vadyxa.MrTeaCircleBot.initbot;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ComponentScan;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.polls.SendPoll;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import ru.vadyxa.MrTeaCircleBot.configuration.ConfigurationBot;
import ru.vadyxa.MrTeaCircleBot.configuration.EnumConstantArgs;
import ru.vadyxa.MrTeaCircleBot.keyboard.Keyboard;

import java.util.List;

import static ru.vadyxa.MrTeaCircleBot.configuration.EnumConstantArgs.*;

@Slf4j
@ComponentScan
@AllArgsConstructor
public class Bot extends TelegramLongPollingBot {
    private static final InlineKeyboardMarkup BUTTONS = Keyboard.build(Keyboard.buildButtons());
    public static final String ALL_COMMAND = EnumConstantArgs.getAll();


    private final ConfigurationBot configurationBot;

    @Override
    public void onUpdateReceived(Update update) {
        var msg = update.getMessage();
        var user = msg.getFrom();
        var userId = user.getId();
        var chatId = msg.getChatId();

        log.info("User name: {} with userId: {} wrote message: \"{}\".",user.getFirstName(), userId, msg.getText());

        if(msg.isCommand()) {
            switch (containsAndReturnEnum(msg.getText())) {
                case MENU:
                    sendMenu(userId, "<b>MENU</b>", BUTTONS);
                    break;
                case SETTINGS:
                    sendMenu(userId, "<b>SETTINGS</b>", BUTTONS);
                    break;
                case HELP:
                    sendText(userId, "Список доступных команд: " + ALL_COMMAND);
                    break;
                case CREATE_ANSWER:
                    sendPollInChat(chatId);
                    break;
                case CHECK_PIDOR:
                    checkPidor(chatId, user.getUserName());
                    break;
                default:
                    sendText(userId, "Такой команды не существует. Помощь - " + HELP);
            }
        }
    }

    @Override
    public void clearWebhook() throws TelegramApiRequestException {

    }

    @Override
    public String getBotUsername() {
        return configurationBot.getBotName();
    }

    @Override
    public String getBotToken() {
        return configurationBot.getToken();
    }

    public void sendText(Long who, String what){
        SendMessage sm = SendMessage.builder()
                .chatId(who.toString()) //Who are we sending a message to
                .text(what).build();    //Message content
        try {
            execute(sm);                        //Actually sending the message
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);      //Any error will be printed here
        }
    }

    public void sendMenu(Long who, String txt, InlineKeyboardMarkup kb){
        SendMessage sm = SendMessage.builder().chatId(who.toString())
                .parseMode("HTML").text(txt)
                .replyMarkup(kb).build();

        try {
            execute(sm);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void checkPidor(Long who, String userName) {
        sendText(who, "Пидор дня:  обнаружен!");
        sendText(who, "И это!");
        sendText(who, "Тадам - " + userName + "!!! Ты пидор");
    }

    public void sendPollInChat(Long chatId) {

        SendPoll sendPoll = SendPoll.builder()
                .chatId(chatId)
                .question("Во сколько идем обедать?")
                .allowMultipleAnswers(true)
                .allowSendingWithoutReply(true)
                .isClosed(false)
                .build();
        var options = List.of("10:00",
                "10:15",
                "10:30");
        sendPoll.setOptions(options);

        try {
            execute(sendPoll);                        //Actually sending the message
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }


}
