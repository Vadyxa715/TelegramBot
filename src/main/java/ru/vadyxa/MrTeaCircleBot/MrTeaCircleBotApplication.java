package ru.vadyxa.MrTeaCircleBot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.vadyxa.MrTeaCircleBot.configuration.ConfigurationBot;
import ru.vadyxa.MrTeaCircleBot.initbot.Bot;

@SpringBootApplication
public class MrTeaCircleBotApplication {

	public static void main(String[] args) throws TelegramApiException {
		var ctx =  SpringApplication.run(MrTeaCircleBotApplication.class, args);
		var configurationBot = ctx.getBean(ConfigurationBot.class);

		var bot = new Bot(configurationBot);

		TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
		botsApi.registerBot(bot);

		bot.sendText(configurationBot.getUserId(), "Ты пидор!))");

	}
}
