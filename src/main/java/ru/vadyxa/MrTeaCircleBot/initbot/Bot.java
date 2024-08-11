package ru.vadyxa.MrTeaCircleBot.initbot;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.ComponentScan;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import ru.vadyxa.MrTeaCircleBot.configuration.ConfigurationBot;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ComponentScan
@AllArgsConstructor
public class Bot extends TelegramLongPollingBot {

    public static final String MENU = "/menu";
    public static final String SETTINGS = "/settings";
    public static final String HELP = "/help";
    public static final String ALL_COMMAND = "\n" + MENU + "\n" + SETTINGS + "\n" + HELP;


    private final ConfigurationBot configurationBot;

    @Override
    public void onUpdateReceived(Update update) {
        var msg = update.getMessage();
        var user = msg.getFrom();
        var userId = user.getId();

        System.out.println(user.getFirstName() + " with userId=" + userId + " wrote " + msg.getText());

        var txt = msg.getText();

        var setButtons = buildButtons();
        var keyBord1 = InlineKeyboardMarkup.builder()
                .keyboardRow(List.of(setButtons.get("next"))).build();
        var keyBord2 = InlineKeyboardMarkup.builder()
                .keyboardRow(List.of(setButtons.get("back")))
                .keyboardRow(List.of(setButtons.get("url")))
                .build();

        if(msg.isCommand()) {
            switch (msg.getText()) {
                case (MENU):
                    sendMenu(userId, "<b>Menu 1</b>", keyBord1);
                    break;
                case (SETTINGS):
                    sendMenu(userId, "<b>Menu 1</b>", keyBord2);
                    break;
                case (HELP):
                    sendText(userId, "Список доступных команд: " + ALL_COMMAND);
                    break;
                default:
                    sendText(userId, "Такой команды не существует");
            }
        }
    }


    public Map<String, InlineKeyboardButton> buildButtons() {
        var next = InlineKeyboardButton.builder().text("Next").callbackData("next").build();
        var back = InlineKeyboardButton.builder().text("Back").callbackData("back").build();
        var url = InlineKeyboardButton.builder().text("Tutorial").url("https://core.telegram.org/bots/api").build();
        var buttonHashMap = new HashMap<String, InlineKeyboardButton>();
        buttonHashMap.put("next",next);
        buttonHashMap.put("back", back);
        buttonHashMap.put("url", url);
        return buttonHashMap;
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

    public void checkPidor() {
        sendText(configurationBot.getUserId(), "Пидор дня:  обнаружен!");
    }


}
