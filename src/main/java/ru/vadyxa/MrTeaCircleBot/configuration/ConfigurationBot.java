package ru.vadyxa.MrTeaCircleBot.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Getter
@Setter
@Configuration
@PropertySource("classpath:application.properties")
public class ConfigurationBot {

    @Value("${source.token}")
    private String token;

    @Value("${source.bot.name}")
    private String botName;

    private Long userId = 916136185L;
}
