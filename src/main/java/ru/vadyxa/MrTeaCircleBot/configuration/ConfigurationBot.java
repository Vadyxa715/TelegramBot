package ru.vadyxa.MrTeaCircleBot.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Getter
@Setter
@Configuration
@PropertySource("application.properties")
public class ConfigurationBot {

    @Value("${token}")
    private String token;
}
