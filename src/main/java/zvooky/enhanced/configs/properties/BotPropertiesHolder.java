package zvooky.enhanced.configs.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "telegrambot")
@Getter
@Setter
@PropertySource("classpath:application.yml")
public class BotPropertiesHolder {
    @Value("${username}")
    private String username;
    @Value("${token}")
    private String token;
    @Value("${ngrok_url}")
    private String ngrokUrl;
    @Value("${endpoint}")
    private String controllerEndpoint;
}
