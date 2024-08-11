package ru.vadyxa.MrTeaCircleBot.initbot;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ComponentScan;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.polls.SendPoll;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.polls.PollOption;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import ru.vadyxa.MrTeaCircleBot.configuration.ConfigurationBot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@ComponentScan
@AllArgsConstructor
public class Bot extends TelegramLongPollingBot {

    public static final String MENU = "/menu";
    public static final String SETTINGS = "/settings";
    public static final String HELP = "/help";
    public static final String CREATE_ANSWER = "/create_answer";
    public static final String ALL_COMMAND = "\n" + MENU + "\n" + SETTINGS + "\n" + HELP + "\n" + CREATE_ANSWER;


    private final ConfigurationBot configurationBot;

    @Override
    public void onUpdateReceived(Update update) {
        var msg = update.getMessage();
        var user = msg.getFrom();
        var userId = user.getId();
        var chatId = msg.getChatId();

        log.info("User name: {} with userId: {} wrote message: \"{}\".",user.getFirstName(), userId, msg.getText());

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
                case (CREATE_ANSWER):
                    sendPollInChat(userId, "Тест метода: 'sendPollInChat'", chatId);
                    break;
                default:
                    sendText(userId, "Такой команды не существует. Помощь - " + HELP);
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

    public void sendPollInChat(Long who, String what, Long chatId) {
        /**
         * Type of the entity. Currently, can be:
         * - “mention” (@username)
         * - “hashtag” (#hashtag)
         * - “cashtag” ($USD)
         * - “bot_command” (/start@jobs_bot)
         * - “url” (https://telegram.org)
         * - “email” (do-not-reply@telegram.org)
         * - “phone_number” (+1-212-555-0123),
         * - “bold” (bold text)
         * - “italic” (italic text)
         * - “underline” (underlined text)
         * - “strikethrough” (strikethrough text)
         * - “spoiler” (spoiler message)
         * - “blockquote” (block quotation)
         * - “code” (monowidth string)
         * - “pre” (monowidth block)
         * - “text_link” (for clickable text URLs)
         * - “text_mention” (for users without usernames)
         * - "custom_emoji" (for inline custom emoji stickers)
         */
        var messageEntity = MessageEntity.builder()
                .type("hashtag")
                .offset(10)
                .length(10)
                .text("Over text text text text text text")
                .build();
        var entities = new ArrayList<MessageEntity>();
        entities.add(messageEntity);
        entities.add(messageEntity);
        entities.add(messageEntity);

        var message = new Message();
        message.setEntities(entities);

        SendPoll sendPoll = SendPoll.builder()
                .chatId(chatId)
                .question("Стартанет али нет?")
                .explanationEntity(messageEntity)
                .option("Ну типо да")
                .option("Ну типо да")
                .allowMultipleAnswers(true)
                .allowSendingWithoutReply(true)
                .isClosed(false)
                .build();

        try {
            execute(sendPoll);                        //Actually sending the message
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }


}
