package ru.vadyxa.MrTeaCircleBot.configuration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.Arrays;

@Getter
@ToString
@AllArgsConstructor
public enum EnumCommandConstantArgs {

    MENU("/menu"),
    SETTINGS("/settings"),
    HELP("/help"),
    CREATE_ANSWER("/create_answer"),
    CHECK_PIDOR("/check_pidor"),
    LUNCH("/lunch"),
    DEFAULT("");

    private final String value;

    public static EnumCommandConstantArgs containsAndReturnEnum(String value) {
        try {
            return Arrays.stream(EnumCommandConstantArgs.values())
                    .filter(i -> i.value.equals(value))
                    .findFirst()
                    .orElseThrow(IllegalArgumentException::new);
        } catch (Exception e) {
            return DEFAULT;
        }
    }

    public static String getAll() {
        return "\n"
                + MENU.value + "\n"
                + SETTINGS.value + "\n"
                + HELP.value + "\n"
                + CREATE_ANSWER.value + "\n"
                + LUNCH.value + "\n"
                + CHECK_PIDOR.value + "\n";
    }
}
