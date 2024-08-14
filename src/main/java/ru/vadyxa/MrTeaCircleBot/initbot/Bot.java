package ru.vadyxa.MrTeaCircleBot.initbot;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ComponentScan;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.polls.SendPoll;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButtonRequestChat;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import ru.vadyxa.MrTeaCircleBot.configuration.ConfigurationBot;
import ru.vadyxa.MrTeaCircleBot.configuration.EnumCommandConstantArgs;
import ru.vadyxa.MrTeaCircleBot.keyboard.Keyboard;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static ru.vadyxa.MrTeaCircleBot.configuration.EnumCommandConstantArgs.HELP;
import static ru.vadyxa.MrTeaCircleBot.configuration.EnumCommandConstantArgs.containsAndReturnEnum;

@Slf4j
@ComponentScan
@AllArgsConstructor
public class Bot extends TelegramLongPollingBot {
    private static final InlineKeyboardMarkup BUTTONS = Keyboard.build(Keyboard.buildButtons());
    private static final InlineKeyboardMarkup LUNCH_BUTTON = Keyboard.build(Keyboard.buildLunchButtons());
    public static final String ALL_COMMAND = EnumCommandConstantArgs.getAll();


    private final ConfigurationBot configurationBot;

    @Override
    public void onUpdateReceived(Update update) {
        var message = update.getMessage();

        if(Objects.nonNull(message) && message.isCommand()) {
            var user = message.getFrom();
            var userId = user.getId();
            var chatId = message.getChatId();
            switch (containsAndReturnEnum(message.getText())) {
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
                case LUNCH:
                    sendMenu(chatId,"Создать обеденный перерыв", LUNCH_BUTTON);
                    break;
                default:
                    sendText(userId, "Такой команды не существует. Помощь - " + HELP);
            }
        }

        if(update.hasCallbackQuery()) {
            var user = update.getCallbackQuery().getFrom();
            var userId = user.getId();
            var chatId = update.getCallbackQuery().getMessage().getChatId();
            switch (update.getCallbackQuery().getData()) {
                case Keyboard.CALL_BACK_LUNCH:
                    sendTimeInput(userId);
                    break;
                case Keyboard.CALL_BACK_CHECK_PIDOR:
                    checkPidor(chatId, user.getUserName());
                    break;
                default:
                    break;
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
        var sendMessage = SendMessage.builder()
                .chatId(who.toString())
                .parseMode("HTML")
                .text(txt)
                .replyMarkup(kb)
                .build();
        try {
            execute(sendMessage);
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

    private void sendTimeInput(Long userId) {
        var markup = ReplyKeyboardMarkup
                .builder()
                .keyboardRow(new KeyboardRow(Arrays.asList(new KeyboardButton("U+2B06"), new KeyboardButton(":arrow_down:"))))
                .keyboardRow(new KeyboardRow(Arrays.asList(new KeyboardButton(""), new KeyboardButton(":arrow_down:"))))
                .selective(true)
                .build();

        sendReplyMenu(userId, "Введите время в формате HH:MM:", markup);
    }

    private void sendReplyMenu(Long userId, String text, ReplyKeyboardMarkup replyKeyboardMarkup) {
        var sendMessage = SendMessage.builder()
                .chatId(userId.toString())
                .parseMode("HTML")
                .text(text)
                .replyMarkup(replyKeyboardMarkup)
                .build();
        try {
            execute(sendMessage);                        //Actually sending the message
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

// Добавьте здесь методы для обработки ввода времени и отправки ответа


    /**
     * Создать рекурсивный метод который будет отрисовывать кнопку
     * и при нажатии на увеличение/уменьшение значения стрелочкой
     * будет вызывать себя снова с новыми значениями на циферблате
     */



}
