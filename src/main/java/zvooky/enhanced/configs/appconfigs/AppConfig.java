package zvooky.enhanced.configs.appconfigs;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import org.telegram.telegrambots.updatesreceivers.DefaultWebhook;
import zvooky.enhanced.configs.properties.BotPropertiesHolder;
import zvooky.enhanced.services.update.UpdateHandlerService;
import zvooky.enhanced.webhookbot.ZvookyBot;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@Configuration
public class AppConfig {
    private final BotPropertiesHolder botPropertiesHolder;

    public AppConfig(BotPropertiesHolder botPropertiesHolder) {
        this.botPropertiesHolder = botPropertiesHolder;
    }

    @Bean
    public SetWebhook setWebhookInstance() {
        return SetWebhook.builder().url(botPropertiesHolder.getNgrokUrl()).build();
    }

    @Bean
    @Primary
    public TelegramWebhookBot getTelegramWebhookBot(
            UpdateHandlerService updateHandlerService,
            SetWebhook setWebhook) throws TelegramApiException {

        ZvookyBot bot = new ZvookyBot(updateHandlerService);

        bot.setPath(botPropertiesHolder.getControllerEndpoint());
        bot.setToken(botPropertiesHolder.getToken());
        bot.setUsername(botPropertiesHolder.getUsername());
        Logger.getLogger(OkHttpClient.class.getName()).setLevel(Level.FINE);

        deleteWebhook(botPropertiesHolder.getToken());

        bot.setWebhook(setWebhook);

        DefaultWebhook webhook = new DefaultWebhook();
        webhook.setInternalUrl("http://localhost:80");

        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class, webhook);
        telegramBotsApi.registerBot(bot, setWebhook);

        return bot;
    }


    private void deleteWebhook(String token) {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .followRedirects(false)
                .build();
        Request request = new Request.Builder()
                .url("https://api.telegram.org/bot" + token + "/deleteWebhook")
                .build();
        try (Response response = client.newCall(request).execute()) {
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
