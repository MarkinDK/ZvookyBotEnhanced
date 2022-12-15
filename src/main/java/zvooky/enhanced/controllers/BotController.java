package zvooky.enhanced.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.objects.Update;

@RestController
@RequestMapping("/callback/webhook")
public class BotController {
    private final TelegramWebhookBot zvookyBot;

    public BotController(TelegramWebhookBot zvookyBot) {
        this.zvookyBot = zvookyBot;
    }

    @PostMapping
    public void handleRequest(@RequestBody Update update) {
        zvookyBot.onWebhookUpdateReceived(update);
    }
}
