package ru.vadyxa.MrTeaCircleBot.keyboard;

import lombok.AllArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class Keyboard {

    public static final String CALL_BACK_CREATE_LUNCH = "create_lunch";
    public static final String CALL_BACK_CHECK_PIDOR = "check_pidor";
    public static final String CALL_BACK_LUNCH = "lunch";
    public static final String CREATE_LUNCH_TEXT = "Создать голосование: Время обеда";
    public static final String CHECK_PIDOR_TEXT = "Выбираем пидора дня";
    public static final String LUNCH_TEXT = "Создать обеденный перерыв";

    public static List<InlineKeyboardButton> buildButtons() {
        var keyboardButtonArrayList = new ArrayList<InlineKeyboardButton>();
        keyboardButtonArrayList.add(InlineKeyboardButton.builder().text(CREATE_LUNCH_TEXT).callbackData(CALL_BACK_CREATE_LUNCH).build());
        keyboardButtonArrayList.add(InlineKeyboardButton.builder().text(CHECK_PIDOR_TEXT).callbackData(CALL_BACK_CHECK_PIDOR).build());
        return keyboardButtonArrayList;
    }

    public static List<InlineKeyboardButton> buildLunchButtons() {
        var keyboardButtonArrayList = new ArrayList<InlineKeyboardButton>();
        keyboardButtonArrayList.add(InlineKeyboardButton.builder().text(LUNCH_TEXT).callbackData(CALL_BACK_LUNCH).build());
        return keyboardButtonArrayList;
    }

    public static InlineKeyboardMarkup build(List<InlineKeyboardButton> buttons) {
        return InlineKeyboardMarkup.builder().keyboardRow(buttons).build();
    }
}
