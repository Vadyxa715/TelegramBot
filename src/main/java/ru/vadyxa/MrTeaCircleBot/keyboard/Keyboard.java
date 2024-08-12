package ru.vadyxa.MrTeaCircleBot.keyboard;

import lombok.AllArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class Keyboard {

    public static final String CREATE_LUNCH = "create_lunch";
    public static final String CHECK_PIDOR = "check_pidor";
    public static final String CREATE_LUNCH_TEXT = "Создать голосование: Время обеда";
    public static final String CHECK_PIDOR_TEXT = "Выбираем пидора дня";

    public static List<InlineKeyboardButton> buildButtons() {
        var keyboardButtonArrayList = new ArrayList<InlineKeyboardButton>();
        keyboardButtonArrayList.add(InlineKeyboardButton.builder().text(CREATE_LUNCH_TEXT).callbackData(CREATE_LUNCH).build());
        keyboardButtonArrayList.add(InlineKeyboardButton.builder().text(CHECK_PIDOR_TEXT).callbackData(CHECK_PIDOR).build());
        return keyboardButtonArrayList;
    }

    public static InlineKeyboardMarkup build(List<InlineKeyboardButton> buttons) {
        return InlineKeyboardMarkup.builder().keyboardRow(buttons).build();
    }
}
