package ru.vadyxa.MrTeaCircleBot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.vadyxa.MrTeaCircleBot.configuration.ConfigurationBot;
import ru.vadyxa.MrTeaCircleBot.initbot.Bot;

@SpringBootApplication
@Slf4j
public class MrTeaCircleBotApplication {

	public static void main(String[] args) throws TelegramApiException {
		var ctx =  SpringApplication.run(MrTeaCircleBotApplication.class, args);
		TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
		var configurationBot = ctx.getBean(ConfigurationBot.class);

		var bot = new Bot(configurationBot);



		try {
			botsApi.registerBot(bot);
		} catch (TelegramApiException e) {
			log.error(e.getMessage());
		}

		bot.sendText(configurationBot.getUserId(), "/help");

	}
}
